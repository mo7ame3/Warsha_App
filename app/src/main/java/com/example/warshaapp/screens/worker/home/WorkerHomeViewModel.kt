package com.example.warshaapp.screens.worker.home

import androidx.lifecycle.ViewModel
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.worker.home.WorkerHome
import com.example.warshaapp.repository.WorkerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkerHomeViewModel @Inject constructor(private val workerRepository: WorkerRepository) :
    ViewModel() {

    suspend fun getHome(
        authorization: String,
        craftId: String
    ): WrapperClass<WorkerHome, Boolean, Exception> {
        return workerRepository.getHome(craftId = craftId, authorization = "Bearer $authorization")
    }
}