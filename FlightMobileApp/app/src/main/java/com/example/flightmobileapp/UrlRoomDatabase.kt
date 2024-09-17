package com.example.flightmobileapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [UrlItem::class], version = 2, exportSchema = false)
abstract class UrlRoomDatabase : RoomDatabase() {

    abstract fun urlDao(): UrlDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.

        @Volatile
        private var INSTANCE: UrlRoomDatabase? = null

        fun getDatabase(context: Context): UrlRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UrlRoomDatabase::class.java,
                    "url_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}