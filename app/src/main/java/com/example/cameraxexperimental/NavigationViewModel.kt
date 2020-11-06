package com.example.cameraxexperimental

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class NavigationViewModel @ViewModelInject constructor()  : ViewModel() {

    private val liveData = MutableLiveData<Screen>()

    fun observable() = liveData

    fun goTo(screen: Screen) {
        liveData.postValue(screen)
    }
}