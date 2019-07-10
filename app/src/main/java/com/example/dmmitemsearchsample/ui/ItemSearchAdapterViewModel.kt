package com.example.dmmitemsearchsample.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemSearchAdapterViewModel : ViewModel() {
    val id = MutableLiveData<String>()
    val affiliateURL = MutableLiveData<String>()
    val showNew = MutableLiveData<Boolean>()
    val showNow = MutableLiveData<Boolean>()
    val imageUrl = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    val title = MutableLiveData<String>()
}
