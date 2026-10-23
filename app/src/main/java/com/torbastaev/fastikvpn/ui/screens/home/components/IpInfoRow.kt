package com.torbastaev.fastikvpn.ui.screens.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.torbastaev.fastikvpn.R
import com.torbastaev.fastikvpn.core.constants.AnimationConstants
import com.torbastaev.fastikvpn.core.constants.Dimens
import com.torbastaev.fastikvpn.core.model.ConnectionState
import com.torbastaev.fastikvpn.core.theme.FastikTheme

@Composable
fun IpInfoRow(
    ipAddress: String?,
    isIpLoading: Boolean,
    pingMs: Int?,
    isPingLoading: Boolean,
    isPingVisible: Boolean,
    canRefreshPing: Boolean,
    connectionState: ConnectionState,
    onCardClick: () -> Unit,
    onRefreshPingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.width(Dimens.BottomBarWidth),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IpCard(
            ipAddress = ipAddress,
            isLoading = isIpLoading,
            isPingVisible = isPingVisible,
            connectionState = connectionState,
            onClick = onCardClick
        )
        AnimatedVisibility(
            visible = isPingVisible,
            enter = fadeIn(tween(AnimationConstants.FADE_MS)) +
                    expandHorizontally(tween(AnimationConstants.FADE_MS)),
            exit = fadeOut(tween(AnimationConstants.FADE_MS)) +
                    shrinkHorizontally(tween(AnimationConstants.FADE_MS))
        ) {
            PingBadge(
                pingMs = pingMs,
                isLoading = isPingLoading,
                canRefresh = canRefreshPing,
                onRefreshClick = onRefreshPingClick,
                modifier = Modifier.padding(start = Dimens.IpPingGap)
            )
        }
    }
}
@Composable
private fun IpCard(
    ipAddress: String?,
    isLoading: Boolean,
    isPingVisible: Boolean,
    connectionState: ConnectionState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = MaterialTheme.shapes.small
    val arrowRotation by animateFloatAsState(
        targetValue = if (isPingVisible) 180f else 0f,
        animationSpec = tween(durationMillis = AnimationConstants.FADE_MS),
        label = "arrowRotation"
    )
    val ipText = when {
        isLoading -> stringResource(R.string.ip_loading)
        connectionState != ConnectionState.CONNECTED -> "IP скрыт" // Если не подключено
        ipAddress != null -> ipAddress
        else -> stringResource(R.string.ip_error)
    }

    Surface(
        modifier = modifier
            .width(Dimens.IpCardWidth)
            .height(Dimens.IpCardHeight)
            .clip(shape)
            .clickable(
                enabled = connectionState == ConnectionState.CONNECTED,
                onClick = onClick),
        shape = shape,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.SpacingS),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingS)
        ) {
            if (connectionState == ConnectionState.CONNECTED) {
                Icon(
                    imageVector = Icons.Outlined.Public,
                    contentDescription = null,
                    tint = FastikTheme.colors.navIconActive,
                    modifier = Modifier.size(Dimens.IpIconHeight)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.ip_label),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = ipText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier
                    .size(Dimens.ActionIconSize)
                    .graphicsLayer { rotationZ = arrowRotation }
            )
        }
    }
}
@Composable
private fun PingBadge(
    pingMs: Int?,
    isLoading: Boolean,
    canRefresh: Boolean,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(Dimens.PingBadgeWidth)
            .height(Dimens.IpCardHeight),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = Dimens.SpacingM),
            verticalAlignment = Alignment.CenterVertically,
            // Текст слева, значок обновления справа
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Crossfade(
                targetState = isLoading,
                animationSpec = tween(durationMillis = AnimationConstants.FADE_MS),
                label = "pingText"
            ) { loading ->
                Text(
                    text = when {
                        loading -> stringResource(R.string.ping_loading)
                        pingMs != null -> stringResource(R.string.ping_value, pingMs)
                        else -> stringResource(R.string.ping_unknown)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            val spin = if (isLoading) rememberSpin() else 0f

            IconButton(
                onClick = onRefreshClick,
                enabled = canRefresh && !isLoading
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(R.string.cd_refresh_ping),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(Dimens.ActionIconSize)
                        .graphicsLayer { rotationZ = spin }
                )
            }
        }
    }
}
@Composable
private fun rememberSpin(): Float {
    val transition = rememberInfiniteTransition(label = "spinTransition")

    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AnimationConstants.SPIN_MS,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )
    return angle
}