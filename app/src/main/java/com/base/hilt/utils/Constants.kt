package com.base.hilt.utils

import androidx.datastore.preferences.core.stringPreferencesKey
import com.base.hilt.BuildConfig
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object Constants {
    val JSON = jacksonObjectMapper()
    val PREF_NAME = BuildConfig.APPLICATION_ID
    var KEY_BUNDLE = "bundle"

    var KEY_DATA = "itemData"
    var KEY_GRAPH_ID = "graphId"
    var KEY_START_DESTINATION = "startDestination"

    const val KEY_WORKER = "FindDuplicateImageWorker"
    const val KEY_COUNT_VALUE = "key_count"

    object DataStore {
        val DATA = stringPreferencesKey("data")
        val SECURED_DATA = stringPreferencesKey("secured_data")
    }
}