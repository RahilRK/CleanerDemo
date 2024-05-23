package com.base.hilt.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


open class ViewModelBase : ViewModel() {

    /*
    *    The objective of either of these is to keep the mutability private,
     *   so consumers of this class do not accidentally update the MutableLiveData themselves.
     */
    private val _snackBarMessage = MutableLiveData<Any>()
    val snackBarMessage: LiveData<Any>  get() = _snackBarMessage

    private val _progressBarDisplay =  MutableLiveData<Boolean>()
    val progressBarDisplay: LiveData<Boolean> get() = _progressBarDisplay

    private val _toolBarModel = MutableLiveData<ToolbarModel>()
    val toolBarModel : LiveData<ToolbarModel> get() = _toolBarModel

    private val _hideKeyBoardEvent = MutableLiveData<Any>()
    val hideKeyBoardEvent : LiveData<Any> get() = _hideKeyBoardEvent

    fun getHideKeyBoardEvent(): MutableLiveData<Any> {
        return _hideKeyBoardEvent
    }

    /**
     * This method is used to display the Snake Bar Message
     *
     * @param message string object to display.
     */
    fun showSnackBarMessage(message: Any) {
        _snackBarMessage.postValue(message)
    }

    fun showProgressBar(display: Boolean) {
        _progressBarDisplay.postValue(display)

    }

    fun setToolbarItems(toolbarModel: ToolbarModel) {
        _toolBarModel.postValue(toolbarModel)
    }

    fun hideKeyboard() {
        getHideKeyBoardEvent().value = true
    }

}
