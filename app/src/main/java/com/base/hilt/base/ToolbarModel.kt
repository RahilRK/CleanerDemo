package com.base.hilt.base

data class ToolbarModel(
    var isVisible: Boolean = false,
    var title: String? = null,
    var isBottomNavVisible: Boolean,
    var isToolbarWithBackVisible: Boolean = false,
    var isBackBtnVisible: Boolean = true
)