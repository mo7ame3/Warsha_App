package com.example.warshaapp.screens.client.home

import androidx.lifecycle.ViewModel
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.getAllCrafts.GetAllCrafts
import com.example.warshaapp.repository.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientHomeViewModel @Inject constructor(private val sharedRepository: SharedRepository) :
    ViewModel() {
    suspend fun getAllCrafts(token: String): WrapperClass<GetAllCrafts, Boolean, Exception> {
        return sharedRepository.getAllCrafts(authorization = "Bearer $token")
    }
}