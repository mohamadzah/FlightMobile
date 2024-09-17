package com.example.flightmobileapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UrlViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UrlRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<UrlItem>>

    init {
        val urlsDao = UrlRoomDatabase.getDatabase(application).urlDao()
        repository = UrlRepository(urlsDao)
        allWords = repository.allWords
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(urlItem: UrlItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(urlItem)
    }

}