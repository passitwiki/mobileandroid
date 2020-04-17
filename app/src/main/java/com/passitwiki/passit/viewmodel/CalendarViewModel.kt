package com.passitwiki.passit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.passitwiki.passit.activity.accessToken
import com.passitwiki.passit.networking.Resource
import com.passitwiki.passit.repository.Repository
import kotlinx.coroutines.Dispatchers

class CalendarViewModel(private val repository: Repository) : ViewModel() {
    fun loadEvents() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        emit(repository.handleGetEvents(accessToken))
    }
}