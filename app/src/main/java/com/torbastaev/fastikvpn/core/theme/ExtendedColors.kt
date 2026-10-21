package com.torbastaev.fastikvpn.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ExtendedColors(
    // Круг кнопки подключения
    val connectGradientStart: Color,
    val connectGradientEnd: Color,
    // Внешние круги свечения вокруг кнопки
    val connectRingSecond: Color,
    val connectRingThird: Color,
    // Цвет заголовков и названия приложения
    val title: Color,
    // «Войти» и текст на ней
    val accentButton: Color,
    val onAccentButton: Color,
    // кнопка пользователь не авторизован
    val disabledContainer: Color,
    val disabledContent: Color,
    // Иконки нижнего меню
    val navIconActive: Color,
    val navIconInactive: Color,
    val error: Color
)

/** Тёмная тема. */
val DarkExtendedColors = ExtendedColors(
    connectGradientStart = CircleGradientStart,
    connectGradientEnd = CircleGradientEnd,
    connectRingSecond = CircleSecond,
    connectRingThird = CircleThird,
    title = TitlePurple,
    accentButton = BgButton,
    onAccentButton = TextWhite,
    disabledContainer = DisabledDark,
    disabledContent = InactiveIconDark,
    navIconActive = CircleGradientStart,
    navIconInactive = InactiveIconDark,
    error = ErrorRed
)

/** Светлая тема. */
val LightExtendedColors = ExtendedColors(
    connectGradientStart = CircleGradientStart,
    connectGradientEnd = CircleGradientEnd,
    connectRingSecond = CircleSecond,
    connectRingThird = CircleThird,
    title = TitlePurple,
    accentButton = BgButton,
    onAccentButton = TextWhite,
    disabledContainer = DisabledLight,
    disabledContent = InactiveIconLight,
    navIconActive = BgButton,
    navIconInactive = InactiveIconLight,
    error = ErrorRed
)

val LocalExtendedColors = staticCompositionLocalOf { DarkExtendedColors }