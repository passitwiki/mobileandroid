package com.passitwiki.passit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.passitwiki.passit.networking.Resource
import com.passitwiki.passit.repository.Repository
import kotlinx.coroutines.Dispatchers

class SubjectsViewModel(private val repository: Repository) : ViewModel() {
    fun loadSubjects(semester:Int, fieldOfStudy:Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        emit(repository.handleGetSubjects(semester, fieldOfStudy))
    }
}