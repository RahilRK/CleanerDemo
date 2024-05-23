package com.base.hilt.model

import android.graphics.Bitmap
import android.net.Uri

data class VideoDataItem(
    var _ID: Int = 0,
    var TITLE: String = "",
    var DATA: Uri? = null,
    var BUCKET_DISPLAY_NAME: String = "",
    var DATE_ADDED: Double = 0.0,
    var DURATION: String = "",
    var DISPLAY_NAME: String = "",
    var videoPath: String = "",
    var contentUri: Uri? = null,
    var thumbnail: Bitmap? = null,

    /*todo meta-data details*/
    var METADATA_KEY_VIDEO_ROTATION: Int = 0,
    var METADATA_KEY_CAPTURE_FRAMERATE: Int = 0,
    var METADATA_KEY_VIDEO_WIDTH: Int = 0,
    var METADATA_KEY_VIDEO_HEIGHT: Int = 0,

    )