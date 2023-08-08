package com.example.warshaapp.repository

import android.util.Log
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.authentication.AuthenticationCraftList
import com.example.warshaapp.model.shared.getAllCrafts.GetAllCrafts
import com.example.warshaapp.model.shared.getCraftOfWorker.GetCraftOfWorker
import com.example.warshaapp.model.shared.profile.GetProfile
import com.example.warshaapp.model.shared.updateOffer.UpdateOffer
import com.example.warshaapp.model.shared.updatePassword.UpdatePassword
import com.example.warshaapp.model.shared.updateProfile.UpdateProfile
import com.example.warshaapp.network.WarshaApi
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

class SharedRepository @Inject constructor(private val api: WarshaApi) {
    private val getCraftList = WrapperClass<AuthenticationCraftList, Boolean, Exception>()
    private val getCraftOfWorker = WrapperClass<GetCraftOfWorker, Boolean, Exception>()
    private val getAllCrafts = WrapperClass<GetAllCrafts, Boolean, Exception>()
    private val updateOffer = WrapperClass<UpdateOffer, Boolean, Exception>()
    private val getProfile = WrapperClass<GetProfile, Boolean, Exception>()
    private val updateProfilePhoto = WrapperClass<UpdateProfile, Boolean, Exception>()
    private val updateProfileData = WrapperClass<UpdateProfile, Boolean, Exception>()
    private val updatePassword = WrapperClass<UpdatePassword, Boolean, Exception>()


    suspend fun getCraftList()
            : WrapperClass<AuthenticationCraftList, Boolean, Exception> {
        try {
            getCraftList.data = api.getCraftList()
        } catch (e: HttpException) {
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
            Log.d("TAG", "getAllCrafts: $e")
            getAllCrafts.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "getAllCrafts: $e")
            getAllCrafts.e = e

        }
        return getAllCrafts
    }

    suspend fun updateOffer(offerId: String, authorization: String, updateBody: Map<String, String>)
            : WrapperClass<UpdateOffer, Boolean, Exception> {
        try {
            updateOffer.data = api.updateOffer(
                offerId = offerId,
                authorization = authorization,
                updateBody = updateBody
            )
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            updateOffer.data = UpdateOffer(status = status, message = message)
        } catch (e: Exception) {
            Log.d("TAG", "updateOffer: $e")
            updateOffer.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "updateOffer: $e")
            updateOffer.e = e
        }
        return updateOffer
    }

    suspend fun getProfile(userId: String, authorization: String)
            : WrapperClass<GetProfile, Boolean, Exception> {
        try {
            getProfile.data = api.getProfile(userId = userId, authorization = authorization)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            getProfile.data = GetProfile(status = status, message = message)
        } catch (e: Exception) {
            Log.d("TAG", "getProfile: $e")
            getProfile.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "getProfile: $e")
            getProfile.e = e
        }
        return getProfile
    }

    suspend fun updateProfilePhoto(
        userId: String,
        authorization: String,
        image: MultipartBody.Part
    ): WrapperClass<UpdateProfile, Boolean, Exception> {
        try {
            updateProfilePhoto.data = api.updateProfilePhoto(
                userId = userId,
                authorization = authorization,
                image = image
            )
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            updateProfilePhoto.data = UpdateProfile(status = status, message = message)
        } catch (e: Exception) {
            Log.d("TAG", "updateProfilePhoto: $e")
            updateProfilePhoto.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "updateProfilePhoto: $e")
            updateProfilePhoto.e = e
        }
        return updateProfilePhoto
    }

    suspend fun updateProfileData(
        userId: String,
        authorization: String,
        updateProfileBody: Map<String, String>
    ): WrapperClass<UpdateProfile, Boolean, Exception> {
        try {
            updateProfileData.data = api.updateProfileData(
                userId = userId,
                authorization = authorization,
                updateProfileBody = updateProfileBody
            )
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            updateProfileData.data = UpdateProfile(status = status, message = message)
        } catch (e: Exception) {
            Log.d("TAG", "updateProfileData: $e")
            updateProfileData.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "updateProfileData: $e")
            updateProfileData.e = e
        }
        return updateProfileData
    }


    suspend fun updatePassword(
        authorization: String,
        updatePasswordBody: Map<String, String>
    ): WrapperClass<UpdatePassword, Boolean, Exception> {
        try {
            updatePassword.data = api.updatePassword(
                authorization = authorization,
                updatePasswordBody = updatePasswordBody
            )
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val status = error!!.split("status")[1].split(":")[1].split("\"")[1]
            val message = error.split("message")[1].split("\":")[1]
            updatePassword.data = UpdatePassword(status = status, message = message)
        } catch (e: Exception) {
            Log.d("TAG", "updatePassword: $e")
            updatePassword.e = e
        } catch (e: SocketTimeoutException) {
            Log.d("TAG", "updatePassword: $e")
            updatePassword.e = e
        }
        return updatePassword
    }

}