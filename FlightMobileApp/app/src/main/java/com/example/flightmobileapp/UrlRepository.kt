package com.example.flightmobileapp

import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class UrlRepository(private val urlDao: UrlDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<UrlItem>> = urlDao.getAlphabetizedWords()

    suspend fun insert(urlItem: UrlItem) {
        if (allWords.value != null) {
            if (allWords.value!!.isNotEmpty()) {
                val count = allWords.value!!.first().num
                urlItem.num = count + 1
            }
        }
        urlDao.insert(urlItem)
    }

}