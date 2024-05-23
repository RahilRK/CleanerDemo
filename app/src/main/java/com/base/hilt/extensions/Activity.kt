package com.base.hilt.extensions

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.base.hilt.utils.Constants


inline fun <reified T : Any> Activity.getIntValue(extra: String): T {
    val bundle = intent.getBundleExtra(Constants.KEY_BUNDLE)
    return if (bundle != null && bundle.containsKey(extra)) {
        intent.getBundleExtra(Constants.KEY_BUNDLE)?.getInt(extra) as T
    } else {
        -1 as T
    }
}

//UnUsed Code
/*inline fun <reified T : Any> Activity.getStringValue(extra: String): T {
    val bundle = intent.getBundleExtra(Constants.KEY_BUNDLE)
    return if (bundle != null && bundle.containsKey(extra) && bundle.getString(extra) != null) {
        intent.getBundleExtra(Constants.KEY_BUNDLE)?.getString(extra) as T
    } else {
        "" as T
    }
}

inline fun <reified T : Any> Activity.getLongValue(extra: String): T {
    val bundle = intent.getBundleExtra(Constants.KEY_BUNDLE)
    return if (bundle != null && bundle.containsKey(extra) && bundle.getLong(extra) != null) {
        intent.getBundleExtra(Constants.KEY_BUNDLE)?.getLong(extra) as T
    } else {
        -1L as T
    }
}

inline fun <reified T : Any> Activity.getBooleanValue(extra: String): T {
    val bundle = intent.getBundleExtra(Constants.KEY_BUNDLE)
    return if (bundle != null && bundle.containsKey(extra) && bundle.getBoolean(extra) != null) {
        intent.getBundleExtra(Constants.KEY_BUNDLE)?.getBoolean(extra) as T
    } else {
        false as T
    }
}

fun AppCompatActivity.setToolbar(
    toolbar: Toolbar,
    tvTitle: AppCompatTextView,
    strTitle: Int,
    backRes: Int,
    statusBarColor: Int,
    setStatusIconDark: Boolean
) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        setHomeAsUpIndicator(backRes)
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowHomeEnabled(false)
        setDisplayShowTitleEnabled(false)
    }
    tvTitle.text = getString(strTitle)

}*/

fun AppCompatActivity.launch(intent: Intent) {
    intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
    startActivity(intent)

}

fun AppCompatActivity.launchWithFirstScreen(intent: Intent) {
    intent.flags =
        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    this.finish()
}


