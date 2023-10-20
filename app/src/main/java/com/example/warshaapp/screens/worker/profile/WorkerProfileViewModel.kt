package com.example.warshaapp.screens.worker.profile

import androidx.lifecycle.ViewModel
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.profile.GetProfile
import com.example.warshaapp.model.shared.updateProfile.UpdateProfile
import com.example.warshaapp.model.worker.getMyOffer.GetMyOffer
import com.example.warshaapp.repository.SharedRepository
import com.example.warshaapp.repository.WorkerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class WorkerProfileViewModel @Inject constructor(
    private val sharedRepository: SharedRepository,
    private val workerRepository: WorkerRepository
) : ViewModel() {

    suspend fun getProfile(
        userId: String,
        authorization: String
    ): WrapperClass<GetProfile, Boolean, Exception> {
        return sharedRepository.getProfile(userId = userId, authorization = "Bearer $authorization")
    }


    suspend fun updateProfilePhoto(
        userId: String,
        authorization: String,
        image: MultipartBody.Part
    ): WrapperClass<UpdateProfile, Boolean, Exception> {
        return sharedRepository.updateProfilePhoto(
            userId = userId,
            authorization = "Bearer $authorization",
            image = image
        )
    }


    suspend fun getMyCompletedOffer(
        authorization: String,
        userId: String
    ): WrapperClass<GetMyOffer, Boolean, Exception> {
        return workerRepository.getMyOffer(authorization = "Bearer $authorization", userId = userId)
    }

}