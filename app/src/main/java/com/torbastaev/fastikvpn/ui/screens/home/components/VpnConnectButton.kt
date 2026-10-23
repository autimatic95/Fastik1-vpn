package com.torbastaev.fastikvpn.ui.screens.home.components

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.torbastaev.fastikvpn.R
import com.torbastaev.fastikvpn.core.constants.AnimationConstants
import com.torbastaev.fastikvpn.core.constants.Dimens
import com.torbastaev.fastikvpn.core.model.ConnectionState
import com.torbastaev.fastikvpn.core.theme.FastikTheme

@Composable
fun VpnConnectButton(
    connectionState: ConnectionState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val glowTarget = when (connectionState) {
        ConnectionState.DISCONNECTED -> 0f
        ConnectionState.CONNECTING -> 0.5f
        ConnectionState.CONNECTED -> 1f
    }

    val glow by animateFloatAsState(
        targetValue = glowTarget,
        animationSpec = tween(durationMillis = AnimationConstants.CONNECT_BUTTON_MS),
        label = "glow"
    )

    val pulse = if (glow > 0f) rememberPulse() else 0f
    val breath = 1f - 0.4f * glow * (1f - pulse)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "pressScale"
    )
    val description = stringResource(R.string.cd_connect_button)
    val canClick = enabled && connectionState != ConnectionState.CONNECTING

    Box(
        modifier = modifier.size(Dimens.ThirdCircle),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = pressScale
                    scaleY = pressScale
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.ThirdCircle)
                    .graphicsLayer {
                        val scale = 1f + 0.10f * pulse * glow
                        scaleX = scale
                        scaleY = scale
                        alpha = (0.06f + 0.14f * glow) * breath
                    }
                    .background(FastikTheme.colors.connectRingThird, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(Dimens.SecondCircle)
                    .graphicsLayer {
                        val scale = 1f + 0.06f * pulse * glow
                        scaleX = scale
                        scaleY = scale
                        alpha = (0.12f + 0.18f * glow) * breath
                    }
                    .background(FastikTheme.colors.connectRingSecond, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(Dimens.FirstCircle)
                    .graphicsLayer {
                        val scale = 1f + 0.03f * pulse * glow
                        scaleX = scale
                        scaleY = scale
                        alpha = (0.55f + 0.45f * glow) * (1f - 0.15f * glow * (1f - pulse))
                    }
                    .clip(CircleShape)
                    .background(
                        if (enabled) {
                            Brush.linearGradient(
                                listOf(
                                    FastikTheme.colors.connectGradientStart,
                                    FastikTheme.colors.connectGradientEnd
                                )
                            )
                        } else {
                            SolidColor(FastikTheme.colors.disabledContainer)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Wifi,
                    contentDescription = null,
                    tint = if (enabled) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        FastikTheme.colors.disabledContent
                    },
                    modifier = Modifier.size(Dimens.WifiIconWidth)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .semantics { contentDescription = description }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = canClick,
                    role = Role.Button,
                    onClick = onClick
                )
        )
    }
}
@Composable
private fun rememberPulse(): Float {
    val transition = rememberInfiniteTransition(label = "pulseTransition")

    val value by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AnimationConstants.PULSE_MS,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    return value
}