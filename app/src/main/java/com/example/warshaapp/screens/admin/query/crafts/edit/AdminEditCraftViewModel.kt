package com.example.warshaapp.screens.admin.query.crafts.edit

import androidx.lifecycle.ViewModel
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.admin.updateCraft.UpdateCraft
import com.example.warshaapp.model.shared.delete.Delete
import com.example.warshaapp.model.shared.getCraft.GetCraft
import com.example.warshaapp.repository.AdminRepository
import com.example.warshaapp.repository.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AdminEditCraftViewModel @Inject constructor(
    private val sharedRepository: SharedRepository,
    private val adminRepository: AdminRepository
) : ViewModel() {
    suspend fun getOneCraft(
        authorization: String,
        craftId: String
    ): WrapperClass<GetCraft, Boolean, Exception> {
        return sharedRepository.getOneCraft(
            authorization = "Bearer $authorization",
            craftId = craftId
        )
    }


    suspend fun updateCraft(
        authorization: String,
        name: RequestBody? = null,
        image: MultipartBody.Part? = null,
        craftId: String
    ): WrapperClass<UpdateCraft, Boolean, Exception> {
        return adminRepository.updateCraft(
            authorization = "Bearer $authorization",
            name = name,
            image = image,
            craftId = craftId
        )
    }

    suspend fun deleteCraft(
        authorization: String,
        craftId: String
    ): WrapperClass<Delete, Boolean, Exception> {
        return adminRepository.deleteCraft(
            authorization = "Bearer $authorization",
            craftId = craftId
        )
    }

}
