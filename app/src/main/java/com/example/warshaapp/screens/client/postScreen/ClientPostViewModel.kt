package com.example.warshaapp.screens.client.postScreen

import androidx.lifecycle.ViewModel
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.client.createOrder.CreateOrder
import com.example.warshaapp.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ClientPostViewModel @Inject constructor(private val clientRepository: ClientRepository) :
    ViewModel() {

    suspend fun createOrder(
        image: MultipartBody.Part,
        title: RequestBody,
        description: RequestBody,
        orderDifficulty: RequestBody,
        token: String,
        craftId: String
    ): WrapperClass<CreateOrder, Boolean, Exception> {
        return clientRepository.createOrder(
            image = image,
            title = title,
            description = description,
            orderDifficulty = orderDifficulty,
            authorization = "Bearer $token",
            craftId = craftId
        )
    }

}