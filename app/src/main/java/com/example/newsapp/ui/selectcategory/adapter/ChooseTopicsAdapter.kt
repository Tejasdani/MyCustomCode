package com.example.newsapp.selectnews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.newsapp.R
import com.example.newsapp.selectnews.model.ChooseTopics
import com.example.newsapp.databinding.ItemChooseTopicsBinding
import com.example.newsapp.utility.getDrawableCompat


class ChooseTopicsAdapter(
    private val cx: Context,
    private val listItem: ArrayList<ChooseTopics>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<ChooseTopicsAdapter.ChooseViewHolder>() {

    interface ItemClickListener {
        fun ItemClick(data: ChooseTopics, position: Int)
    }

    lateinit var dataBinding: ItemChooseTopicsBinding
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChooseViewHolder {
        val binding: ItemChooseTopicsBinding =
            ItemChooseTopicsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        dataBinding = binding
        return ChooseViewHolder(binding, itemClickListener)
    }


    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) =
        holder.bind(listItem[position])


    override fun getItemCount(): Int {
        return listItem.size
    }

    inner class ChooseViewHolder(
        private val itemBinding: ItemChooseTopicsBinding,
        private val listener: ItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        lateinit var user: ChooseTopics

        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: ChooseTopics) {
            this.user = item
            itemBinding.tvCategory.text = item.topics

            Glide.with(itemBinding.root.context)
                .load(item.imgTopics)
                .override(512, 512)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(itemBinding.imgCategory)
            if (item.isSelected) {
                itemBinding.topicsLayout.background = cx.getDrawableCompat(R.drawable.bg_select_category)
            } else itemBinding.topicsLayout.background =
                cx.getDrawableCompat(R.drawable.bg_unselect_category)
        }

        override fun onClick(v: View?) {
            listener.ItemClick(user, adapterPosition)
        }
    }

    fun setSelected(isSelected: Boolean) {
        if (isSelected) {
            dataBinding.topicsLayout.background = cx.getDrawable(R.drawable.bg_select_category)

        } else dataBinding.topicsLayout.background =
            cx.getDrawable(R.drawable.bg_unselect_category)

        notifyDataSetChanged()
    }
}