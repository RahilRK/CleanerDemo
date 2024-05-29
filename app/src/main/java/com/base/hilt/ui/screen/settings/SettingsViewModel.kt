package com.base.hilt.ui.screen.settings

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.base.hilt.R
import com.base.hilt.model.SettingsMenuDataItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
//    private val repository: MainRepository
) : ViewModel() {

    private val TAG = "SettingsViewModel"

    val settingsMenuList = mutableStateListOf(
            SettingsMenuDataItem(menuIcon = R.drawable.ic_restore_purchase, menuName = "Restore Purchase"),
            SettingsMenuDataItem(menuIcon = R.drawable.ic_rate_app, menuName = "Rate App"),
            SettingsMenuDataItem(menuIcon = R.drawable.ic_share_app, menuName = "Share App"),
            SettingsMenuDataItem(menuIcon = R.drawable.ic_change_plan, menuName = "Change Plan"),
            SettingsMenuDataItem(menuIcon = R.drawable.ic_cancel_subscription, menuName = "Cancel Subscription"),
            SettingsMenuDataItem(menuIcon = R.drawable.ic_theme, menuName = "Themes"),
            SettingsMenuDataItem(menuIcon = R.drawable.ic_contact_us, menuName = "Contact Us"),
            SettingsMenuDataItem(menuIcon = R.drawable.ic_terms_of_use, menuName = "Terms of Use"),
            SettingsMenuDataItem(menuIcon = R.drawable.ic_terms_of_use, menuName = "Privacy Policy"),
        )

   /* private var _settingsMenuList = MutableStateFlow<MutableList<SettingsMenuDataItem>>(radioButtons)
    val settingsMenuList: StateFlow<MutableList<SettingsMenuDataItem>>
        get() = _settingsMenuList*/

    init {

    }


}