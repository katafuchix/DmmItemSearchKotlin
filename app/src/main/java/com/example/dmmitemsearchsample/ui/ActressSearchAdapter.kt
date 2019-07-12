package com.example.dmmitemsearchsample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.dmmitemsearchsample.R
import com.example.dmmitemsearchsample.common.util.ImageLoader
import com.example.dmmitemsearchsample.databinding.CustomSearchActressBinding
import com.example.dmmitemsearchsample.databinding.CustomSearchItemBinding
import kotlin.math.roundToInt

class ActressSearchAdapter : RecyclerView.Adapter<ActressSearchAdapter.ActressViewHolder>() {

    interface OnClickListener {
        fun onClick(item: ActressSearchAdapterViewModel)
    }

    class ActressViewHolder(private val binding: CustomSearchActressBinding, private val listener: OnClickListener?) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ActressSearchAdapterViewModel, screenWidth: Int) {

            if (data.imageUrl.value == null) {
                ImageLoader.imageUrlToView(R.drawable.avatar_default_new, binding.memberImage.image)
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

    val dataList = object : ArrayList<ActressSearchAdapterViewModel>() {
        override fun addAll(elements: Collection<ActressSearchAdapterViewModel>): Boolean {
            val result = super.addAll(elements)
            notifyDataSetChanged()
            return result
        }

        override fun clear() {
            super.clear()
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActressViewHolder {
        return ActressViewHolder(
            DataBindingUtil.inflate<CustomSearchActressBinding>(
                LayoutInflater.from(parent.context),
                R.layout.custom_search_actress,
                parent,
                false
            ).apply {
                this.lifecycleOwner = this@ActressSearchAdapter.lifecycleOwner
            },
            listener
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ActressViewHolder, position: Int) {
        holder.bind(dataList[position], screenWidth!!)
    }
}
