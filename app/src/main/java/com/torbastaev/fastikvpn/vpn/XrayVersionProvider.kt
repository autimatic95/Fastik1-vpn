package com.torbastaev.fastikvpn.vpn

import libv2ray.Libv2ray
import javax.inject.Inject

class XrayVersionProvider @Inject constructor() {

    fun getVersion(): String = try {
        // Вызов уходит в машинный код библиотеки и возвращает строку с версиями
        Libv2ray.checkVersionX()
    } catch (e: Throwable) {
        // Ловим даже UnsatisfiedLinkError (библиотека не загрузилась на этом процессоре),
        // чтобы приложение не падало при запуске
        "недоступно: ${e.message}"
    }
}