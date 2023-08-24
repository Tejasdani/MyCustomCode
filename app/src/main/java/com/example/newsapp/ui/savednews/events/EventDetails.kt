package com.example.newsapp.ui.savednews.events

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
Created by Tejas Dani on 21/Feb/2023,
for data class of Category details required for DB,
ACS Solution
 */

@Entity(tableName = "categoryTable")
data class EventDetails(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    val id: Int?,

    @ColumnInfo(name = "event_title")
    var title: String?,

    @ColumnInfo(name = "event_description")
    val description: String?,

    @ColumnInfo(name = "event_url")
    val image_url: String?,

    @ColumnInfo(name = "isSaved")
    val isSaved: Boolean?
)
