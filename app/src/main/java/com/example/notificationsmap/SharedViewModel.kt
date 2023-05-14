package com.example.notificationsmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    var latCoord = MutableLiveData<String>()
    var lngCoord = MutableLiveData<String>()

}