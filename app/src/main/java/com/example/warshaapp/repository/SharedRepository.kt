package com.example.warshaapp.repository

import android.util.Log
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.authentication.AuthenticationCraftList
import com.example.warshaapp.model.shared.getAllCrafts.GetAllCrafts
import com.example.warshaapp.model.shared.getCraftOfWorker.GetCraftOfWorker
import com.example.warshaapp.network.WarshaApi
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

class SharedRepository @Inject constructor(private val api: WarshaApi) {
    private val getCraftList = WrapperClass<AuthenticationCraftList, Boolean, Exception>()
    private val getCraftOfWorker = WrapperClass<GetCraftOfWorker, Boolean, Exception>()
    private val getAllCrafts = WrapperClass<GetAllCrafts, Boolean, Exception>()

    suspend fun getCraftList()
            : WrapperClass<AuthenticationCraftList, Boolean, Exception> {
        try {
            getCraftList.data = api.getCraftList()
        } catch (e: HttpException) {
            //addNewUser.loading = true
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            getCraftList.data = AuthenticationCraftList(status = status, message = message)
        } catch (e: Exception) {
            Log.d("TAG", "getCraftList: $e")
            getCraftList.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "getCraftList: $e")
            getCraftList.e = e
        }
        return getCraftList
    }

    suspend fun getCraftOfWorker(workerId: String)
            : WrapperClass<GetCraftOfWorker, Boolean, Exception> {
        try {
            getCraftOfWorker.data = api.getCraftOfWorker(workerId = workerId)
        } catch (e: HttpException) {
            //addNewUser.loading = true
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            getCraftOfWorker.data = GetCraftOfWorker(status = status, message = message)
        } catch (e: Exception) {
            Log.d("TAG", "getCraftOfWorker: $e")
            getCraftOfWorker.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "getCraftOfWorker: $e")
            getCraftOfWorker.e = e
        }
        return getCraftOfWorker
    }

    suspend fun getAllCrafts(authorization: String)
            : WrapperClass<GetAllCrafts, Boolean, Exception> {
        try {
            getAllCrafts.data = api.getAllCrafts(authorization = authorization)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            getAllCrafts.data = GetAllCrafts(status = status, message = message)

        } catch (e: Exception) {
            //addNewUser.loading = false
            Log.d("TAG", "getAllCrafts: $e")
            getAllCrafts.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "getAllCrafts: $e")
            getAllCrafts.e = e

        }
        return getAllCrafts
    }

}