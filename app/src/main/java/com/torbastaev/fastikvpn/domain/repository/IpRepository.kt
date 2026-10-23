package com.torbastaev.fastikvpn.domain.repository

interface IpRepository {
    suspend fun getPublicIp(): String?
}