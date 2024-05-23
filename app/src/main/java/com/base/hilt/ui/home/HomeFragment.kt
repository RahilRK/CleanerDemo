package com.base.hilt.ui.home

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color.blue
import android.graphics.Color.green
import android.graphics.Color.red
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.bind.GenericRecyclerViewAdapter
import com.base.hilt.databinding.FragmentHomeBinding
import com.base.hilt.databinding.MatchingImageListItemBinding
import com.base.hilt.model.MatchingImageDataItem
import com.base.hilt.model.VideoDataItem
import com.base.hilt.utils.Constants
import com.base.hilt.utils.DebugLog
import com.base.hilt.utils.FileUtils.getVideoRealPathFromURI
import com.base.hilt.worker.FindDuplicateImageWorker
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.math.pow
import kotlin.system.measureTimeMillis


@AndroidEntryPoint
class HomeFragment : FragmentBase<HomeViewModel, FragmentHomeBinding>() {

    private val TAG = "HomeFragment"

    val mList: ArrayList<MatchingImageDataItem> = ArrayList()
    private val bitmapQuality = 25
    private val allImageScope = CoroutineScope(Dispatchers.IO)

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(
            ToolbarModel(
                isVisible = true,
                title = getString(R.string.title_home),
                isBottomNavVisible = true
            )
        )
    }

    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun initializeScreenVariables() {

        observeData()
//        getAllImages()
//        getAllScreenShotImages()
        getAllScreenRecording()

//        startWorker()
    }

    private fun observeData() {

        viewModel.imagesList.observe(this) { mImageList ->

//            DebugLog.d(TAG, "observeData: imagesList Total ${mImageList.size}")
            createArrayChunk(mImageList)
//            findDuplicateImages(mImageList)
        }

        viewModel.matchImagesList.observe(this) { mImageList ->

            DebugLog.d(TAG, "observeData: matchImagesList mImageList size ${mImageList.size}")
            if (mList.size == mImageList.size) {
                DebugLog.d(TAG, "observeData: matchImagesList Total: ${mImageList.size}")
                findDuplicateImages(mImageList)
            }
        }

        viewModel.duplicateImagesList.observe(this) { mDuplicateImagesList ->

            loadDuplicateImages(arrayList = mDuplicateImagesList as ArrayList<MatchingImageDataItem>)
        }

        viewModel.measureProcessTime.observe(this) { mTime ->

            dataBinding.txtViewTime.text = "Total images ${viewModel.imagesList.value?.size} scanned in $mTime sec"
        }

        viewModel.screenRecordingList.observe(this) { mList ->

            loadScreenRecordingVideos(arrayList = mList as ArrayList<VideoDataItem>)
        }
    }

    /*todo step #1*/
    private fun getAllImages() {

        var imgId = 0
        try {
            val allImagesJob = allImageScope.launch {

                val measureTime = measureTimeMillis {

                    try {
                        dataBinding.progressBar.visibility = View.VISIBLE
                        dataBinding.textView.visibility = View.VISIBLE

                        DebugLog.d(TAG, "Scanning starts...")
                        DebugLog.d(TAG, "getAllImages: allImagesJob starts...")

                        var matchingImageDataItem = MatchingImageDataItem()

                        val imageProjection = arrayOf(
                            MediaStore.Images.Media._ID,
                            MediaStore.Images.Media.DISPLAY_NAME,
                            MediaStore.Images.Media.SIZE,
                            MediaStore.Images.Media.DATE_TAKEN,
                        )

                        val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
//                        val selection = "${MediaColumns.RELATIVE_PATH} LIKE ?"
//                        val selectionArgs = arrayOf("Download/My images 1%")
//                        val selectionArgs = arrayOf("Download/My images 2%")
//                        val selectionArgs = arrayOf("Download/My images 3%")
//                        val selectionArgs = arrayOf("Download/Quick Share%")

                        val cursor = requireActivity().contentResolver?.query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            imageProjection,
//                            selection,
                            null,
//                            selectionArgs,
                            null,
                            imageSortOrder
                        )
                        ;

                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                ///// Put your idea here
                                val idColumn =
                                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//                                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
//                                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
//                                val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

                                DebugLog.d(TAG, "getAllImages Total images: ${cursor.count}")

                                while (cursor.moveToNext()) {

                                    val id = cursor.getLong(idColumn)
//                                    val name = it.getString(nameColumn)
//                                    val size = it.getString(sizeColumn)
//                                    val dateTaken = it.getString(dateTakenColumn)

                                    imgId++
//                                    DebugLog.d(TAG, "imgId: $imgId")
                                    val contentUri = ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )

                                    //breaker
//                                    val hashValue = calculateMD5(requireActivity(), contentUri)
                                    matchingImageDataItem = MatchingImageDataItem(
                                        imgId = imgId,
//                                        imageBitmap = mBitmap,
                                        matchImageUri = contentUri,
//                                            matchImageName = getRealPath ?: "",
//                                        hashValue = hashValue!!
                                    )

                                    mList.add(matchingImageDataItem)

//                                    val getRealPath = getRealPathFromURI(requireActivity(), contentUri)
//                                    val bitmap: Bitmap? = uriToBitmap(requireActivity(), contentUri)
                                    /*
                                                                        bitmap?.let { mBitmap ->

                                                                            val hashValue = sampleHashFile(mBitmap)

                                                                            matchingImageDataItem = MatchingImageDataItem(
                                                                                imageBitmap = mBitmap,
                                                                                matchImageUri = contentUri,
                                    //                                            matchImageName = getRealPath ?: "",
                                                                                hashValue = hashValue
                                                                            )

                                                                            mList.add(matchingImageDataItem)
                                                                            DebugLog.d(TAG, "getAllImages matchingImageDataItem: $matchingImageDataItem")

                                                                        } ?: kotlin.run {

                                    //                                Toast.makeText(activity, "Bitmap is null!", Toast.LENGTH_SHORT).show()
                                                                            DebugLog.e(TAG, "getAllImages: Bitmap is null!")
                                                                        }
                                    */

//                                getMetaData(contentUri, matchingImageDataItem)
                                }
                            }
                            cursor.close()
                        }

                    } catch (e: Exception) {

                        val error = Log.getStackTraceString(e)
                        DebugLog.d(TAG, "getAllImages Exception: $error")
//                Toast.makeText(activity, "Error in getting getAllImages", Toast.LENGTH_SHORT).show()
                    }
                }

                DebugLog.d(TAG, "getAllImages: measureTime: $measureTime")
                val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(measureTime)
                dataBinding.txtViewTime.text = "Total images $imgId scanned in $seconds sec"
            }

            allImagesJob.invokeOnCompletion {

                viewModel.addImagesToList(mList)
                DebugLog.d(TAG, "getAllImages: allImagesJob Done...")
                allImageScope.launch(Dispatchers.Main) {
                    dataBinding.progressBar.visibility = View.GONE
                    dataBinding.textView.visibility = View.GONE
                    dataBinding.txtViewTime.visibility = View.VISIBLE
                }
            }
        } catch (e: Exception) {

            val error = Log.getStackTraceString(e)
            DebugLog.d(TAG, "getAllImages job error: $error")
        } catch (e: TimeoutException) {

            val error = Log.getStackTraceString(e)
            DebugLog.d(TAG, "getAllImages job TimeoutException: $error")
        }
    }

    /*todo step #2*/
    val mMyList: ArrayList<MatchingImageDataItem> = ArrayList()
    private fun createArrayChunk(mImageList: MutableList<MatchingImageDataItem>) {

        /*
                try {
                    var totalJobCount = 0
                    val mainJob = allImageScope.launch {

                        val measureTime = measureTimeMillis {

                        }

                        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(measureTime)
                        DebugLog.d(TAG, "createArrayChunk main job sec: $seconds")
                    }
                    mainJob.invokeOnCompletion {

                        viewModel.addMatchingImagesToList(mMyList)
                        DebugLog.d(TAG, "createArrayChunk mainJob completed...")
                    }
                }
                catch (e: Exception) {

                    val error = Log.getStackTraceString(e)
                    DebugLog.e(TAG, "createArrayChunk mainJob Exception: $error")
                }
        */

        mImageList.chunked(500) { newList ->

            try {
                DebugLog.d(TAG, "createArrayChunk job created...")
                val chunkArrayList = ArrayList<MatchingImageDataItem>(newList) //500
                val chunkJob = allImageScope.launch {

                    val measureTime = measureTimeMillis {

                        chunkArrayList.map { model ->

                            val hashValue = calculateMD5(requireActivity(), model.matchImageUri)
                            hashValue?.let {
                                model.hashValue = it
                            }
                        }
                    }

                    val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(measureTime)
                    viewModel.updateMeasureTime(mTime = seconds)
                    DebugLog.d(TAG, "createArrayChunk job sec: $seconds")
                }
                chunkJob.invokeOnCompletion {

                    mMyList.addAll(chunkArrayList)
                    viewModel.addMatchingImagesToList(mMyList)
                    DebugLog.d(
                        TAG,
                        "createArrayChunk job completed ${chunkArrayList[0].imgId}...${chunkArrayList[chunkArrayList.size - 1].imgId}"
                    )
                }
            } catch (e: Exception) {

                val error = Log.getStackTraceString(e)
                DebugLog.e(TAG, "createArrayChunk chunkJob Exception: $error")
            }

            /*DebugLog.d(TAG, "createJob_1: group1: ${newList.size}")

            if (newList[0].imgId == 1) {
            } else if (newList[0].imgId == 501) {

                DebugLog.d(TAG, "createJob2")
                try {
                    val arrayList = ArrayList<MatchingImageDataItem>(newList)
                    val job2 = allImageScope.launch {

                        val measureTime = measureTimeMillis {

                            arrayList.forEach { model ->

                                val hashValue = calculateMD5(requireActivity(), model.matchImageUri)
                                hashValue?.let {
                                    model.hashValue = it
                                }
                            }
                        }

                        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(measureTime)
                        DebugLog.d(TAG, "job2 seconds: $seconds")
                    }
                    job2.invokeOnCompletion {

                        mMyList.addAll(arrayList)
                        viewModel.addMatchingImagesToList(mMyList)
                        DebugLog.d(TAG, "Job2 completed...")
                    }
                } catch (e: Exception) {

                    val error = Log.getStackTraceString(e)
                    DebugLog.e(TAG, error)
                }

            } else if (newList[0].imgId == 1001) {

                DebugLog.d(TAG, "createJob3")
                try {
                    val arrayList = ArrayList<MatchingImageDataItem>(newList)
                    val job3 = allImageScope.launch {

                        val measureTime = measureTimeMillis {

                            arrayList.forEach { model ->

                                val hashValue = calculateMD5(requireActivity(), model.matchImageUri)
                                hashValue?.let {
                                    model.hashValue = it
                                }
                            }
                        }

                        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(measureTime)
                        DebugLog.d(TAG, "job3 seconds: $seconds")
                    }
                    job3.invokeOnCompletion {

                        mMyList.addAll(arrayList)
                        viewModel.addMatchingImagesToList(mMyList)
                        DebugLog.d(TAG, "Job3 completed...")
                    }
                } catch (e: Exception) {

                    val error = Log.getStackTraceString(e)
                    DebugLog.e(TAG, error)
                }

            } else if (newList[0].imgId == 1501) {

                DebugLog.d(TAG, "createJob4")
                try {
                    val arrayList = ArrayList<MatchingImageDataItem>(newList)
                    val job4 = allImageScope.launch {

                        val measureTime = measureTimeMillis {

                            arrayList.forEach { model ->

                                val hashValue = calculateMD5(requireActivity(), model.matchImageUri)
                                hashValue?.let {
                                    model.hashValue = it
                                }
                            }
                        }

                        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(measureTime)
                        DebugLog.d(TAG, "job4 seconds: $seconds")
                    }
                    job4.invokeOnCompletion {

                        mMyList.addAll(arrayList)
                        viewModel.addMatchingImagesToList(mMyList)
                        DebugLog.d(TAG, "Job4 completed...")
                    }
                } catch (e: Exception) {

                    val error = Log.getStackTraceString(e)
                    DebugLog.e(TAG, error)
                }

            } else {

                DebugLog.d(TAG, "createJob5")
                try {
                    val arrayList = ArrayList<MatchingImageDataItem>(newList)
                    val job5 = allImageScope.launch {

                        val measureTime = measureTimeMillis {

                            arrayList.forEach { model ->

                                val hashValue = calculateMD5(requireActivity(), model.matchImageUri)
                                hashValue?.let {
                                    model.hashValue = it
                                }
                            }
                        }

                        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(measureTime)
                        DebugLog.d(TAG, "job5 seconds: $seconds")
                    }
                    job5.invokeOnCompletion {

                        mMyList.addAll(arrayList)
                        viewModel.addMatchingImagesToList(mMyList)
                        DebugLog.d(TAG, "Job5 completed...")
                    }
                } catch (e: Exception) {

                    val error = Log.getStackTraceString(e)
                    DebugLog.e(TAG, error)
                }

            }*/
        }
    }

    /*todo step #3*/
    private fun calculateMD5(context: Context, fileUri: Uri?): String? {
        val digest: MessageDigest = try {
//            MessageDigest.getInstance("MD5")
//            MessageDigest.getInstance("SHA-256")
            MessageDigest.getInstance("SHA-1")
        } catch (e: NoSuchAlgorithmException) {
            DebugLog.e(TAG, "Exception while getting digest: $e")
            return null
        }
        val `is`: InputStream? = try {
            context.contentResolver.openInputStream(fileUri!!)
            // is = new FileInputStream("some_file_location");
        } catch (e: FileNotFoundException) {
            DebugLog.e(TAG, "Exception while getting FileInputStream: $e")
            return null
        }
//        val buffer = ByteArray(8192)
//        val buffer = ByteArray(20480)
        val buffer = ByteArray(1024 * 1024)
        var read: Int
        return try {
            while (`is`!!.read(buffer).also { read = it } > 0) {
                digest.update(buffer, 0, read)
            }
            val md5sum = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in md5sum) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            hexString.toString()
        } catch (e: IOException) {
            throw java.lang.RuntimeException("Unable to process file for MD5", e)
        } finally {
            try {
                `is`?.close()
            } catch (e: IOException) {
                DebugLog.e(TAG, "Exception on closing MD5 input stream: $e")
            }
        }
    }

    /*todo step #4*/
    private fun findDuplicateImages(mImageList: MutableList<MatchingImageDataItem> = arrayListOf()) {

        if (mImageList.isNotEmpty()) {
            // Map to store the frequency of each "mTAG_IMAGE_UNIQUE_ID"
            val frequencyMap: MutableMap<String, Int> = HashMap()
            val duplicateNames: ArrayList<String> = ArrayList()

            for (model in mImageList) {

                val imageId: String = model.hashValue
                if (frequencyMap.containsKey(imageId)) {
                    // If the name is already in the map, it's a duplicate
                    if (!duplicateNames.contains(imageId)) {
                        duplicateNames.add(imageId)
                    }
                } else {
                    frequencyMap[imageId] = 1
                }
            }

            // Printing duplicate names
            // Printing duplicate names
            for (name in duplicateNames) {
//            DebugLog.d(TAG, "$name is duplicated")
            }

            DebugLog.d(TAG, "findDuplicateImages duplicateList: ${duplicateNames.size}")
            keepOnlyDuplicateImages(mImageList, duplicateNames)
        }
    }

    /*todo step #5*/
    private fun keepOnlyDuplicateImages(
        mImageList: MutableList<MatchingImageDataItem> = arrayListOf(),
        duplicateImageHash: ArrayList<String>
    ) {
        if (mImageList.isNotEmpty() && duplicateImageHash.isNotEmpty()) {

            val finalList = ArrayList<MatchingImageDataItem>()

            for (model in mImageList) {

                val hash = model.hashValue

                for (j in duplicateImageHash) {

                    if (j == hash) {
                        finalList.add(model)
                    }
                }
            }

            val sortedList = finalList.sortedWith(compareBy { it.hashValue })
            viewModel.addDuplicateImagesToList(sortedList.toMutableList())
            DebugLog.d(TAG, "keepOnlyDuplicateImages finalList: ${sortedList.size}")
            DebugLog.d(TAG, "Scanning Done...")
        } else {
            Toast.makeText(activity, "No duplicate images found", Toast.LENGTH_SHORT).show()
        }
    }

    /*todo recyclerview adapter*/
    private fun loadDuplicateImages(arrayList: ArrayList<MatchingImageDataItem>) {

        val mAdapter = object :
            GenericRecyclerViewAdapter<MatchingImageDataItem, MatchingImageListItemBinding>(
                requireContext(),
                arrayList
            ) {
            override val layoutResId: Int
                get() = R.layout.matching_image_list_item

            override fun onBindData(
                model: MatchingImageDataItem,
                position: Int,
                dataBinding: MatchingImageListItemBinding
            ) {
//                dataBinding.model = model

                Glide.with(requireActivity())
                    .load(model.matchImageUri)
                    .transform(CenterCrop(), RoundedCorners(8))
                    .into(dataBinding.imageViewMatch)

//                dataBinding.textViewMatch.text = "index: $position, diff: ${model.imageDiffPerc.toInt()}"
                dataBinding.textViewMatch.text = model.hashValue

                /* if (model.imageDiffPerc in 0.0..2.0) {
                     dataBinding.textViewMatch.setTextColor(context.resources.getColor(R.color.red))
                     dataBinding.textViewMatch.setTypeface(null, Typeface.BOLD)
                 } else {
                     dataBinding.textViewMatch.setTextColor(context.resources.getColor(R.color.black))
                 }*/
                dataBinding.executePendingBindings()
            }

            override fun onItemClick(model: MatchingImageDataItem, position: Int) {

            }
        }

        dataBinding.rvList.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun getAllScreenShotImages() {

        var imageNumber = 1
        try {
            val allImagesJob = allImageScope.launch {

                val measureTime = measureTimeMillis {

                    try {
                        dataBinding.progressBar.visibility = View.VISIBLE
                        dataBinding.textView.visibility = View.VISIBLE

                        DebugLog.d(TAG, "Scanning starts...")
                        DebugLog.d(TAG, "getAllImages: allImagesJob starts...")

                        var matchingImageDataItem = MatchingImageDataItem()

                        val imageProjection = arrayOf(
                            MediaStore.Images.Media._ID,
                            MediaStore.Images.Media.DISPLAY_NAME,
                            MediaStore.Images.Media.SIZE,
                            MediaStore.Images.Media.DATE_TAKEN,
                        )

                        val selection =
                            "${MediaColumns.RELATIVE_PATH} LIKE ? OR ${MediaColumns.RELATIVE_PATH} LIKE ?"
                        val selectionArgs = arrayOf(
                            "Pictures/Screenshots%",
                            "DCIM/Screenshots%"
                        )

                        val cursor = requireActivity().contentResolver?.query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            imageProjection,
                            selection,
                            selectionArgs,
                            null
                        )

                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                ///// Put your idea here
                                val idColumn =
                                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//                                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
//                                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
//                                val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

                                DebugLog.d(TAG, "getAllImages Total images: ${cursor.count}")

                                while (cursor.moveToNext()) {

                                    val id = cursor.getLong(idColumn)
//                                    val name = it.getString(nameColumn)
//                                    val size = it.getString(sizeColumn)
//                                    val dateTaken = it.getString(dateTakenColumn)

                                    imageNumber++
                                    DebugLog.d(TAG, "imageNumber: $imageNumber")
                                    val contentUri = ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )
//                                    val hashValue = calculateMD5(requireActivity(), contentUri)
                                    matchingImageDataItem = MatchingImageDataItem(
//                                        imageBitmap = mBitmap,
                                        matchImageUri = contentUri,
//                                            matchImageName = getRealPath ?: "",
//                                        hashValue = hashValue!!
                                    )

                                    mList.add(matchingImageDataItem)

//                                    val getRealPath = getRealPathFromURI(requireActivity(), contentUri)
//                                    val bitmap: Bitmap? = uriToBitmap(requireActivity(), contentUri)
                                    /*
                                                                        bitmap?.let { mBitmap ->

                                                                            val hashValue = sampleHashFile(mBitmap)

                                                                            matchingImageDataItem = MatchingImageDataItem(
                                                                                imageBitmap = mBitmap,
                                                                                matchImageUri = contentUri,
                                    //                                            matchImageName = getRealPath ?: "",
                                                                                hashValue = hashValue
                                                                            )

                                                                            mList.add(matchingImageDataItem)
                                                                            DebugLog.d(TAG, "getAllImages matchingImageDataItem: $matchingImageDataItem")

                                                                        } ?: kotlin.run {

                                    //                                Toast.makeText(activity, "Bitmap is null!", Toast.LENGTH_SHORT).show()
                                                                            DebugLog.e(TAG, "getAllImages: Bitmap is null!")
                                                                        }
                                    */

//                                getMetaData(contentUri, matchingImageDataItem)
                                }
                            }
                            cursor.close()
                        }

                    } catch (e: Exception) {

                        val error = Log.getStackTraceString(e)
                        DebugLog.d(TAG, "getAllImages error: $error")
//                Toast.makeText(activity, "Error in getting getAllImages", Toast.LENGTH_SHORT).show()
                    }
                }

                DebugLog.d(TAG, "getAllImages: measureTime: $measureTime")
                val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(measureTime)
                dataBinding.txtViewTime.text = "Total images $imageNumber scanned in $seconds sec"
            }

            allImagesJob.invokeOnCompletion {

                viewModel.addImagesToList(mList)
                DebugLog.d(TAG, "getAllImages: allImagesJob Done...")
                allImageScope.launch(Dispatchers.Main) {
                    dataBinding.progressBar.visibility = View.GONE
                    dataBinding.textView.visibility = View.GONE
                    dataBinding.txtViewTime.visibility = View.VISIBLE
                }
            }
        } catch (e: Exception) {

            val error = Log.getStackTraceString(e)
            DebugLog.d(TAG, "getAllImages job error: $error")
        } catch (e: TimeoutException) {

            val error = Log.getStackTraceString(e)
            DebugLog.d(TAG, "getAllImages job TimeoutException: $error")
        }
    }

    private fun getAllScreenRecording() {

        try {
            val allImagesJob = allImageScope.launch {

                val measureTime = measureTimeMillis {

                    try {
                        var videoDataItem: VideoDataItem
                        dataBinding.progressBar.visibility = View.VISIBLE
                        dataBinding.textView.visibility = View.VISIBLE

                        DebugLog.d(TAG, "Scanning starts...")
                        DebugLog.d(TAG, "getAllScreenRecording: Job starts...")

                        val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                        } else {
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }

                        val projection = arrayOf(
                            MediaStore.Video.Media._ID,
                            MediaStore.Video.Media.TITLE,
                            MediaStore.Video.Media.DATA,
                            MediaStore.Video.VideoColumns.DATE_ADDED,
                            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                            MediaStore.Video.Media.DURATION,
                            MediaStore.Video.Media.SIZE
                        )

                        val selection =
                            "${MediaStore.Video.Media.DISPLAY_NAME} LIKE ? OR ${MediaStore.Video.Media.DISPLAY_NAME} LIKE ? OR ${MediaStore.Video.Media.DISPLAY_NAME} LIKE ?"

                        val selectionArgs = arrayOf(
                            "%screen%",
                            "%screencast%",
                            "%record%")

                        val orderBy = "${MediaStore.Video.Media.DATE_ADDED} DESC"

                        val cursor = requireActivity().contentResolver?.query(
                            uri,
                            null,
                            selection,
//                            null,
                            selectionArgs,
//                            null,
                            orderBy
                        )

                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                ///// Put your idea here
                                val _ID = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                                val TITLE =
                                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
                                val DATA = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                                val BUCKET_DISPLAY_NAME =
                                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
                                val DATE_ADDED =
                                    cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATE_ADDED)
                                val DURATION =
                                    cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION)
                                val SIZE =
                                    cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.SIZE)
                                val DISPLAY_NAME =
                                    cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DISPLAY_NAME)

                                DebugLog.d(
                                    TAG,
                                    "getAllScreenRecording Total screen recording: ${cursor.count}"
                                )

                                while (cursor.moveToNext()) {

                                    val id = cursor.getLong(_ID)
                                    val TITLE = cursor.getString(TITLE)
                                    val DATA = Uri.parse(cursor.getString(DATA))
                                    val BUCKET_DISPLAY_NAME = cursor.getString(BUCKET_DISPLAY_NAME)
                                    val DATE_ADDED = cursor.getString(DATE_ADDED)
                                    val DURATION = cursor.getString(DURATION)
                                    val SIZE = cursor.getString(SIZE)
                                    val DISPLAY_NAME = cursor.getString(DISPLAY_NAME)

                                    val contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                                    val getRealPath = getVideoRealPathFromURI(requireActivity(), contentUri)
                                    videoDataItem = VideoDataItem(
                                        _ID = _ID,
                                        TITLE = TITLE,
                                        DATA = DATA,
                                        BUCKET_DISPLAY_NAME = BUCKET_DISPLAY_NAME,
                                        DISPLAY_NAME = DISPLAY_NAME,
                                        videoPath = getRealPath,
                                        contentUri = contentUri,
                                        DURATION = timeConversion(DURATION.toLong()),
                                    )

                                    viewModel.addScreenRecordingToList(videoDataItem)
//                                    getVideoMetaData(contentUri = contentUri, videoDataItem)
                                    DebugLog.d(TAG, "getAllScreenRecording videoDataItem: $videoDataItem")
                                }
                            }
                            cursor.close()
                        }

                    } catch (e: Exception) {

                        val error = Log.getStackTraceString(e)
                        DebugLog.d(TAG, "getAllScreenRecording Exception: $error")
//                Toast.makeText(activity, "Error in getting getAllImages", Toast.LENGTH_SHORT).show()
                    }
                }

                DebugLog.d(TAG, "getAllScreenRecording: measureTime: $measureTime")
                val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(measureTime)
                dataBinding.txtViewTime.text = "Scanned in $seconds sec"
            }

            allImagesJob.invokeOnCompletion {

                DebugLog.d(TAG, "getAllScreenRecording: Job Done...")
                allImageScope.launch(Dispatchers.Main) {
                    dataBinding.progressBar.visibility = View.GONE
                    dataBinding.textView.visibility = View.GONE
                    dataBinding.txtViewTime.visibility = View.VISIBLE
                }
            }
        } catch (e: Exception) {

            val error = Log.getStackTraceString(e)
            DebugLog.d(TAG, "getAllImages job Exception: $error")
        } catch (e: TimeoutException) {

            val error = Log.getStackTraceString(e)
            DebugLog.d(TAG, "getAllImages job TimeoutException: $error")
        }
    }

    @Throws(Throwable::class)
    fun getVideoMetaData(contentUri: Uri, videoDataItem: VideoDataItem) {

        // Create a MediaMetadataRetriever
        val retriever = MediaMetadataRetriever()

        try {
            // Set the data source to the video file
            retriever.setDataSource(requireActivity(), contentUri)

            // Retrieve metadata
            val rotation: String? =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
            val duration: String? =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val frameRate: String? =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE)
            val width: String? =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val height: String? =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)

            // Convert rotation to integer
            val rotationValue: Int = rotation?.toInt() ?: 0

            // Convert width and height to integer
            val widthValue: Int = width?.toInt() ?: 0
            val heightValue: Int = height?.toInt() ?: 0

            // Convert duration to milliseconds
            val durationMillis: Long = duration?.toLong() ?: 0

