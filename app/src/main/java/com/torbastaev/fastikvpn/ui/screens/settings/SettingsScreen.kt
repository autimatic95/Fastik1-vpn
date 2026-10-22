package com.torbastaev.fastikvpn.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.torbastaev.fastikvpn.R
import com.torbastaev.fastikvpn.core.constants.Dimens
import com.torbastaev.fastikvpn.core.model.ThemeMode

@Composable
fun SettingsScreen(
    onThemeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.stub_settings),
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