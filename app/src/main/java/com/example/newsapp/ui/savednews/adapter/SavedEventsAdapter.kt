package com.example.newsapp.ui.savednews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemSavedNewsBinding
import com.example.newsapp.ui.savednews.SavedEventViewModel
import com.example.newsapp.ui.savednews.events.EventDetails
import com.example.newsapp.utility.toast


class SavedEventsAdapter(private var cx:Context, private var viewModel: SavedEventViewModel, private val allEvents:ArrayList<EventDetails>) :
    RecyclerView.Adapter<SavedEventsAdapter.MainViewHolder>() {

   /* private val allEvents = ArrayList<EventDetails>()

    fun updateList(userArrayList: List<EventDetails>) {
        allEvents.clear()
        allEvents.addAll(userArrayList)
        notifyDataSetChanged()
    }*/

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {
        val binding: ItemSavedNewsBinding =
            ItemSavedNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = allEvents[position]
        holder.binding.tvCatTitle.text = item.title
        holder.binding.tvCatDesc.text = item.description
        holder.binding.tvUserName.text = item.title
        Glide.with(holder.binding.root.context)
            .load(item.image_url)
            .override(512, 512)
            .dontAnimate()
            .error(R.drawable.logo_img)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.binding.itemHomeCategoryImg)

        Glide.with(holder.binding.root.context)
            .load(item.image_url)
            .error(R.drawable.ic_user)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.binding.userImage)

        holder.binding.deleteLayout.setOnClickListener {
            allEvents[position].id?.let { it -> viewModel.deleteEventById(it) }
            allEvents.removeAt(position)
            notifyDataSetChanged()
            cx.toast(cx.getString(R.string.delete_msg))

        }
    }

    override fun getItemCount(): Int {
        return allEvents.size
    }

    class MainViewHolder(val binding: ItemSavedNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}