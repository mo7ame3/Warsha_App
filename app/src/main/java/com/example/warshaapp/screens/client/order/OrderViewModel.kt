package com.example.warshaapp.screens.client.order

import androidx.lifecycle.ViewModel
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.client.getMyOrder.GetMyOrder
import com.example.warshaapp.model.client.offerOfAnOrder.GetOfferOfAnOrder
import com.example.warshaapp.model.client.updateOrder.UpdateOrder
import com.example.warshaapp.model.shared.authentication.AuthenticationCraftList
import com.example.warshaapp.model.shared.delete.Delete
import com.example.warshaapp.model.shared.updateOffer.UpdateOffer
import com.example.warshaapp.repository.ClientRepository
import com.example.warshaapp.repository.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val sharedRepository: SharedRepository,
    private val clientRepository: ClientRepository
) : ViewModel() {

    suspend fun getCraftList(): WrapperClass<AuthenticationCraftList, Boolean, Exception> {
        return sharedRepository.getCraftList()

    }

    suspend fun getMyOrder(
        authorization: String,
        craftId: String,
    ): WrapperClass<GetMyOrder, Boolean, Exception> {
        return clientRepository.getMyOrder(
            authorization = "Bearer $authorization",
            craftId = craftId
        )
    }

    suspend fun deleteOrder(
        authorization: String,
        craftId: String,
        orderId: String
    ): WrapperClass<Delete, Boolean, Exception> {
        return clientRepository.deleteOrder(
            authorization = "Bearer $authorization", craftId = craftId, orderId = orderId
        )
    }


    suspend fun getOfferOfAnOrder(
        authorization: String,
        orderId: String
    ): WrapperClass<GetOfferOfAnOrder, Boolean, Exception> {
        return clientRepository.getOfferOfAnOrder(
            authorization = "Bearer $authorization",
            orderId = orderId
        )
    }


    suspend fun updateOffer(
        authorization: String,
        offerId: String,
        text: String,
        status: String
    ): WrapperClass<UpdateOffer, Boolean, Exception> {
        return sharedRepository.updateOffer(
            authorization = "Bearer $authorization",
            offerId = offerId,
            updateBody = mapOf(
                "text" to text,
                "status" to status
            )
        )
    }

    suspend fun updateOrderStatus(
        authorization: String,
        orderId: String,
        craftId: String,
        status: String,
    ): WrapperClass<UpdateOrder, Boolean, Exception> {
        return clientRepository.updateOrder(
            authorization = "Bearer $authorization",
            orderId = orderId,
            craftId = craftId,
            updateOrderBody = mapOf("status" to status)
        )
    }

}