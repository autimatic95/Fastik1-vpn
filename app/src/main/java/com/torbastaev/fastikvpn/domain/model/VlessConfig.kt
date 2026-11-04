package com.torbastaev.fastikvpn.domain.model
//  data class сам создаёт copy(), equals() и hashCode().
data class VlessConfig(
    // Подпись для человека (часть ссылки после знака #)
    val name: String,

    // IP-адрес
    val address: String,

    // Порт сервера
    val port: Int,

    // Личный идентификатор пользователя
    val uuid: String,

    // Режим ускорения
    val flow: String,

    //  tcp
    val network: String,

    // Способ защиты: reality, tls или none
    val security: String,

    // Имя сайта, под который маскируется соединение
    val sni: String,

    // Отпечаток браузера, под который подделывается шифрование (firefox)
    val fingerprint: String,

    // Публичный ключ сервера для Reality
    val publicKey: String,

    // Короткий идентификатор Reality
    val shortId: String,

    // Путь SpiderX для Reality
    val spiderX: String
) {
    override fun toString(): String =
        "VlessConfig(name=$name, address=$address:$port, network=$network, " +
                "security=$security, flow=${flow.ifBlank { "нет" }}, uuid=***, publicKey=***)"
}