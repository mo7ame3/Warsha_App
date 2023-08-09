package com.example.warshaapp.screens.worker.myOffers

import androidx.lifecycle.ViewModel
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.worker.getMyOffer.GetMyOffer
import com.example.warshaapp.model.worker.orderDetails.GetOrderDetails
import com.example.warshaapp.repository.WorkerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyOffersViewModel @Inject constructor(private val workerRepository: WorkerRepository) :
    ViewModel() {
    suspend fun getMyOffers(
        authorization: String,
        userId: String
    ): WrapperClass<GetMyOffer, Boolean, Exception> {
        return workerRepository.getMyOffer(authorization = "Bearer $authorization", userId = userId)
    }

    suspend fun getOrderDetails(
        authorization: String,
        orderId: String,
        craftId: String
    ): WrapperClass<GetOrderDetails, Boolean, Exception> {
        return workerRepository.getOrderDetails(
            authorization = "Bearer $authorization",
            orderId = orderId,
            craftId = craftId
        )
    }

}