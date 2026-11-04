package com.torbastaev.fastikvpn.data.source

import com.torbastaev.fastikvpn.BuildConfig
import com.torbastaev.fastikvpn.data.vless.VlessLinkParser
import com.torbastaev.fastikvpn.domain.model.VlessConfig
import javax.inject.Inject

// Временный источник: достаёт тестовую ссылку, которую мы положили в local.properties,
// и разбирает её. Позже его заменят настоящие источники (хранилище, подписка)
class TestVlessSource @Inject constructor(
    // Hilt сам подставит парсер
    private val parser: VlessLinkParser
) {

    fun getConfig(): Result<VlessConfig> {
        val link = BuildConfig.VLESS_TEST_LINK

        // Если ссылку не задана - ошибка
        if (link.isBlank()) {
            return Result.failure(
                IllegalStateException("В local.properties не задана VLESS_TEST_LINK")
            )
        }
        return parser.parse(link)
    }
}