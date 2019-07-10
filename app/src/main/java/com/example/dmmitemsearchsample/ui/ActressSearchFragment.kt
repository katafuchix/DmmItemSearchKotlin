package com.example.dmmitemsearchsample.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dmmitemsearchsample.R
import com.example.dmmitemsearchsample.common.Constants
import com.example.dmmitemsearchsample.common.repository.DmmApiRepositoryImpl
import com.example.dmmitemsearchsample.databinding.FragmentActresssearchBinding
import com.example.dmmitemsearchsample.viewmodel.ActressSearchViewModel
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.subjects.BehaviorSubject

class ActressSearchFragment : Fragment() {

    companion object {
        fun createInstance(): ActressSearchFragment {
            val fragment = ActressSearchFragment()
            return fragment
        }
    }

    private val viewModel
            by lazy {
                context?.run {
                    ViewModelProviders.of(
                        this@ActressSearchFragment,
                        ActressSearchViewModel.Factory(
                            DmmApiRepositoryImpl()
                        )
                    ).get(ActressSearchViewModel::class.java)
                }
            }

    private var loadingOffset: Int = 1
    private var currentOffset: Int = 1
    private var totalCount: Int = 0

    private val adapter by lazy {
        ActressSearchAdapter().apply {
            this.lifecycleOwner = this@ActressSearchFragment
            this.listener = object : ActressSearchAdapter.OnClickListener {
                override fun onClick(item: ActressSearchAdapterViewModel) {
                    onClickActress(item)
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
            DataBindingUtil.inflate<FragmentActresssearchBinding>(
                inflater,
                R.layout.fragment_actresssearch,
                container,
                false
            )
                .apply {
                    val binding = this
                    val actressSearchFragment = this@ActressSearchFragment
                    binding.lifecycleOwner = actressSearchFragment
                    binding.viewModel = actressSearchFragment.viewModel
                    binding.presenter = actressSearchFragment

                    binding.listRecyclerView.apply {
                        this.layoutManager = GridLayoutManager(actressSearchFragment.context, 2)
                        this.setHasFixedSize(true)

                        // スクロール終了時の処理
                        this.addOnScrollListener(ScrollListener {

                            if (isLoading.value == false
                                && !lastItemLoaded
                                && this@ActressSearchFragment.totalCount > this@ActressSearchFragment.loadingOffset + Constants.hits
                            ) {

                                // 検索中
                                isLoading.value = true

                                // APIコール
                                this@ActressSearchFragment.viewModel?.getActresses(
                                    this@ActressSearchFragment.keyword,
                                    this@ActressSearchFragment.loadingOffset
                                )
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
                            actressSearchFragment.keyword = query
                            return true
                        }
                    })

                    // 検索
                    keywordSubject.subscribe {
                        actressSearchFragment.loadingOffset = 1
                        actressSearchFragment.viewModel?.getActresses(it, 1)
                    }

                    // 検索結果
                    actressSearchFragment.viewModel?.actressResult?.observe(actressSearchFragment, Observer {
                        // リストデータ追加
                        Log.d("http", it.actressData.toString())
                        adapter.dataList.addAll(
                            it.actressData.map {
                                ActressSearchAdapterViewModel().apply {
                                    val viewModel = this
                                    viewModel.id.value = it.actress.id
                                    viewModel.title.value = it.actress.name
                                    //viewModel.affiliateURL.value = it.item.affiliateURL

                                    it.actress.imageURL?.apply {
                                        viewModel.imageUrl.value = this?.large
                                    }
                                }
                            }
                        )

                        // ページ数
                        it.pages.run {
                            this@ActressSearchFragment.totalCount = this.totalCount
                            if (this.totalCount > this@ActressSearchFragment.loadingOffset + Constants.hits) {
                                this@ActressSearchFragment.loadingOffset =
                                    this@ActressSearchFragment.loadingOffset + Constants.hits
                            } else {
                                this@ActressSearchFragment.lastItemLoaded = true
                            }
                        }
                        // 検索終了
                        this@ActressSearchFragment.isLoading.value = false
                    })

                    // ロード中
                    isLoading.observe(actressSearchFragment, Observer {
                        if (it) {
                            hud = KProgressHUD.create(this@ActressSearchFragment.context)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            hud!!.show()
                        } else {
                            hud?.dismiss()
                        }
                    })

                    // UI設定
                    return binding.root.apply {
                        val root = this
                        this.viewTreeObserver.addOnGlobalLayoutListener(object :
                            ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                                // RecyclerView に adapter を設定
                                binding.listRecyclerView.adapter = this@ActressSearchFragment.adapter.apply {
                                    this.screenWidth = root.width
                                }
                            }
                        })
                    }
                }
    }

    fun onClickActress(item: ActressSearchAdapterViewModel) {
        val uri = Uri.parse(item.affiliateURL.value)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
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