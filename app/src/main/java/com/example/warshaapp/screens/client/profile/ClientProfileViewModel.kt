package com.example.warshaapp.screens.client.profile

import androidx.lifecycle.ViewModel
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.profile.GetProfile
import com.example.warshaapp.model.shared.updateProfile.UpdateProfile
import com.example.warshaapp.repository.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ClientProfileViewModel @Inject constructor(
    private val sharedRepository: SharedRepository
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

}