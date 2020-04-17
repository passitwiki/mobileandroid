package com.passitwiki.passit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.passitwiki.passit.activity.accessToken
import com.passitwiki.passit.networking.Resource
import com.passitwiki.passit.repository.Repository
import kotlinx.coroutines.Dispatchers

class DashboardViewModel(private val repository: Repository) : ViewModel() {
    fun loadNews() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        emit(repository.handleGetNews(accessToken))
    }
}