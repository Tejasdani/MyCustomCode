package com.example.newsapp.db.repository

import androidx.lifecycle.LiveData
import com.example.newsapp.db.dao.EventsDao
import com.example.newsapp.ui.savednews.events.EventDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventDao: EventsDao) {

   suspend fun getAllEvents(): List<EventDetails> = eventDao.getAllEvents()

    suspend fun insertEvent(event: EventDetails) {
        eventDao.insertEvent(event)
    }

    // deletes an event from database.
    suspend fun deleteEvent(event: EventDetails) {
        eventDao.deleteEvent(event)
    }

    // updates an event from database.
    suspend fun updateEvent(event: EventDetails) {
        eventDao.updateEvent(event)
    }

    //delete an event by id.
    suspend fun deleteEventById(id: Int) = eventDao.deleteEventById(id)

    // delete all events
    suspend fun clearEvent() = eventDao.clearEvent()

    //fun searchEventTitle(title:String) = eventDao.searchTitle(title)
}