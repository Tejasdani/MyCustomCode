package com.example.newsapp.ui.dashboard.ui

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.*
import com.example.newsapp.`interface`.RVListner
import com.example.newsapp.constants.Constants.NEWS_DATA
import com.example.newsapp.core.BaseVMBindingFragment
import com.example.newsapp.ui.dashboard.adapter.HomeAdapter
import com.example.newsapp.ui.dashboard.model.CategoryData
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.ui.dashboard.HomeViewModel
import com.example.newsapp.ui.newsdetails.NewsDetailsActivity
import com.example.newsapp.ui.savednews.events.EventDetails
import com.example.newsapp.utility.CustomAlertDialog
import com.example.newsapp.utility.handleAdapterIfEmpty
import com.example.newsapp.utility.toast
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment :
    BaseVMBindingFragment<FragmentHomeBinding, HomeViewModel>(HomeViewModel::class.java),
    HomeAdapter.SaveEventListner {

    override fun getPersistentView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val TAG = "HomeFragment"
    private lateinit var dashboardAdapter: HomeAdapter
    private lateinit var customAlertDialog: CustomAlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        setUserDetails()
        setChipGroupData()
        showSearchedData()
    }


    private fun setUserDetails() {
        viewModel.getUserName {
            if (!it.isNullOrEmpty()) {
                binding.tvUserName.text = it
            }
        }
    }

    private fun initObservers() {
        customAlertDialog = CustomAlertDialog(requireContext())
        binding.rvCategory.layoutManager = LinearLayoutManager(requireContext())
        dashboardAdapter = HomeAdapter(
            requireContext(), this, rvInterfaceInstance, viewModel
        )
        binding.rvCategory.adapter = dashboardAdapter

        /*lifecycleScope.launchWhenStarted {
            viewModel.response.collect {
                //Show dialog
                Timber.d("Success Msg $it")
            }
        }*/

        viewModel.responseList.observe(viewLifecycleOwner) { list ->
            list.handleAdapterIfEmpty(binding.tvNoEvents, binding.rvCategory) {
                dashboardAdapter.updateList(it)
            }
        }

        viewModel.responseList.observe(viewLifecycleOwner) { list ->
            list.handleAdapterIfEmpty(binding.tvNoEvents, binding.rvCategory) {
                dashboardAdapter.updateList(it)
            }
        }
    }

    private fun setChipGroupData() {

        viewModel.getStoreChipList(chipList = {
            it.forEach { chipName ->
                val chip = Chip(requireContext(), null, R.style.ChipCustomStyle)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5f,
                    resources.displayMetrics
                ).toInt()

                chip.setChipDrawable(
                    ChipDrawable.createFromResource(
                        requireContext(),
                        R.xml.my_chip
                    )
                )
                chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
                chip.isCloseIconVisible = false
                chip.text = chipName
                chip.setOnClickListener {
                    viewModel.getNewsApi(chip.chipText.toString())
                }

                binding.chipGroupView.addView(chip)

            }
        })
    }

    private val rvInterfaceInstance: RVListner = object : RVListner {
        override fun onClickedItem(view: View, list: List<CategoryData>) {
            val index: Int = binding.rvCategory.getChildAdapterPosition(view)
            if (index > 0) {
                val intent = Intent(requireContext(), NewsDetailsActivity::class.java)
                intent.putExtra(NEWS_DATA, list[binding.rvCategory.getChildAdapterPosition(view)])
                requireContext().startActivity(intent)
            }
        }
    }

    override fun onClickedSave(position: Int, list: List<CategoryData>) {
        viewModel.insertEvent(
            EventDetails(
                null,
                list[position].title,
                list[position].description,
                list[position].image_url,
                true
            )
        )
        requireContext().toast(requireContext().getString(R.string.save_successfully))
    }

    private fun showSearchedData() {
        binding.searchFilter.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.searchFilter.searchView.setQuery("", false)
                Timber.d(TAG + "onQueryTextSubmit", "Text: $query")
                viewModel.getNewsBySearch(query)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.d(TAG + "onQueryTextChange", "Text: $newText")
                return false
            }

        })
    }

}