//            val mFrameRate: Int = frameRate?.toInt() ?: 0

            // Log retrieved metadata
            /*Log.d("Video Metadata", "Rotation: $rotationValue")
            Log.d("Video Metadata", "Duration (ms): $durationMillis")
            Log.d("Video Metadata", "Frame Rate: $frameRate")
            Log.d("Video Metadata", "Width: $widthValue")
            Log.d("Video Metadata", "Height: $heightValue")*/

            val model = videoDataItem.copy(

                METADATA_KEY_VIDEO_ROTATION = rotationValue,
                METADATA_KEY_VIDEO_WIDTH = widthValue,
                METADATA_KEY_VIDEO_HEIGHT = heightValue,
//                METADATA_KEY_CAPTURE_FRAMERATE = mFrameRate,
            )
            DebugLog.d(TAG, "getAllScreenRecording videoDataItem: $videoDataItem")
            viewModel.addScreenRecordingToList(model)

            /*if(model.METADATA_KEY_VIDEO_HEIGHT > model.METADATA_KEY_VIDEO_WIDTH) {
            }*/

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Release the MediaMetadataRetriever
            retriever.release()
        }
    }

    private fun loadScreenRecordingVideos(arrayList: ArrayList<VideoDataItem>) {

        val mAdapter = object :
            GenericRecyclerViewAdapter<VideoDataItem, MatchingImageListItemBinding>(
                requireContext(),
                arrayList
            ) {
            override val layoutResId: Int
                get() = R.layout.matching_image_list_item

            override fun onBindData(
                model: VideoDataItem,
                position: Int,
                dataBinding: MatchingImageListItemBinding
            ) {
//                dataBinding.model = model

                Glide.with(requireActivity())
                    .load(model.contentUri)
                    .transform(CenterCrop(), RoundedCorners(8))
                    .into(dataBinding.imageViewMatch)

//                dataBinding.textViewMatch.text = "index: $position, diff: ${model.imageDiffPerc.toInt()}"
                dataBinding.textViewMatch.text = model.videoPath

                dataBinding.executePendingBindings()
            }

            override fun onItemClick(model: VideoDataItem, position: Int) {

            }
        }

        dataBinding.rvList.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    //time conversion
    private fun timeConversion(value: Long): String {
        val videoTime: String
        val dur = value.toInt()
        val hrs = dur / 3600000
        val mns = dur / 60000 % 60000
        val scs = dur % 60000 / 1000
        videoTime = if (hrs > 0) {
            String.format("%02d:%02d:%02d", hrs, mns, scs)
        } else {
            String.format("%02d:%02d", mns, scs)
        }
        return videoTime
    }

    /*todo functions not useful*/
    private fun startWorker() {

        //for constraints
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //Input and output
        val data: Data = Data.Builder()
            .putInt(Constants.KEY_COUNT_VALUE, 10)
            .build()

        val uploadRequest = OneTimeWorkRequest.Builder(FindDuplicateImageWorker::class.java)
            //set constraints
            //.setConstraints(constraints)
            //Input and output
            .setInputData(data)
            .build()

        val workManager = WorkManager.getInstance(requireActivity())
        workManager.enqueue(uploadRequest)

        //get Status of work manager
        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, Observer {
                // here we got the status of work manager

//                DebugLog.d(TAG, "state name: ${it.state.name}")
                //2.constraints are used for work space work on different condition

                if (it.state.isFinished) {
                    val data = it.outputData
                    val message = data.getString(Constants.KEY_WORKER)
//                    Toast.makeText(requireActivity(),message.toString(),Toast.LENGTH_LONG).show()
                    DebugLog.d(TAG, "message: $message images")
                }
            })
    }

    private fun getImageMetaData(
        uri: Uri,
        matchingImageDataItem: MatchingImageDataItem = MatchingImageDataItem(),
    ) {
        try {
            requireActivity().contentResolver.openInputStream(uri).use { inputStream ->
                inputStream?.let { mInputStream ->
                    val exif = ExifInterface(mInputStream)

                    val mTAG_IMAGE_UNIQUE_ID =
                        exif.getAttribute(ExifInterface.TAG_IMAGE_UNIQUE_ID) ?: ""

                    val mTAG_GPS_LATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) ?: ""
                    val mTAG_GPS_LONGITUDE =
                        exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) ?: ""

//                    val mTAG_GPS_LATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
//                    val mTAG_GPS_LONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)

//                    val mTAG_GPS_DEST_LATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_DEST_LATITUDE)
//                    val mTAG_GPS_DEST_LONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_DEST_LONGITUDE)

//                    val mTAG_GPS_DEST_LATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_DEST_LATITUDE_REF)
//                    val mTAG_GPS_DEST_LONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_DEST_LONGITUDE_REF)

//                    val mTAG_DATETIME = exif.getAttribute(ExifInterface.TAG_DATETIME)
//                    val mTAG_GPS_DATESTAMP = exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP)
//                    val mTAG_GPS_TIMESTAMP = exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP)
                    val mTAG_DATETIME_ORIGINAL =
                        exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL) ?: ""
//                    val mTAG_DATETIME_DIGITIZED = exif.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)

                    val mTAG_IMAGE_HEIGHT = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) ?: ""
                    val mTAG_IMAGE_WIDTH = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) ?: ""

                    val mTAG_SHUTTER_SPEED_VALUE =
                        exif.getAttribute(ExifInterface.TAG_SHUTTER_SPEED_VALUE) ?: ""
                    val mTAG_APERTURE_VALUE =
                        exif.getAttribute(ExifInterface.TAG_APERTURE_VALUE) ?: ""
                    val mTAG_BRIGHTNESS_VALUE =
                        exif.getAttribute(ExifInterface.TAG_BRIGHTNESS_VALUE) ?: ""

                    /*
                                        DebugLog.d(
                                            TAG, "Metadata details:\n" +
                                                    "mTAG_IMAGE_UNIQUE_ID: $mTAG_IMAGE_UNIQUE_ID\n" +
                        //                                "mTAG_GPS_DATESTAMP: $mTAG_GPS_DATESTAMP\n" +
                        //                                "mTAG_DATETIME: $mTAG_DATETIME\n" +
                                                    "mTAG_GPS_LATITUDE - $mTAG_GPS_LATITUDE\n" +
                                                    "mTAG_GPS_LONGITUDE - $mTAG_GPS_LONGITUDE\n" +
                        //                                "mTAG_GPS_LATITUDE_REF - $mTAG_GPS_LATITUDE_REF\n" +
                        //                                "mTAG_GPS_LONGITUDE_REF - $mTAG_GPS_LONGITUDE_REF\n" +
                        //                                "mTAG_GPS_DEST_LATITUDE - $mTAG_GPS_DEST_LATITUDE\n" +
                        //                                "mTAG_GPS_DEST_LONGITUDE - $mTAG_GPS_DEST_LONGITUDE\n" +
                        //                                "mTAG_GPS_DEST_LATITUDE_REF - $mTAG_GPS_DEST_LATITUDE_REF\n" +
                        //                                "mTAG_GPS_DEST_LONGITUDE_REF - $mTAG_GPS_DEST_LONGITUDE_REF\n" +
                        //                                "mTAG_GPS_TIMESTAMP - $mTAG_GPS_TIMESTAMP\n" +
                                                    "mTAG_DATETIME_ORIGINAL - $mTAG_DATETIME_ORIGINAL\n" +
                        //                                "mTAG_DATETIME_DIGITIZED - $mTAG_DATETIME_DIGITIZED\n" +
                                                    "mTAG_IMAGE_HEIGHT - $mTAG_IMAGE_HEIGHT\n" +
                                                    "mTAG_IMAGE_WIDTH - $mTAG_IMAGE_WIDTH\n" +
                                                    "mTAG_SHUTTER_SPEED_VALUE - $mTAG_SHUTTER_SPEED_VALUE\n" +
                                                    "mTAG_APERTURE_VALUE - $mTAG_APERTURE_VALUE\n" +
                                                    "mTAG_BRIGHTNESS_VALUE - $mTAG_BRIGHTNESS_VALUE\n"
                                        )
                    */

                    val matchingImageDataItem = matchingImageDataItem.copy(
                        mTAG_IMAGE_UNIQUE_ID = mTAG_IMAGE_UNIQUE_ID,
                        mTAG_GPS_LATITUDE = mTAG_GPS_LATITUDE,
                        mTAG_GPS_LONGITUDE = mTAG_GPS_LONGITUDE,
                        mTAG_DATETIME_ORIGINAL = mTAG_DATETIME_ORIGINAL,
                        mTAG_IMAGE_HEIGHT = mTAG_IMAGE_HEIGHT,
                        mTAG_IMAGE_WIDTH = mTAG_IMAGE_WIDTH,
                        mTAG_SHUTTER_SPEED_VALUE = mTAG_SHUTTER_SPEED_VALUE,
                        mTAG_APERTURE_VALUE = mTAG_APERTURE_VALUE,
                        mTAG_BRIGHTNESS_VALUE = mTAG_BRIGHTNESS_VALUE
                    )

                    mList.add(matchingImageDataItem)
