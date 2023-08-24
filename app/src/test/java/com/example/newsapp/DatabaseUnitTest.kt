package com.example.newsapp

import android.app.Application
import androidx.room.Room
import com.example.newsapp.constants.Constants
import com.example.newsapp.db.EventDatabase
import com.example.newsapp.db.dao.EventsDao
import com.example.newsapp.db.repository.EventRepository

import com.example.newsapp.ui.dashboard.HomeRepo
import com.example.newsapp.ui.dashboard.HomeViewModel
import com.example.newsapp.ui.savednews.events.EventDetails
import com.example.newsapp.utility.PreferenceStore
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

/*Created By Tejas Dani on 10/March/2023
* to test Room Database Queries and Methods inside DAO
* */

class DatabaseUnitTest {

    private lateinit var homeViewModelTarget: HomeViewModel
    private val applicationMock = mock<Application>()
    private val mockEventRepo = mock<EventRepository>()
    private val mockHomeRepo = mock<HomeRepo>()
    private val dataStoreMock = mock<PreferenceStore>()
    private var mockDatabase = mock<EventDatabase>()
    private var mockDao = mock<EventsDao>()

    private val setList = mutableSetOf("Hiiii", "World")

    private val event = EventDetails(1, "Event Title", "Event Description", "", true)

    @Before
    fun setUp() {

        mockDatabase = Room.inMemoryDatabaseBuilder(
            applicationMock, EventDatabase::class.java
        ).build()

        mockDao = mockDatabase.getEventDao()

        homeViewModelTarget =
            HomeViewModel(app = applicationMock, repo = mockHomeRepo, repoDatabase = mockEventRepo)
        homeViewModelTarget.preferenceStore = dataStoreMock
        Dispatchers.setMain(Dispatchers.Unconfined)
    }


    @Test
    fun isDataStoreValid() = runBlocking {
        val variable = mutableListOf<String>()
        given(dataStoreMock.getSetStringList(Constants.CATEGORY_PREF)).willReturn(setList)
        homeViewModelTarget.getStoreChipList {
            variable.addAll(it)
        }
        assertEquals(variable, setList.toList())

    }


    @Test
    fun insertEvent_returnTrue() = runBlocking {
        val responseList = mutableListOf<EventDetails>()
        responseList.add(event)
        given(mockEventRepo.getAllEvents()).willReturn(responseList)
        mockEventRepo.insertEvent(event)
        val result = mockEventRepo.getAllEvents()
        assertTrue(result.contains(event))
    }


    @Test
    fun updateEvents_returnTrue() = runBlocking {
        mockEventRepo.insertEvent(event)
        val responseList = mutableListOf<EventDetails>()
        given(mockEventRepo.getAllEvents()).willReturn(responseList)
        event.title = "This is Updated Events"
        responseList.add(event)
        mockEventRepo.updateEvent(event)
        val result = mockEventRepo.getAllEvents()
        assertEquals(result[0].title, event.title)
    }

    @Test
    fun deleteAllEvents() = runBlocking {
        val responseList = mutableListOf<EventDetails>()
        responseList.add(event)
        mockEventRepo.deleteEvent(event)
        verify(mockEventRepo).deleteEvent(event)// verify when the data is deleted or not
    }

    @After
    fun closeDatabase() {
        mockDatabase.close()
    }

}