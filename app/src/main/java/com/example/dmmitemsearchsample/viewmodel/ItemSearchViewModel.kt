package com.example.dmmitemsearchsample.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dmmitemsearchsample.common.Constants
import com.example.dmmitemsearchsample.common.repository.DmmApiRepository
import com.example.dmmitemsearchsample.model.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ItemSearchViewModel(
    private val dmmApiRepository: DmmApiRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    data class ItemModel(
        val item: Item
    )

    data class Pages(val totalCount: Int, val firstPosition: Int)
    data class ItemsResult(val itemData: List<ItemModel>, val pages: Pages)

    private val _itemsResult = MutableLiveData<ItemsResult>()
    val itemsResult: LiveData<ItemsResult> = _itemsResult

    init {

    }

    fun getItems(
            keyword: String,
            offset: Int

    ){
        compositeDisposable.add(
            dmmApiRepository.getItems(
                "DMM.com",//site,
                "digital", //service,
                offset,
                Constants.hits,
                "idol",
                "date",
                keyword
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        _itemsResult.value = ItemsResult(
                            result.result.items!!.map {
                                Log.d("http", it.toString())
                                ItemModel(it) },
                            Pages(
                                result.result.total_count, result.result.first_position
                            )
                        )
                    },
                    {
                        // TODO: エラー処理
                        it.printStackTrace()
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    class Factory(
        private val dmmApiRepository: DmmApiRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ItemSearchViewModel(
                dmmApiRepository
            ) as T
        }
    }
}