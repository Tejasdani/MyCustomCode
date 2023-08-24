package com.example.newsapp.selectnews

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.selectnews.adapter.ChooseTopicsAdapter
import com.example.newsapp.selectnews.model.ChooseTopics
import com.example.newsapp.core.BaseVMBindingActivity
import com.example.newsapp.databinding.ActivitySelectTopicsBinding
import com.example.newsapp.ui.selectcategory.ChooseTopicViewModel
import com.example.newsapp.utility.CustomAlertDialog
import com.example.newsapp.utility.changeColorOfFilterButtons
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Created by Tejas on 16/Feb/2023
 * to select category news with validation of minimum 3 selection
 * */

@AndroidEntryPoint
class SelectTopicsActivity :
    BaseVMBindingActivity<ActivitySelectTopicsBinding, ChooseTopicViewModel>(ChooseTopicViewModel::class.java),
    ChooseTopicsAdapter.ItemClickListener {

    val context = this@SelectTopicsActivity

    override fun getPersistentView(): ActivitySelectTopicsBinding {
        return ActivitySelectTopicsBinding.inflate(layoutInflater)
    }

    private lateinit var chooseTopicsAdapter: ChooseTopicsAdapter
    private lateinit var customAlertDialog: CustomAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupRecyclerView()
        initClickListeners()
    }

    private fun initClickListeners() {
        binding.btnContinue.setOnClickListener {
            viewModel.setChipsPref()
            customAlertDialog = CustomAlertDialog(context)
            customAlertDialog.alertDialog {
                viewModel.setUserName(it)
            }
        }
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(context, 3)
        binding.rvTopics.layoutManager = gridLayoutManager
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        chooseTopicsAdapter =
            ChooseTopicsAdapter(context, viewModel.addTopics(), this)
        binding.rvTopics.adapter = chooseTopicsAdapter

    }

    private fun changeDrawable(isEnable: Boolean) {
        changeColorOfFilterButtons(context, isEnable, binding.btnContinue)
    }

    override fun ItemClick(data: ChooseTopics, adapterPosition: Int) {

        if (viewModel.newList[adapterPosition].isSelected) {
            viewModel.newList[adapterPosition].isSelected = false
            chooseTopicsAdapter.setSelected(false)
        } else {
            viewModel.newList[adapterPosition].isSelected = true
            chooseTopicsAdapter.setSelected(true)
        }
        val isEnable = viewModel.isEnableButton()
        changeDrawable(isEnable)
        viewModel.getChipsName(chipList = {
            it.forEach { name ->
                Timber.d("Pref Value $name")
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}