//                    viewModel.addImagesToList(matchingImageDataItem)
                    DebugLog.d(TAG, "getMetaData matchingImageDataItem: $matchingImageDataItem")
                }
            }
        } catch (e: Exception) {

            val error = Log.getStackTraceString(e)
            DebugLog.d(TAG, "getMetaData error: $error")
//                Toast.makeText(activity, "Error in getting getMetaData", Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    private fun sampleHashFile(bitmap: Bitmap): String {

        return try {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, bitmapQuality, baos)
            val bb = baos.toByteArray()

            val bc = MessageDigest.getInstance("SHA-256").digest(bb)
            BigInteger(1, bc).toString(16)
        } catch (e: Exception) {

            val error = Log.getStackTraceString(e)
            DebugLog.d(TAG, "sampleHashFile error: $error")
            ""
        }
    }

    /*todo #1st way*/
    private fun compareImage1(image1: Bitmap, image2: Bitmap): Double {

        val width1: Int = image1.width
        val height1: Int = image1.height
        val width2: Int = image2.width
        val height2: Int = image2.height

        if (width1 == width2 || height1 == height2) {

            val maxValue = 255

            val width = image1.width
            val height = image1.height
            val totalPixels = width * height

            val pixels1 = IntArray(totalPixels)
            val pixels2 = IntArray(totalPixels)

            image1.getPixels(pixels1, 0, width, 0, 0, width, height)
            image2.getPixels(pixels2, 0, width, 0, 0, width, height)

            var sumSquaredDiff = 0.0

            for (i in 0 until totalPixels) {
                val pixel1 = pixels1[i]
                val pixel2 = pixels2[i]

                val red1 = red(pixel1)
                val green1 = green(pixel1)
                val blue1 = blue(pixel1)

                val red2 = red(pixel2)
                val green2 = green(pixel2)
                val blue2 = blue(pixel2)

                val diffRed = (red1 - red2).toDouble().pow(2.0)
                val diffGreen = (green1 - green2).toDouble().pow(2.0)
                val diffBlue = (blue1 - blue2).toDouble().pow(2.0)

                sumSquaredDiff += (diffRed + diffGreen + diffBlue) / 3
            }

            val mse = sumSquaredDiff / totalPixels
            val percentage = (mse / (maxValue * maxValue)) * 100
            return percentage
        }

        return -0.1
    }

    /*todo #2nd way*/
    private fun compareImage2(bmp1: Bitmap, bmp2: Bitmap): Double {

        // Assigning dimensions to image
        val width1: Int = bmp1.width
        val height1: Int = bmp1.height
        val width2: Int = bmp2.width
        val height2: Int = bmp2.height

        // Checking whether the images are of same size or not
        if (width1 != width2 || height1 != height2) {

            return -0.1
        } else {

            // By now, images are of same size
            var difference: Long = 0

            // treating images likely 2D matrix
            var y = 0
            while (y < height1) {
                // original y++
                var x = 0
                while (x < width1) {
                    // original x++
                    val rgbA: Int = bmp1.getPixel(x, y)
                    val rgbB: Int = bmp2.getPixel(x, y)
                    val redA = rgbA shr 16 and 0xff
                    val greenA = rgbA shr 8 and 0xff
                    val blueA = rgbA and 0xff
                    val redB = rgbB shr 16 and 0xff
                    val greenB = rgbB shr 8 and 0xff
                    val blueB = rgbB and 0xff
                    difference += Math.abs(redA - redB).toLong()
                    difference += Math.abs(greenA - greenB).toLong()
                    difference += Math.abs(blueA - blueB).toLong()
                    x = x + 2
                }
                y = y + 2
            }

            // Total number of red pixels = width * height
            // Total number of blue pixels = width * height
            // Total number of green pixels = width * height
            // So total number of pixels = width * height *
            // 3
            val total_pixels = (width1 * height1 * 3).toDouble()

            // Normalizing the value of different pixels for accuracy
            // Note: Average pixels per color component
            val avg_different_pixels = difference / total_pixels
//            DebugLog.d(TAG, "avg_different_pixels: $avg_different_pixels")

            // There are 255 values of pixels in total
            return avg_different_pixels / 255 * 100
        }
    }
}