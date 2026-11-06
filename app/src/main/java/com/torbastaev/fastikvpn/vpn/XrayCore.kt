package com.torbastaev.fastikvpn.vpn

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import libv2ray.CoreCallbackHandler
import libv2ray.CoreController
import libv2ray.Libv2ray
import javax.inject.Inject

// Обёртка над ядром Xray. Все обращения к библиотеке libv2ray собраны в этом классе.
class XrayCore @Inject constructor(
    // Контекст всего приложения, нужен для пути к папке с файлами
    @ApplicationContext private val context: Context
) {

    // Контроллер запущенного ядра.
    private var controller: CoreController? = null

    // Ядро умеет сообщать нам о своих событиях. Для этого мы даём ему объект с тремя методами.
    private val callbackHandler = object : CoreCallbackHandler {

        // Ядро запустилось
        override fun startup(): Long {
            Log.i(TAG, "Ядро Xray: запущено")
            return 0
        }

        // Ядро остановилось
        override fun shutdown(): Long {
            Log.i(TAG, "Ядро Xray: остановлено")
            return 0
        }

        // Ядро прислало сообщение о состоянии.
        override fun onEmitStatus(code: Long, message: String?): Long {
            Log.i(TAG, "Ядро Xray: $message")
            return 0
        }
    }
    // Запускает ядро.
    // configJson - настройки в формате JSON (их собирает XrayConfigBuilder),
    // @Synchronized: пока один поток выполняет этот метод, остальные ждут.
    @Synchronized
    fun start(configJson: String, tunFd: Int) {
        if (controller != null) return
        // Готовим окружение ядра: папка для файлов и ключ шифрования XUDP (пока не нужен).
        Libv2ray.initCoreEnv(context.filesDir.absolutePath, "")
        // Создаём контроллер и передаём ему наш обработчик событий
        val newController = Libv2ray.newCoreController(callbackHandler)
        newController.startLoop(configJson, tunFd)
        controller = newController
    }
    // Останавливает ядро. Если оно не запущено
    @Synchronized
    fun stop() {
        val current = controller ?: return
        // Сначала забываем контроллер, чтобы повторная остановка не сработала дважды
        controller = null
        try {
            current.stopLoop()
        } catch (e: Exception) {
            Log.w(TAG, "Ошибка при остановке ядра: ${e.javaClass.simpleName}")
        }
    }
    private companion object {
        const val TAG = "FastikVPN"
    }
}