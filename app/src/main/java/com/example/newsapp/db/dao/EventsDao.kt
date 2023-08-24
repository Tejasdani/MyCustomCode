package com.example.newsapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.constants.Constants
import com.example.newsapp.ui.savednews.events.EventDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@Dao
interface EventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventDetails)

    @Delete
    suspend fun deleteEvent(event: EventDetails)

    @Update
    suspend fun updateEvent(event: EventDetails)

    @Query("Select * FROM ${Constants.DB_NAME} order by ${Constants.EVENT_ID} DESC")
    suspend fun getAllEvents(): List<EventDetails>

    @Query("DELETE FROM ${Constants.DB_NAME}")
    suspend fun clearEvent()

    @Query("DELETE FROM ${Constants.DB_NAME} WHERE ${Constants.EVENT_ID} = :id")
    suspend fun deleteEventById(id: Int)

    @Query("SELECT * from ${Constants.DB_NAME} WHERE ${Constants.EVENT_TITLE} LIKE '%' || :search || '%'")
    fun getSearchTitle(search: String): Flow<List<EventDetails>>
}