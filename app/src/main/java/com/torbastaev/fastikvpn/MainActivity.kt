package com.torbastaev.fastikvpn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.torbastaev.fastikvpn.core.constants.Dimens
import com.torbastaev.fastikvpn.core.model.ThemeMode
import com.torbastaev.fastikvpn.core.theme.FastikTheme
import com.torbastaev.fastikvpn.core.theme.FastikVpnTheme
import com.torbastaev.fastikvpn.ui.mainscreen.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition { viewModel.themeMode.value == null }

        setContent {
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()

            FastikVpnTheme(themeMode = themeMode ?: ThemeMode.SYSTEM) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ThemeCheck(onThemeSelected = viewModel::onThemeSelected)
                }
            }
        }
    }
}

@Composable
private fun ThemeCheck(onThemeSelected: (ThemeMode) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = FastikTheme.colors.title
        )
        Box(
            modifier = Modifier
                .padding(Dimens.SpacingXl)
                .size(Dimens.FirstCircle)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(
                            FastikTheme.colors.connectGradientStart,
                            FastikTheme.colors.connectGradientEnd
                        )
                    )
                )
        )
        Text(
            text = stringResource(R.string.theme_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Column(
            modifier = Modifier.padding(top = Dimens.SpacingM),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingS)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingS)
            ) {
                Button(onClick = { onThemeSelected(ThemeMode.DARK) }) {
                    // Добавили maxLines = 1, чтобы текст не переносился
                    Text(stringResource(R.string.theme_dark), maxLines = 1)
                }
                Button(onClick = { onThemeSelected(ThemeMode.LIGHT) }) {
                    Text(stringResource(R.string.theme_light), maxLines = 1)
                }
            }

            Row {
                Button(onClick = { onThemeSelected(ThemeMode.SYSTEM) }) {
                    Text(stringResource(R.string.theme_system), maxLines = 1)
                }
            }
        }
    }
}