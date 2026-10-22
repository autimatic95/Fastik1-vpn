package com.torbastaev.fastikvpn.ui.mainscreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.torbastaev.fastikvpn.R
import com.torbastaev.fastikvpn.core.constants.Dimens
import com.torbastaev.fastikvpn.core.theme.FastikTheme

@Composable
fun AppTopBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.ScreenPadding, vertical = Dimens.SpacingM),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = FastikTheme.colors.title
        )
    }
}