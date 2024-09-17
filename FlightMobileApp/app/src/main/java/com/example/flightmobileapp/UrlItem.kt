package com.example.flightmobileapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "url_table")
data class UrlItem(
    @PrimaryKey val url: String,

    @ColumnInfo var num: Int = 0

)