package com.example.dmmitemsearchsample.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.dmmitemsearchsample.common.util.ImageLoader
import com.example.dmmitemsearchsample.databinding.CustomSearchItemBinding
import com.example.dmmitemsearchsample.R
import kotlin.math.roundToInt

class ItemSearchAdapter : RecyclerView.Adapter<ItemSearchAdapter.ItemViewHolder>() {

    interface OnClickListener {
        fun onClick(item: ItemSearchAdapterViewModel)
    }

    class ItemViewHolder(private val binding: CustomSearchItemBinding, private val listener: OnClickListener?) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ItemSearchAdapterViewModel, screenWidth: Int) {

            if (data.imageUrl.value == null) {
                //ImageLoader.imageUrlToView(R.drawable.avatar_default_new, binding.memberImage.image)
            } else {
                ImageLoader.imageUrlToViewAsCircle(
                    data.imageUrl.value!!,
                    R.drawable.avatar_default_new,
                    binding.memberImage.image
                )
            }

            binding.viewModel = data

            binding.memberImage.image.layoutParams = binding.memberImage.image.layoutParams.apply {
                this.width = ((screenWidth / 2) * 0.85).roundToInt()
                this.height = ((screenWidth / 2) * 0.85).roundToInt()
            }

            binding.card.setOnClickListener { listener?.onClick(data) }
        }
    }

    lateinit var lifecycleOwner: LifecycleOwner

    var listener: OnClickListener? = null

    var screenWidth: Int? = null

    val dataList = object : ArrayList<ItemSearchAdapterViewModel>() {
        override fun addAll(elements: Collection<ItemSearchAdapterViewModel>): Boolean {
            val result = super.addAll(elements)
            notifyDataSetChanged()
            return result
        }

        override fun clear() {
            super.clear()
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            DataBindingUtil.inflate<CustomSearchItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.custom_search_item,
                parent,
                false
            ).apply {
                this.lifecycleOwner = this@ItemSearchAdapter.lifecycleOwner
            },
            listener
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], screenWidth!!)
    }

}