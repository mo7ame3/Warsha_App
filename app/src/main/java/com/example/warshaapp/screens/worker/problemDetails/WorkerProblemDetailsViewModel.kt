package com.example.warshaapp.screens.worker.problemDetails

import androidx.lifecycle.ViewModel
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.worker.createOffer.CreateOffer
import com.example.warshaapp.model.worker.orderDetails.GetOrderDetails
import com.example.warshaapp.repository.WorkerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkerProblemDetailsViewModel @Inject constructor(private val workerRepository: WorkerRepository) :
    ViewModel() {


    suspend fun getOrderDetails(
        craftId: String,
        authorization: String,
        orderId: String
    ): WrapperClass<GetOrderDetails, Boolean, Exception> {
        return workerRepository.getOrderDetails(
            craftId = craftId,
            orderId = orderId,
            authorization = "Bearer $authorization"
        )
    }


    suspend fun createOffer(
        authorization: String,
        offerDetails: String? = null,
        orderId: String
    ): WrapperClass<CreateOffer, Boolean, Exception> {
        return workerRepository.createOffer(
            authorization = "Bearer $authorization",
            offerBody = if (offerDetails == null) mapOf("" to "")
            else mapOf(
                "text" to offerDetails,
            ),
            orderId = orderId
        )
    }


}
