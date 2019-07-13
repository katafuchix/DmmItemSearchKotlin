package com.example.dmmitemsearchsample.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActressSearchAdapterViewModel : ViewModel() {
    val id = MutableLiveData<String>()
    val affiliateURL = MutableLiveData<String>()
    val imageUrl = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val ruby = MutableLiveData<String>()

    val height = MutableLiveData<String>()
    val bust = MutableLiveData<String>()
    val waist = MutableLiveData<String>()
    val hip = MutableLiveData<String>()
}
