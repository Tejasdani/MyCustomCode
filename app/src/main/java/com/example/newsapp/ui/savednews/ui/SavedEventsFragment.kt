package com.example.newsapp.ui.savednews.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.core.BaseVMBindingFragment
import com.example.newsapp.databinding.FragmentSavedEventsBinding
import com.example.newsapp.ui.savednews.SavedEventViewModel
import com.example.newsapp.ui.savednews.adapter.SavedEventsAdapter
import com.example.newsapp.ui.savednews.events.EventDetails
import com.example.newsapp.utility.handleAdapterIfEmpty
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SavedEventsFragment :
    BaseVMBindingFragment<FragmentSavedEventsBinding, SavedEventViewModel>(SavedEventViewModel::class.java) {

    override fun getPersistentView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSavedEventsBinding {
        return FragmentSavedEventsBinding.inflate(layoutInflater)
    }

    private lateinit var savedAdapter: SavedEventsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGetDashboardData()


    }


    private fun initGetDashboardData() {
        //setCategoryData()
        observeEvents()
    }

    /*private fun setCategoryData() {
        savedAdapter = SavedEventsAdapter(requireContext(),viewModel)
        binding.rvSavedEvents.adapter = savedAdapter

    }*/

    private fun observeEvents() {
        binding.rvSavedEvents.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launchWhenStarted {
            viewModel.response.collect { list ->
                list.handleAdapterIfEmpty(binding.tvNoEvents, binding.rvSavedEvents) {
                    savedAdapter = SavedEventsAdapter(
                        requireContext(), viewModel,
                        it as ArrayList<EventDetails>
                    )
                    binding.rvSavedEvents.adapter = savedAdapter
                    savedAdapter.notifyDataSetChanged()
                }
            }

            /* list?.let {
                 // updates the list.
                 savedAdapter.updateList(it)
             }*/
        }
    }

}