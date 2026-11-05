package com.torbastaev.fastikvpn.vpn

import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log
import com.torbastaev.fastikvpn.R
import com.torbastaev.fastikvpn.core.model.ConnectionState
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import javax.inject.Inject

// Сам VPN-сервис. Его задача на этом этапе: создать виртуальную сетевую карту (TUN),
// @AndroidEntryPoint позволяет Hilt заполнить поля, помеченные @Inject
@AndroidEntryPoint
class FastikVpnService : VpnService() {

    // Hilt подставит тот же объект, что видит экран
    @Inject lateinit var statusHolder: VpnStatusHolder

    // уведомления
    @Inject lateinit var notificationFactory: VpnNotificationFactory

    // Открытая виртуальная карта.
    // ParcelFileDescriptor - обёртка над файловым дескриптором (номером открытого "файла").
    // этот номер позже получит Xray.
    private var tunInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startTunnel()
            ACTION_STOP -> stopTunnel()
            // Команда пришла без действия (например, после перезапуска системой): останавливаемся
            else -> stopSelf()
        }
        // NOT_STICKY: если систему вынудили убить сервис, не перезапускать его самой.
        return START_NOT_STICKY
    }

    // Включение туннеля
    private fun startTunnel() {
        if (tunInterface != null) return

        statusHolder.update(ConnectionState.CONNECTING)

        // Правило Android: после startForegroundService сервис обязан в течение ~5 секунд
        // вызвать startForeground с уведомлением, иначе приложение будет остановлено.
        showForegroundNotification()
        // Создаём виртуальную карту. Любая ошибка не должна ронять приложение.
        val tun = try {
            createTunnelBuilder().establish()
        } catch (e: Exception) {
            Log.e(TAG, "Не удалось создать туннель: ${e.message}")
            null
        }
        // establish() возвращает null, если разрешение на VPN пропало
        if (tun == null) {
            stopTunnel()
            return
        }

        tunInterface = tun
        statusHolder.update(ConnectionState.CONNECTED)
        Log.i(TAG, "Туннель создан")
    }

    // Показывает постоянное уведомление и переводит сервис в режим "на переднем плане"
    private fun showForegroundNotification() {
        val notification = notificationFactory.build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // Android 14 и новее: нужно указать тип сервиса
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            // Старые версии тип не требуют
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    // Описывает параметры виртуальной карты.
    private fun createTunnelBuilder(): Builder {
        val builder = Builder()
            // Название, которое пользователь увидит в системных настройках VPN
            .setSession(getString(R.string.app_name))
            // Максимальный размер пакета
            .setMtu(TUN_MTU)
            // Адрес самой виртуальной карты внутри телефона (IPv4 и IPv6)
            .addAddress(TUN_ADDRESS_V4, TUN_PREFIX_V4)
            .addAddress(TUN_ADDRESS_V6, TUN_PREFIX_V6)
            // Какой трафик направить в туннель. Нулевой маршрут означает "весь".
            // IPv6 тоже нужен, иначе трафик по нему пошёл бы мимо VPN (утечка)
            .addRoute("0.0.0.0", 0)
            .addRoute("::", 0)
            // DNS-серверы внутри туннеля, чтобы запросы имён не утекали мимо VPN
            .addDnsServer(DNS_PRIMARY)
            .addDnsServer(DNS_SECONDARY)

        if (EXCLUDE_SELF) {
            try {
                // Исключаем наше приложение из туннеля: ядро Xray работает внутри него,
                // и его собственный трафик к серверу не должен возвращаться в туннель
                builder.addDisallowedApplication(packageName)
            } catch (e: Exception) {
                Log.w(TAG, "Не удалось исключить приложение из туннеля: ${e.message}")
            }
        }
        return builder
    }

    // Выключение туннеля
    private fun stopTunnel() {
        closeTunnel()
        statusHolder.update(ConnectionState.DISCONNECTED)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    // Закрывает виртуальную карту. Закрытие дескриптора = выключение туннеля.
    private fun closeTunnel() {
        try {
            tunInterface?.close()
        } catch (e: IOException) {
            Log.w(TAG, "Ошибка при закрытии туннеля: ${e.message}")
        }
        tunInterface = null
    }

    // Система вызывает, когда пользователь отозвал разрешение на VPN
    // или включил другое VPN-приложение, которое заняло наше место
    override fun onRevoke() {
        Log.i(TAG, "Разрешение на VPN отозвано")
        stopTunnel()
        super.onRevoke()
    }

    // Страховка: если сервис уничтожается неожиданно, туннель всё равно закрываем
    override fun onDestroy() {
        closeTunnel()
        statusHolder.update(ConnectionState.DISCONNECTED)
        super.onDestroy()
    }

    companion object {
        private const val TAG = "FastikVPN"

        const val ACTION_START = "com.torbastaev.fastikvpn.action.START"
        const val ACTION_STOP = "com.torbastaev.fastikvpn.action.STOP"

        private const val NOTIFICATION_ID = 1001

        // Параметры виртуальной карты
        private const val TUN_MTU = 1500
        private const val TUN_ADDRESS_V4 = "172.19.0.1"
        private const val TUN_PREFIX_V4 = 30
        private const val TUN_ADDRESS_V6 = "fdfe:dcba:9876::1"
        private const val TUN_PREFIX_V6 = 126
        private const val DNS_PRIMARY = "1.1.1.1"
        private const val DNS_SECONDARY = "8.8.8.8"

        // Исключать ли наше приложение из туннеля.
        private const val EXCLUDE_SELF = true

        // Готовая команда "включить"
        fun startIntent(context: Context): Intent =
            Intent(context, FastikVpnService::class.java).setAction(ACTION_START)

        // Готовая команда "выключить"
        fun stopIntent(context: Context): Intent =
            Intent(context, FastikVpnService::class.java).setAction(ACTION_STOP)
    }
}