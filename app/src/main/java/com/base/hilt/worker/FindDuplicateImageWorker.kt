package com.base.hilt.worker

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.base.hilt.model.MatchingImageDataItem
import com.base.hilt.utils.Constants
import com.base.hilt.utils.DebugLog
import com.base.hilt.utils.FileUtils
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.security.MessageDigest

class FindDuplicateImageWorker(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {

    private val TAG = "FindDuplicateImageWorker"
    private val mContext = context

    private val bitmapQuality = 50
    private val failureMessage: String = ""

    override fun doWork(): Result {

        try {

//            val count:Int = inputData.getInt(Constants.KEY_COUNT_VALUE,0)

            val totalImages = getAllImages()

            val outPutDate = Data.Builder().putString(Constants.KEY_WORKER,totalImages.toString()).build()
            return Result.success(outPutDate)
        }
        catch (e: Exception) {

            val error = Log.getStackTraceString(e)
            DebugLog.e(TAG, "doWork: $error")
            val outPutDate = Data.Builder().putString(Constants.KEY_WORKER, failureMessage).build()
            return Result.failure(outPutDate)
        }
    }

    private fun getAllImages(): ArrayList<MatchingImageDataItem> {

        try {
            val mList: ArrayList<MatchingImageDataItem> = ArrayList()

            DebugLog.d(TAG, "Scanning starts...")
            DebugLog.d(TAG, "getAllImages: allImagesJob starts...")

            var matchingImageDataItem: MatchingImageDataItem

            val imageProjection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_TAKEN,
            )

            val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

            val cursor = mContext.contentResolver?.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageProjection,
                null,
                null,
                imageSortOrder
            )

            cursor.use {
                it?.let {
//                                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val idColumn = it.getColumnIndex(MediaStore.MediaColumns._ID)
//                                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
//                                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
//                                val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

//                DebugLog.d(TAG, "getAllImages Total images: ${it.count}")

                    while (it.moveToNext()) {
                        val id = it.getLong(idColumn)
//                                    val name = it.getString(nameColumn)
//                                    val size = it.getString(sizeColumn)
//                                    val dateTaken = it.getString(dateTakenColumn)

                        val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                        /*
                                            contentUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                MediaStore.setRequireOriginal(contentUri)
                                            } else {
                                                TODO("VERSION.SDK_INT < Q")
                                            }
                        */

                        val getRealPath = FileUtils.getRealPathFromURI(mContext, contentUri)
//                    DebugLog.d(TAG, "getAllImages getRealPath: $getRealPath")

                        // add the URI to the list
                        // generate the thumbnail
//                    val thumbnail = activity?.contentResolver.loadThumbnail(contentUri, Size(480, 480), null)

                        val bitmap: Bitmap? = FileUtils.uriToBitmap(mContext, contentUri)
                        bitmap?.let { mBitmap ->

                            /*mList.add(
                                MatchingImageDataItem(
                                    imageBitmap = mBitmap,
                                    matchImageUri = contentUri,
                                    matchImageName = getRealPath ?: ""
                                )
                            )*/

                            val hashValue = sampleHashFile(mBitmap)
//                        DebugLog.d(TAG, "hashValue: $hashValue")

                            matchingImageDataItem = MatchingImageDataItem(
                                imageBitmap = mBitmap,
                                matchImageUri = contentUri,
                                matchImageName = getRealPath ?: "",
                                hashValue = hashValue
                            )

                            mList.add(matchingImageDataItem)
                            DebugLog.d(
                                TAG,
                                "getAllImages matchingImageDataItem: $matchingImageDataItem"
                            )

                        } ?: kotlin.run {

//                                Toast.makeText(activity, "Bitmap is null!", Toast.LENGTH_SHORT).show()
                            DebugLog.e(TAG, "getAllImages: Bitmap is null!")
                        }

//                                getMetaData(contentUri, matchingImageDataItem)
                    }
                } ?: kotlin.run {

//                        Toast.makeText(activity, "Cursor is null!", Toast.LENGTH_SHORT).show()
                    DebugLog.e(TAG, "getAllImages: Cursor is null!")
                }
            }

            DebugLog.d(TAG, "mList: ${mList.size}")

            return mList

        } catch (e: Exception) {

            val error = Log.getStackTraceString(e)
            DebugLog.d(TAG, "getAllImages error: $error")
//                Toast.makeText(activity, "Error in getting getAllImages", Toast.LENGTH_SHORT).show()
            return arrayListOf()
        }
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
}