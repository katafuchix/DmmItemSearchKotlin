package com.example.dmmitemsearchsample.ui

import android.os.Bundle
import android.provider.SyncStateContract
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dmmitemsearchsample.R
import com.example.dmmitemsearchsample.common.Constants
import com.example.dmmitemsearchsample.common.repository.DmmApiRepositoryImpl
import com.example.dmmitemsearchsample.viewmodel.ItemSearchViewModel
import com.example.dmmitemsearchsample.databinding.FragmentItemsearchBinding
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.subjects.BehaviorSubject
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData

class ItemSearchFragment : Fragment() {

    companion object {
        fun createInstance(): ItemSearchFragment {
            val fragment = ItemSearchFragment()
            return fragment
        }
    }

    private val viewModel
            by lazy {
                context?.run {
                    ViewModelProviders.of(
                        this@ItemSearchFragment,
                        ItemSearchViewModel.Factory(
                            DmmApiRepositoryImpl()
                        )
                    ).get(ItemSearchViewModel::class.java)
                }
            }

    private var loadingOffset: Int = 1
    private var currentOffset: Int = 1
    private var totalCount: Int = 0

/*
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val r = inflater.inflate(R.layout.fragment_itemsearch, container, false)
        return r
    }
*/

    private val adapter by lazy {
        ItemSearchAdapter().apply {
            this.lifecycleOwner = this@ItemSearchFragment
            this.listener = object : ItemSearchAdapter.OnClickListener {
                override fun onClick(item: ItemSearchAdapterViewModel) {
                    onClickItem(item)
                }
            }
        }
    }

    // keyword監視
    val keywordSubject = BehaviorSubject.create<String>()
    var keyword: String = ""
        set(value) {
            keywordSubject.onNext(value)
        }

    var hud: KProgressHUD? = null
    var lastItemLoaded: Boolean = false
    var isLoading = MutableLiveData<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil.inflate<FragmentItemsearchBinding>(inflater, R.layout.fragment_itemsearch, container, false)
                .apply {
                    val binding = this
                    val itemSearchFragment = this@ItemSearchFragment
                    binding.lifecycleOwner = itemSearchFragment
                    binding.viewModel = itemSearchFragment.viewModel
                    binding.presenter = itemSearchFragment

                    binding.listRecyclerView.apply {
                        this.layoutManager = GridLayoutManager(itemSearchFragment.context, 2)
                        this.setHasFixedSize(true)

                        // スクロール終了時の処理
                        this.addOnScrollListener(ScrollListener {

                            if (isLoading.value == false
                                && !lastItemLoaded
                                && this@ItemSearchFragment.totalCount > this@ItemSearchFragment.loadingOffset + Constants.hits) {

                                // 検索中
                                isLoading.value = true

                                // APIコール
                                this@ItemSearchFragment.viewModel?.getItems(
                                                        this@ItemSearchFragment.keyword,
                                                        this@ItemSearchFragment.loadingOffset)
                            }
                        })
                    }

                    // 検索窓
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextChange(newText: String): Boolean {
                            return false
                        }

                        // 検索ワード決定時
                        override fun onQueryTextSubmit(query: String): Boolean {
                            // ロード中
                            isLoading.value = true
                            // リストをクリア
                            adapter.dataList.clear()
                            // キーボードを下げる
                            searchView.clearFocus()
                            // 検索開始
                            itemSearchFragment.keyword = query
                            return true
                        }
                    })

                    // 検索
                    keywordSubject.subscribe {
                        itemSearchFragment.loadingOffset = 1
                        itemSearchFragment.viewModel?.getItems(it, 1)
                    }

                    // 検索結果
                    itemSearchFragment.viewModel?.itemsResult?.observe(itemSearchFragment, Observer {
                        // リストデータ追加
                        adapter.dataList.addAll(
                            it.itemData.map {
                                ItemSearchAdapterViewModel().apply {
                                    val viewModel = this
                                    viewModel.id.value              = it.item.content_id
                                    viewModel.title.value           = it.item.title
                                    viewModel.affiliateURL.value    = it.item.affiliateURL

                                    it.item.imageURL?.apply {
                                        viewModel.imageUrl.value = this.large
                                    }
                                }
                            }
                        )

                        // ページ数
                        it.pages.run {
                            this@ItemSearchFragment.totalCount = this.totalCount
                            if(this.totalCount > this@ItemSearchFragment.loadingOffset + Constants.hits) {
                                this@ItemSearchFragment.loadingOffset = this@ItemSearchFragment.loadingOffset + Constants.hits
                            } else {
                                this@ItemSearchFragment.lastItemLoaded = true
                            }
                        }
                        // 検索終了
                        this@ItemSearchFragment.isLoading.value = false
                    })

                    // ロード中
                    isLoading.observe(itemSearchFragment, Observer {
                        if (it) {
                            hud = KProgressHUD.create(this@ItemSearchFragment.context)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            hud!!.show()
                        } else {
                            hud?.dismiss()
                        }
                    })
                }

        // UI設定
        return binding.root.apply {
            val root = this
            this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // RecyclerView に adapter を設定
                    binding.listRecyclerView.adapter = this@ItemSearchFragment.adapter.apply {
                        this.screenWidth = root.width
                    }
                }
            })
        }
    }

    fun onClickItem(item: ItemSearchAdapterViewModel) {
        val uri = Uri.parse(item.affiliateURL.value)
        startActivity(Intent(Intent.ACTION_VIEW,uri))
    }

    // スクロール終了時の処理
    private class ScrollListener(private val paging: () -> Unit?) : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val totalCount = recyclerView.adapter?.itemCount //合計のアイテム数
            val childCount = recyclerView.childCount // RecyclerViewに表示されてるアイテム数
            val layoutManager = recyclerView.layoutManager

            if (layoutManager is GridLayoutManager) {
                // RecyclerViewに表示されている一番上のアイテムポジション
                val firstPosition = layoutManager.findFirstVisibleItemPosition()
                if (totalCount == childCount + firstPosition) {
                    // ページング処理
                    paging()
                }
            }
        }
    }
}