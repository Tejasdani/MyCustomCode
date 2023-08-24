package com.example.newsapp.ui.savednews

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.newsapp.core.BaseViewModel
import com.example.newsapp.db.repository.EventRepository
import com.example.newsapp.ui.savednews.events.EventDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Created by Tejas Dani on 212-Feb-2023
 * for view model of Saved News Data
 * */
class SavedEventViewModel @Inject constructor(
    val app: Application,
    private val repository: EventRepository
) : BaseViewModel(app) {
    private var _response = MutableSharedFlow<List<EventDetails>>()
    val response = _response.asSharedFlow()

    fun getAllEvents() {
        viewModelScope.launch {
            _response.emit(repository.getAllEvents())
        }
    }

    fun deleteEvent(event: EventDetails) =
        viewModelScope.launch(Dispatchers.IO) { repository.deleteEvent(event) }

    fun deleteEventById(id: Int) =
        viewModelScope.launch(Dispatchers.IO) { repository.deleteEventById(id) }
}