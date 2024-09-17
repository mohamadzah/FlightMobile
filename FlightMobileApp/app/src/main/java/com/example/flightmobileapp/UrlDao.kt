package com.example.flightmobileapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao

interface UrlDao {
    @Query("SELECT * from url_table ORDER BY num DESC LIMIT 5")
    fun getAlphabetizedWords(): LiveData<List<UrlItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(urlItem: UrlItem)

    @Query("DELETE FROM url_table")
    suspend fun deleteAll()

    @Query("DELETE FROM url_table WHERE url == :url_item")
    suspend fun delete(url_item: String)

    @Query("SELECT * FROM url_table WHERE url == :url")
    suspend fun getUrl(url: String): UrlItem

    // use this for the insert!! @Insert(onConflict = OnConflictStrategy.REPLACE)
    //     public void insertUsers(User... users);
}
