package com.torbastaev.fastikvpn

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import android.util.Log
import com.torbastaev.fastikvpn.data.source.TestVlessSource
import com.torbastaev.fastikvpn.vpn.XrayVersionProvider
import javax.inject.Inject

@HiltAndroidApp
class FastikVpnApplication : Application() {

    @Inject lateinit var xrayVersionProvider: XrayVersionProvider
    @Inject lateinit var testVlessSource: TestVlessSource

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            logCoreSmokeTest()
        }
    }
    private fun logCoreSmokeTest() {
        Log.i(TAG, "Версия Xray: ${xrayVersionProvider.getVersion()}")

        testVlessSource.getConfig()
            .onSuccess { Log.i(TAG, "Сервер разобран: $it") }
            .onFailure { Log.w(TAG, "Не удалось разобрать ссылку: ${it.message}") }
    }

    private companion object {
        const val TAG = "FastikVPN"
    }
}