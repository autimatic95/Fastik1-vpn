package com.torbastaev.fastikvpn.ui.screens.home

import com.torbastaev.fastikvpn.core.model.ConnectionState

data class HomeUiState(
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val ipAddress: String? = null,
    val isIpLoading: Boolean = true,
    val pingMs: Int? = null,
    val isPingLoading: Boolean = false,
    val isPingVisible: Boolean = false
)