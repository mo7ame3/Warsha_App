package com.example.warshaapp.screens.sharedScreens.setting

import androidx.lifecycle.ViewModel
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.profile.GetProfile
import com.example.warshaapp.model.shared.updatePassword.UpdatePassword
import com.example.warshaapp.model.shared.updateProfile.UpdateProfile
import com.example.warshaapp.repository.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val sharedRepository: SharedRepository) :
    ViewModel() {
    suspend fun getProfile(
        userId: String,
        authorization: String
    ): WrapperClass<GetProfile, Boolean, Exception> {
        return sharedRepository.getProfile(userId = userId, authorization = "Bearer $authorization")
    }

    suspend fun updateProfileSetting(
        userId: String,
        authorization: String,
        name: String,
        address: String,
        bio: String
    ): WrapperClass<UpdateProfile, Boolean, Exception> {
        return sharedRepository.updateProfileData(
            userId = userId,
            authorization = "Bearer $authorization",
            updateProfileBody = mapOf(
                "name" to name,
                "address" to address,
                "bio" to bio
            )
        )
    }

    suspend fun updatePassword(
        authorization: String,
        oldPassword: String,
        password: String,
        passwordConfirm: String
    ): WrapperClass<UpdatePassword, Boolean, Exception> {
        return sharedRepository.updatePassword(
            authorization = "Bearer $authorization",
            updatePasswordBody = mapOf(
                "passwordCurrent" to oldPassword,
                "password" to password,
                "passwordConfirm" to passwordConfirm
            )
        )
    }

}