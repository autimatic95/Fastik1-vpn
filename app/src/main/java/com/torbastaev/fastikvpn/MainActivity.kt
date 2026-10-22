package com.torbastaev.fastikvpn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.torbastaev.fastikvpn.core.model.ThemeMode
import com.torbastaev.fastikvpn.core.theme.FastikVpnTheme
import com.torbastaev.fastikvpn.ui.mainscreen.MainScreen
import com.torbastaev.fastikvpn.ui.mainscreen.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition { viewModel.themeMode.value == null }

        setContent {
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()

            FastikVpnTheme(themeMode = themeMode ?: ThemeMode.SYSTEM) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(onThemeSelected = viewModel::onThemeSelected)
                }
            }
        }
    }
}