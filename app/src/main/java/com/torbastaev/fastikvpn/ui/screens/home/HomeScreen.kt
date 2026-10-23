package com.torbastaev.fastikvpn.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.torbastaev.fastikvpn.core.constants.Dimens
import com.torbastaev.fastikvpn.core.model.ConnectionState
import com.torbastaev.fastikvpn.ui.screens.home.components.ConnectionStatusText
import com.torbastaev.fastikvpn.ui.screens.home.components.IpInfoRow
import com.torbastaev.fastikvpn.ui.screens.home.components.VpnConnectButton

@Composable
        fun HomeScreen(
            modifier: Modifier = Modifier,
            viewModel: HomeViewModel = hiltViewModel()
        ) {
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            HomeContent(
                state = state,
                onConnectClick = viewModel::onConnectClick,
                onIpCardClick = viewModel::onIpCardClick,
                onPingRefreshClick = viewModel::onPingRefreshClick,
                modifier = modifier
            )
        }
        @Composable
        private fun HomeContent(
            state: HomeUiState,
            onConnectClick: () -> Unit,
            onIpCardClick: () -> Unit,
            onPingRefreshClick: () -> Unit,
            modifier: Modifier = Modifier
        ) {
            Column(modifier = modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VpnConnectButton(
                        connectionState = state.connectionState,
                        onClick = onConnectClick
                    )

                    Spacer(modifier = Modifier.height(Dimens.SpacingL))

                    ConnectionStatusText(
                        connectionState = state.connectionState
                    )
                }
                IpInfoRow(
                    ipAddress = state.ipAddress,
                    isIpLoading = state.isIpLoading,
                    pingMs = state.pingMs,
                    isPingLoading = state.isPingLoading,
                    isPingVisible = state.isPingVisible,
                    canRefreshPing = state.connectionState == ConnectionState.CONNECTED,
                    onCardClick = onIpCardClick,
                    onRefreshPingClick = onPingRefreshClick,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = Dimens.SpacingM)
                )
            }
        }