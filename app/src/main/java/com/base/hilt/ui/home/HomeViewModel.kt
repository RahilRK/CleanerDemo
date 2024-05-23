package com.base.hilt.ui.home

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.hilt.base.ViewModelBase
import com.base.hilt.model.MatchingImageDataItem
import com.base.hilt.model.VideoDataItem
import com.base.hilt.model.response.MovieListResponse
import com.base.hilt.network.ResponseData
import com.base.hilt.network.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) :

    ViewModelBase() {
/*
    The objective of either of these is to keep the mutability private,
    so consumers of this class do not accidentally update the MutableLiveData themselves.
*/


    private val _responseLiveHomeVendorListResponse =
    MutableLiveData<ResponseHandler<ResponseData<MovieListResponse>?>>()
    val responseLiveHomeVendorListResponse: LiveData<ResponseHandler<ResponseData<MovieListResponse>?>>
        get() = _responseLiveHomeVendorListResponse

    fun callHomeScreenAPI() {
        viewModelScope.launch {
            _responseLiveHomeVendorListResponse.postValue(ResponseHandler.Loading)
                _responseLiveHomeVendorListResponse.value = homeRepository.callHomeScreenAPI()
        }
    }

    /*todo all images meta-data*/
    private val _imagesList =
        MutableLiveData<MutableList<MatchingImageDataItem>>(arrayListOf())
    val imagesList: LiveData<MutableList<MatchingImageDataItem>>
        get() = _imagesList

    fun addImagesToList(mList: MutableList<MatchingImageDataItem> = arrayListOf()) {

        _imagesList.postValue(mList)
    }

    fun addImagesToList(model: MatchingImageDataItem = MatchingImageDataItem()) {

        _imagesList.value?.add(model)
//        _imagesList.postValue(_imagesList.value)
    }

    fun clearImageList() {
        _imagesList.postValue(arrayListOf())
    }

    /*todo match images*/
    private val _matchImagesList =
        MutableLiveData<MutableList<MatchingImageDataItem>>(arrayListOf())
    val matchImagesList: LiveData<MutableList<MatchingImageDataItem>>
        get() = _matchImagesList

    fun addMatchingImagesToList(mList: MutableList<MatchingImageDataItem> = arrayListOf()) {

        _matchImagesList.postValue(mList)
    }

    fun clearMatchingImageList() {
        _matchImagesList.postValue(arrayListOf())
    }

    /*todo match images*/
    private val _duplicateImagesList =
        MutableLiveData<MutableList<MatchingImageDataItem>>(arrayListOf())
    val duplicateImagesList: LiveData<MutableList<MatchingImageDataItem>>
        get() = _duplicateImagesList

    fun addDuplicateImagesToList(mList: MutableList<MatchingImageDataItem> = arrayListOf()) {

        _duplicateImagesList.postValue(mList)
    }

    fun clearDuplicateImageList() {
        _duplicateImagesList.postValue(arrayListOf())
    }

    /*todo _measureProcessTime*/
    private val _measureProcessTime =
        MutableLiveData<Long>(0)
    val measureProcessTime: LiveData<Long>
        get() = _measureProcessTime

    fun updateMeasureTime(mTime: Long = 0) {

        _measureProcessTime.postValue(mTime)
    }

    /*todo all screenshot*/
    private val _screenShotList =
        MutableLiveData<MutableList<MatchingImageDataItem>>(arrayListOf())
    val screenShotList: LiveData<MutableList<MatchingImageDataItem>>
        get() = _screenShotList

    fun addScreenShotToList(model: MatchingImageDataItem = MatchingImageDataItem()) {

        _screenShotList.value?.add(model)
//        _imagesList.postValue(_imagesList.value)
    }

    fun addScreenShotToList(mList: MutableList<MatchingImageDataItem> = arrayListOf()) {

        _screenShotList.postValue(mList)
    }

    /*todo all screen recording*/
    private val _screenRecordingList =
        MutableLiveData<MutableList<VideoDataItem>>(arrayListOf())
    val screenRecordingList: LiveData<MutableList<VideoDataItem>>
        get() = _screenRecordingList

    fun addScreenRecordingToList(model: VideoDataItem = VideoDataItem()) {

        _screenRecordingList.value?.add(model)
    }
}