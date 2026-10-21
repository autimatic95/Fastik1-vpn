package com.torbastaev.fastikvpn.domain.repository

import com.torbastaev.fastikvpn.core.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeMode: Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
}