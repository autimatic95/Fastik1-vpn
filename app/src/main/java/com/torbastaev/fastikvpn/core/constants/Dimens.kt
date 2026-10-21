package com.torbastaev.fastikvpn.core.constants

import androidx.compose.ui.unit.dp

object Dimens {

    private const val DESIGN_WIDTH_PX = 1440f
    private const val TARGET_WIDTH_DP = 390f
    private const val FIGMA_SCALE = TARGET_WIDTH_DP / DESIGN_WIDTH_PX

    private fun fromDesign(px: Int) = (px * FIGMA_SCALE).dp

    //  Круги кнопки подключения
    val FirstCircle = fromDesign(742)
    val SecondCircle = fromDesign(928)
    val ThirdCircle = fromDesign(1100)

    // Иконка Wi-Fi
    val WifiIconWidth = fromDesign(437)
    val WifiIconHeight = fromDesign(350)

    // Нижнее меню
    val BottomBarWidth = fromDesign(1240)
    val BottomBarHeight = fromDesign(242)
    val BottomBarRadius = fromDesign(26)
    val NavIconSize = fromDesign(100)

    // «Войти»
    val EnterButtonWidth = fromDesign(835)
    val EnterButtonHeight = fromDesign(130)
    val EnterButtonRadius = fromDesign(40)

    // Карточка IP
    val IpCardWidth = fromDesign(676)
    val IpCardHeight = fromDesign(243)
    val IpCardRadius = fromDesign(26)
    val IpIconWidth = fromDesign(178)
    val IpIconHeight = fromDesign(113)

    // отступы
    val SpacingXs = 4.dp
    val SpacingS = 8.dp
    val SpacingM = 16.dp
    val SpacingL = 24.dp
    val SpacingXl = 32.dp
    val ScreenPadding = 20.dp
}