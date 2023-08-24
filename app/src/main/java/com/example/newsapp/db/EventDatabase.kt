package com.example.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapp.db.dao.EventsDao
import com.example.newsapp.ui.savednews.events.EventDetails


private const val DB_NAME = "event_database.db"
@Database(
    entities = [EventDetails::class],
    version = 1,
    exportSchema = false
)
abstract class EventDatabase :RoomDatabase(){
    abstract fun getEventDao(): EventsDao

    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getDatabase(context: Context): EventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    DB_NAME
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}