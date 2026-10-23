package com.torbastaev.fastikvpn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.torbastaev.fastikvpn.core.model.ConnectionState
import com.torbastaev.fastikvpn.domain.repository.IpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ipRepository: IpRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var ipJob: Job? = null
    private var pingJob: Job? = null

    init {
        refreshIp()
    }

    fun onConnectClick() {
        when (_uiState.value.connectionState) {
            ConnectionState.DISCONNECTED -> connect()
            ConnectionState.CONNECTED -> disconnect()
            ConnectionState.CONNECTING -> Unit
        }
    }

    fun onIpCardClick() {
        // Пинг раскрывается только если VPN подключен
        if (_uiState.value.connectionState == ConnectionState.CONNECTED) {
            _uiState.update { it.copy(isPingVisible = !it.isPingVisible) }
        }
    }

    fun onPingRefreshClick() {
        val state = _uiState.value
        if (state.connectionState == ConnectionState.CONNECTED && !state.isPingLoading) {
            measurePing()
        }
    }

    private fun connect() {
        _uiState.update { it.copy(connectionState = ConnectionState.CONNECTING) }

        viewModelScope.launch {
            delay(CONNECT_DELAY_MS)
            _uiState.update { it.copy(connectionState = ConnectionState.CONNECTED) }
            refreshIp()
            measurePing()
        }
    }

    private fun disconnect() {
        pingJob?.cancel()
        _uiState.update {
            it.copy(
                connectionState = ConnectionState.DISCONNECTED,
                pingMs = null,
                isPingLoading = false,
                isPingVisible = false
            )
        }
        refreshIp()
    }

    private fun refreshIp() {
        ipJob?.cancel()
        _uiState.update { it.copy(isIpLoading = true) }

        ipJob = viewModelScope.launch {
            val ip = ipRepository.getPublicIp()
            _uiState.update { it.copy(ipAddress = ip, isIpLoading = false) }
        }
    }

    private fun measurePing() {
        pingJob?.cancel()
        _uiState.update { it.copy(isPingLoading = true) }

        pingJob = viewModelScope.launch {
            delay(Random.nextLong(PING_MIN_DELAY_MS, PING_MAX_DELAY_MS))
            val ping = Random.nextInt(PING_MIN_MS, PING_MAX_MS + 1)
            _uiState.update { it.copy(pingMs = ping, isPingLoading = false) }
        }
    }

    private companion object {
        const val CONNECT_DELAY_MS = 800L
        const val PING_MIN_MS = 30
        const val PING_MAX_MS = 150
        const val PING_MIN_DELAY_MS = 400L
        const val PING_MAX_DELAY_MS = 1200L
    }
}