package com.example.warshaapp.repository

import android.util.Log
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.client.createOrder.CreateOrder
import com.example.warshaapp.model.client.getMyOrder.GetMyOrder
import com.example.warshaapp.model.client.offerOfAnOrder.GetOfferOfAnOrder
import com.example.warshaapp.model.client.updateOrder.UpdateOrder
import com.example.warshaapp.model.shared.delete.Delete
import com.example.warshaapp.network.WarshaApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

class ClientRepository @Inject constructor(private val api: WarshaApi) {
    private val createNewOrder: WrapperClass<CreateOrder, Boolean, Exception> = WrapperClass()
    private val getMyOrder: WrapperClass<GetMyOrder, Boolean, Exception> = WrapperClass()
    private val delete = WrapperClass<Delete, Boolean, Exception>()
    private val getOfferOfAnCraft = WrapperClass<GetOfferOfAnOrder, Boolean, Exception>()
    private val updateOrder = WrapperClass<UpdateOrder, Boolean, Exception>()


    suspend fun createOrder(
        image: MultipartBody.Part,
        title: RequestBody,
        description: RequestBody,
        orderDifficulty: RequestBody,
        authorization: String,
        craftId: String
    ): WrapperClass<CreateOrder, Boolean, Exception> {
        try {
            createNewOrder.data = api.createOrder(
                image = image,
                title = title,
                description = description,
                orderDifficulty = orderDifficulty,
                authorization = authorization,
                craftId = craftId
            )
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            createNewOrder.data = CreateOrder(status = status, message = message)

        } catch (e: Exception) {
            Log.d("TAG", "createOrder: $e")
            createNewOrder.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "createOrder: $e")
            createNewOrder.e = e

        }
        return createNewOrder
    }

    suspend fun getMyOrder(
        authorization: String,
        craftId: String,
    ): WrapperClass<GetMyOrder, Boolean, Exception> {
        try {
            getMyOrder.data = api.getMyOrder(authorization = authorization, craftId = craftId)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            getMyOrder.data = GetMyOrder(status = status, message = message)

        } catch (e: Exception) {
            Log.d("TAG", "getMyOrder: $e")
            getMyOrder.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "getMyOrder: $e")
            getMyOrder.e = e
        }
        return getMyOrder
    }

    suspend fun deleteOrder(
        authorization: String,
        craftId: String,
        orderId: String,
    ): WrapperClass<Delete, Boolean, Exception> {
        try {
            delete.data = api.deleteOrder(
                craftId = craftId,
                orderId = orderId,
                authorization = authorization
            )
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            delete.data = Delete(status = status, message = message)
        } catch (e: NullPointerException) {
            delete.data = Delete(status = "success")
        } catch (e: Exception) {
            //addNewUser.loading = false
            Log.d("TAG", "deleteOrder: $e")
            delete.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "deleteOrder: $e")
            delete.e = e
        }
        return delete
    }


    suspend fun getOfferOfAnOrder(
        authorization: String,
        orderId: String,
    ): WrapperClass<GetOfferOfAnOrder, Boolean, Exception> {
        try {
            getOfferOfAnCraft.data =
                api.getOfferOfAnOrder(authorization = authorization, orderId = orderId)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            getOfferOfAnCraft.data = GetOfferOfAnOrder(status = status, message = message)

        } catch (e: Exception) {
            Log.d("TAG", "getOfferOfAnCraft: $e")
            getOfferOfAnCraft.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "getOfferOfAnCraft: $e")
            getOfferOfAnCraft.e = e
        }
        return getOfferOfAnCraft
    }

    suspend fun updateOrder(
        authorization: String,
        orderId: String,
        craftId: String,
        updateOrderBody: Map<String, String>
    ): WrapperClass<UpdateOrder, Boolean, Exception> {
        try {
            updateOrder.data =
                api.updateOrder(
                    craftId = craftId,
                    orderId = orderId,
                    authorization = authorization,
                    updateOrderBody = updateOrderBody
                )
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            updateOrder.data = UpdateOrder(status = status, message = message)

        } catch (e: Exception) {
            Log.d("TAG", "updateOrder: $e")
            updateOrder.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "updateOrder: $e")
            updateOrder.e = e
        }
        return updateOrder
    }

}