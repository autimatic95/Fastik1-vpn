package com.torbastaev.fastikvpn.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.vector.ImageVector
import com.torbastaev.fastikvpn.R

enum class AppTab(
    val icon: ImageVector,
    @StringRes val labelRes: Int
) {
    HOME(Icons.Filled.Home, R.string.tab_home),
    LOGIN(Icons.Filled.Person, R.string.tab_login),
    SETTINGS(Icons.Filled.Tune, R.string.tab_settings)
}