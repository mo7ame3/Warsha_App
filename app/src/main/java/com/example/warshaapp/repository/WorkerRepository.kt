package com.example.warshaapp.repository

import android.util.Log
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.worker.createOffer.CreateOffer
import com.example.warshaapp.model.worker.getMyOffer.GetMyOffer
import com.example.warshaapp.model.worker.home.WorkerHome
import com.example.warshaapp.model.worker.orderDetails.GetOrderDetails
import com.example.warshaapp.network.WarshaApi
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

class WorkerRepository @Inject constructor(private val api: WarshaApi) {
    private val getHome = WrapperClass<WorkerHome, Boolean, Exception>()
    private val getMyOffer = WrapperClass<GetMyOffer, Boolean, Exception>()
    private val getOrderDetails = WrapperClass<GetOrderDetails, Boolean, Exception>()
    private val createOffer = WrapperClass<CreateOffer, Boolean, Exception>()
    suspend fun getHome(
        craftId: String,
        authorization: String
    ): WrapperClass<WorkerHome, Boolean, Exception> {

        try {
            getHome.data = api.getWorkerHome(craftId, authorization)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            Log.d("TAG", "getHomeWorker: $message")
            getHome.data = WorkerHome(status = status, message = message)

        } catch (e: SocketTimeoutException) {
            getHome.e = e
            Log.d("TAG", "getHomeWorker: $e")

        } catch (e: Exception) {
            getHome.e = e
            Log.d("TAG", "getHomeWorker: $e")
        }

        return getHome
    }

    suspend fun getMyOffer(
        authorization: String,
        userId: String
    ): WrapperClass<GetMyOffer, Boolean, Exception> {
        try {
            getMyOffer.data = api.getMyOffer(authorization = authorization, userId = userId)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            Log.d("TAG", "getMyOffer: $message")
            getMyOffer.data = GetMyOffer(status = status, message = message)

        } catch (e: SocketTimeoutException) {
            getMyOffer.e = e
            Log.d("TAG", "getMyOffer: $e")

        } catch (e: Exception) {
            getHome.e = e
            Log.d("TAG", "getMyOffer: $e")
        }
        return getMyOffer
    }

    suspend fun getOrderDetails(
        craftId: String, authorization: String,
        orderId: String
    ): WrapperClass<GetOrderDetails, Boolean, Exception> {
        try {
            getOrderDetails.data = api.getOrderDetails(
                craftId = craftId,
                authorization = authorization,
                orderId = orderId
            )
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            Log.d("TAG", "getOrderDetails: $message")
            getOrderDetails.data = GetOrderDetails(status = status, message = message)

        } catch (e: SocketTimeoutException) {
            getOrderDetails.e = e
            Log.d("TAG", "getOrderDetails: $e")

        } catch (e: Exception) {
            getHome.e = e
            Log.d("TAG", "getOrderDetails: $e")
        }

        return getOrderDetails
    }


    suspend fun createOffer(
        authorization: String,
        orderId: String,
        offerBody: Map<String, String>
    ): WrapperClass<CreateOffer, Boolean, Exception> {
        try {
            createOffer.data = api.createOffer(
                authorization = authorization,
                offerBody = offerBody,
                orderId = orderId
            )
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            Log.d("TAG", "createOffer: $message")
            createOffer.data = CreateOffer(status = status, message = message)

        } catch (e: SocketTimeoutException) {
            createOffer.e = e
            Log.d("TAG", "createOffer: $e")

        } catch (e: Exception) {
            getHome.e = e
            Log.d("TAG", "createOffer: $e")
        }

        return createOffer
    }

}