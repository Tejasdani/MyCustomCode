package com.example.newsapp.ui.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.newsapp.R
import com.example.newsapp.`interface`.RVListner
import com.example.newsapp.ui.dashboard.model.CategoryData
import com.example.newsapp.databinding.ItemDashboardBinding
import com.example.newsapp.ui.dashboard.HomeViewModel

class HomeAdapter(
    private var cx: Context,
    private var listner: SaveEventListner,
    private var detailListner: RVListner,
    private var homeViewModel: HomeViewModel
) : RecyclerView.Adapter<HomeAdapter.MainViewHolder>() {

    interface SaveEventListner {
        fun onClickedSave(position: Int, list: List<CategoryData>)
    }

    var newsList: ArrayList<CategoryData> = arrayListOf()

    fun updateList(_newsList: List<CategoryData>) {
        newsList.clear()
        newsList = _newsList as ArrayList<CategoryData>
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {
        val binding: ItemDashboardBinding =
            ItemDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = newsList[position]
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
            .load(item.pubDate)
            .error(R.drawable.ic_user)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.binding.userImage)

        holder.binding.savedLayout.setOnClickListener {
            /* if(newsList[position].title!!.equals(newsList[position].title?.let { title ->
                     homeViewModel.isExistTitle(
                         title
                     )
                 }))
             {
                 cx.toast(cx.resources.getString(R.string.saved_msg))
             } else {
                 listner.onClickedSave(position, newsList)
             }*/
            listner.onClickedSave(position, newsList)

        }
        holder.itemView.setOnClickListener {
            detailListner.onClickedItem(holder.itemView, newsList)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class MainViewHolder(val binding: ItemDashboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}