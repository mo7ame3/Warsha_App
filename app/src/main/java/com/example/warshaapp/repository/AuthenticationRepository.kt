package com.example.warshaapp.repository

import android.util.Log
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.authentication.Authentication
import com.example.warshaapp.model.shared.authentication.AuthenticationCraft
import com.example.warshaapp.network.WarshaApi
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(private val api: WarshaApi) {
    private val loginOrRegister = WrapperClass<Authentication, Boolean, Exception>()
    private val workerChooseCraft = WrapperClass<AuthenticationCraft, Boolean, Exception>()

    suspend fun addNewUser(register: Map<String, String>)
            : WrapperClass<Authentication, Boolean, Exception> {
        try {
            loginOrRegister.data = api.register(register)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            loginOrRegister.data = Authentication(status = status, message = message)
        } catch (e: Exception) {
            //addNewUser.loading = false
            Log.d("TAG", "Register: $e")
            loginOrRegister.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "Register: $e")
            loginOrRegister.e = e
        }
        return loginOrRegister
    }

    suspend fun addLoggedInUser(login: Map<String, String>)
            : WrapperClass<Authentication, Boolean, Exception> {
        try {
            loginOrRegister.data = api.login(loginInput = login)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            loginOrRegister.data = Authentication(status = status, message = message)
            Log.d("TAG", "error: ${error}")

        } catch (e: Exception) {
            Log.d("TAG", "addLoggedInUser: $e")
            loginOrRegister.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "addLoggedInUser: $e")
            loginOrRegister.e = e
        }
        return loginOrRegister
    }

    suspend fun workerChooseCraft(workerId: String, myCraft: Map<String, String>, token: String)
            : WrapperClass<AuthenticationCraft, Boolean, Exception> {
        try {
            workerChooseCraft.data =
                api.workerChooseCraft(workerId = workerId, myCraft = myCraft, authorization = token)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            workerChooseCraft.data = AuthenticationCraft(status = status)
        } catch (e: Exception) {
            //addNewUser.loading = false
            Log.d("TAG", "workerChooseCraft: $e")
            workerChooseCraft.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "workerChooseCraft: $e")
            workerChooseCraft.e = e
        }
        return workerChooseCraft
    }
}