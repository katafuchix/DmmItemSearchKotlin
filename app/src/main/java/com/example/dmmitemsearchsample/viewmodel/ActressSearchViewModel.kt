package com.example.dmmitemsearchsample.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dmmitemsearchsample.common.Constants
import com.example.dmmitemsearchsample.common.repository.DmmApiRepository
import com.example.dmmitemsearchsample.model.Actress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ActressSearchViewModel(
    private val dmmApiRepository: DmmApiRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    data class ActressModel(
        val actress: Actress
    )

    data class Pages(val totalCount: Int, val firstPosition: Int)
    data class ActressResult(val actressData: List<ActressModel>, val pages: Pages)

    private val _actressResult = MutableLiveData<ActressResult>()
    val actressResult: LiveData<ActressResult> = _actressResult

    init {

    }

    fun getActresses(
        keyword: String,
        offset: Int

    ){
        compositeDisposable.add(
            dmmApiRepository.getActresses(
                offset,
                Constants.hits,
                "name",
                keyword
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        _actressResult.value = ActressResult(
                            result.result.actress!!.map {
                                Log.d("http", it.toString())
                                ActressModel(it) },
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
            return ActressSearchViewModel(
                dmmApiRepository
            ) as T
        }
    }
}