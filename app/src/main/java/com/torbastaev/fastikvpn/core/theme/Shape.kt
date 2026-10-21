package com.torbastaev.fastikvpn.core.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import com.torbastaev.fastikvpn.core.constants.Dimens

val AppShapes = Shapes(
    small = RoundedCornerShape(Dimens.IpCardRadius),
    medium = RoundedCornerShape(Dimens.EnterButtonRadius),
    large = RoundedCornerShape(Dimens.BottomBarRadius)
)