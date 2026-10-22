package com.torbastaev.fastikvpn.ui.mainscreen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.torbastaev.fastikvpn.core.constants.AnimationConstants
import com.torbastaev.fastikvpn.core.constants.Dimens
import com.torbastaev.fastikvpn.core.model.ThemeMode
import com.torbastaev.fastikvpn.ui.mainscreen.components.AppBottomBar
import com.torbastaev.fastikvpn.ui.mainscreen.components.AppTopBar
import com.torbastaev.fastikvpn.ui.navigation.AppTab
import com.torbastaev.fastikvpn.ui.screens.home.HomeScreen
import com.torbastaev.fastikvpn.ui.screens.login.LoginScreen
import com.torbastaev.fastikvpn.ui.screens.settings.SettingsScreen

@Composable
fun MainScreen(
    onThemeSelected: (ThemeMode) -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding()
    ) {
        AppTopBar()
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            Crossfade(
                targetState = selectedTab,
                animationSpec = tween(durationMillis = AnimationConstants.FADE_MS),
                label = "screenSwitch"
            ) { tab ->
                when (tab) {
                    AppTab.HOME -> HomeScreen()
                    AppTab.LOGIN -> LoginScreen()
                    AppTab.SETTINGS -> SettingsScreen(onThemeSelected = onThemeSelected)
                }
            }
        }
        AppBottomBar(
            selectedTab = selectedTab,
            onTabSelected = viewModel::onTabSelected,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = Dimens.SpacingM)
        )
    }
}
