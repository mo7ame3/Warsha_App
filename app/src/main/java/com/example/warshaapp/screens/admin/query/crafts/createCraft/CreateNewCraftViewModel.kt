package com.example.warshaapp.screens.admin.query.crafts.createCraft

import androidx.lifecycle.ViewModel
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.admin.createNewCraft.CreateNewCraft
import com.example.warshaapp.repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CreateNewCraftViewModel @Inject constructor(private val adminRepository: AdminRepository) :
    ViewModel() {

    suspend fun createNewCraft(
        authorization: String, name: RequestBody, image: MultipartBody.Part
    ): WrapperClass<CreateNewCraft, Boolean, Exception> {
        return adminRepository.createNewCraft(
            authorization = "Bearer $authorization",
            name = name, image = image
        )
    }
}