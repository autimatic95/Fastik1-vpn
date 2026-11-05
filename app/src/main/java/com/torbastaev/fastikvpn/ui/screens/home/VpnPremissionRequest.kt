package com.torbastaev.fastikvpn.ui.screens.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

// Возвращает функцию "запросить разрешения и подключиться".
// Цепочка: разрешение на уведомления -> разрешение на VPN -> onReady().

@Composable
fun rememberVpnPermissionRequest(onReady: () -> Unit): () -> Unit {
    val context = LocalContext.current

    // rememberUpdatedState: ловушка создаётся один раз, но всегда вызывает
    // самую свежую версию onReady, а не ту, что была в момент создания
    val currentOnReady by rememberUpdatedState(onReady)
    val vpnLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            currentOnReady()
        }
    }

    // Шаг 2: проверка разрешения на VPN.
    fun askVpnPermission() {
        val intent = VpnService.prepare(context)
        if (intent == null) {
            currentOnReady()
        } else {
            vpnLauncher.launch(intent)
        }
    }
    // Ловушка для окна разрешения на уведомления.
    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        askVpnPermission()
    }

    // Возвращаем функцию, которую вызовет нажатие на кнопку (шаг 1)
    return {
        // Разрешение на уведомления существует только на Android 13 и новее
        val needNotificationPermission =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED

        if (needNotificationPermission) {
            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            askVpnPermission()
        }
    }
}