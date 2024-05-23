package com.appname.structure.user.utils

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import com.base.hilt.MainActivity
import com.base.hilt.utils.Constants

/**
 * ActivityTestRule for [MainActivity] that can launch with any initial navigation target.
 */
class MainActivityTestRule(
    private val initialNavId: Int,
    private val graphId: Int=-1
) : ActivityTestRule<MainActivity>(MainActivity::class.java) {

    override fun getActivityIntent(): Intent {
        return Intent(Intent.ACTION_MAIN).apply {
            putExtra(Constants.KEY_START_DESTINATION, initialNavId)
            putExtra(Constants.KEY_GRAPH_ID, graphId)
        }
    }
}
