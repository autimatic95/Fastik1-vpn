package com.torbastaev.fastikvpn.ui.mainscreen

import androidx.lifecycle.ViewModel
import com.torbastaev.fastikvpn.ui.navigation.AppTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    private val _selectedTab = MutableStateFlow(AppTab.HOME)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    fun onTabSelected(tab: AppTab) {
        _selectedTab.value = tab
    }
}