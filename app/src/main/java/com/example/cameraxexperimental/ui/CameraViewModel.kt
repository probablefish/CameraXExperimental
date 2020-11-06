package com.example.cameraxexperimental.ui

import android.graphics.Bitmap
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameraxexperimental.data.remote.ICameraImageRepository
import kotlinx.coroutines.launch

class CameraViewModel @ViewModelInject constructor(
    private val cameraImageRepository: ICameraImageRepository
) : ViewModel() {

    data class Model(
        var modelImage: Bitmap?,
        var flashMode: MutableLiveData<Boolean>
    )

    private val liveData = MutableLiveData<Model>()
    private var model = Model(null, MutableLiveData(true))

    fun observable() = liveData

    fun update(image: Bitmap) {
        model.apply {
            modelImage = image
        }
        liveData.postValue(model)
    }

    fun toggleFlashMode() {
        model.apply {
            flashMode.value = (flashMode.value
                ?: throw NullPointerException("Expression 'flashMode.value' must not be null")).not()
        }

    }

    fun clearImage() {
        model.apply {
            modelImage = null
        }
        liveData.postValue(model)
    }

    fun getImage() = liveData.value?.modelImage

    fun sendImage() = viewModelScope.launch {
        model.modelImage?.let {
            cameraImageRepository.keepImage(it)
        }
    }
}