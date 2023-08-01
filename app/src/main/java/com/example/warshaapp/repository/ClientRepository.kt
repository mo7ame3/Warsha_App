package com.example.warshaapp.repository

import android.util.Log
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.client.createOrder.CreateOrder
import com.example.warshaapp.network.WarshaApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

class ClientRepository @Inject constructor(private val api: WarshaApi) {
    private val createNewOrder: WrapperClass<CreateOrder, Boolean, Exception> = WrapperClass()


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
            //addNewUser.loading = false
            Log.d("TAG", "createOrder: $e")
            createNewOrder.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "createOrder: $e")
            createNewOrder.e = e

        }
        return createNewOrder
    }

}