package com.torbastaev.fastikvpn.data.repository

import com.torbastaev.fastikvpn.domain.repository.IpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class IpRepositoryImpl @Inject constructor() : IpRepository {

    override suspend fun getPublicIp(): String? = withContext(Dispatchers.IO) {
        for (address in SOURCES) {
            val ip = requestIp(address)
            if (ip != null) return@withContext ip
        }
        null
    }

    private fun requestIp(address: String): String? {
        return try {
            val connection = URL(address).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = TIMEOUT_MS
            connection.readTimeout = TIMEOUT_MS
            try {
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val text = connection.inputStream.bufferedReader().use { it.readText() }.trim()
                    if (IP_REGEX.matches(text)) text else null
                } else null
            } finally {
                connection.disconnect()
            }
        } catch (e: IOException) {
            null
        }
    }

    private companion object {
        val SOURCES = listOf("https://api.ipify.org", "https://icanhazip.com")
        const val TIMEOUT_MS = 5_000
        val IP_REGEX = Regex("^[0-9a-fA-F:.]{3,45}$")
    }
}