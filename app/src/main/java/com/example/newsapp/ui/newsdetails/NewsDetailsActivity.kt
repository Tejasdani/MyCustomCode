package com.example.newsapp.ui.newsdetails

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.newsapp.R
import com.example.newsapp.constants.Constants.COPY_NEWS
import com.example.newsapp.constants.Constants.NEWS_DATA
import com.example.newsapp.core.BaseVMBindingActivity
import com.example.newsapp.databinding.ActivityDescriptionBinding
import com.example.newsapp.ui.dashboard.model.CategoryData
import com.example.newsapp.ui.dashboard.model.NewTestModel
import com.example.newsapp.utility.toast
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber


@AndroidEntryPoint
class NewsDetailsActivity :
    BaseVMBindingActivity<ActivityDescriptionBinding, NewsDetailsViewModel>(NewsDetailsViewModel::class.java) {

    override fun getPersistentView(): ActivityDescriptionBinding {
        return ActivityDescriptionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newDetails = intent?.getParcelableExtra<CategoryData>(NEWS_DATA)
        Timber.d("DATA: $newDetails")
        newDetails?.let { setDescData(it) }
        newDetails?.let { copyNewsLink(it) }
        newDetails?.let { shareNewsLink(it) }

    }


    /*Show News Details from intent*/
    private fun setDescData(data: CategoryData) {
        binding.tvNewsTitle.text = data.title
        binding.tvNewsDesc.text = data.description
        binding.tvUserName.text = data.title
        binding.tvNewsTime.text = data.pubDate

        Glide.with(this@NewsDetailsActivity)
            .load(data.image_url)
            .override(512, 512)
            .dontAnimate()
            .error(R.drawable.logo_img)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.newsIV)

        Glide.with(this@NewsDetailsActivity)
            .load(data.image_url)
            .override(512, 512)
            .dontAnimate()
            .error(R.drawable.logo_img)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.userImage)

    }

    /*Share News*/
    private fun shareNewsLink(data: CategoryData) {
        binding.btnShareLink.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, data.link)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    /*Share News*/
    private fun copyNewsLink(data: CategoryData) {
        binding.btnCopyLink.setOnClickListener {
            val clipboard: ClipboardManager =
                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(COPY_NEWS, data.link)
            clipboard.setPrimaryClip(clip)
            toast(this@NewsDetailsActivity.resources.getString(R.string.copy_successfully))
        }

    }


    private fun setupFlow() {
        val flow = flow {
            Log.d("setupFlow ", "Start flow")
            (0..10).forEach {
                // Emit items with 500 milliseconds delay
                delay(500)
                Log.d("Emitting Value ", "Emitting $it")
                emit(it)
            }
        }.map {
            it * it
        }.flowOn(Dispatchers.Default)
        println(flow)
    }
}