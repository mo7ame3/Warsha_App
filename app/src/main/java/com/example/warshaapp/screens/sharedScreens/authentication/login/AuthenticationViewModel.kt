package com.example.warshaapp.screens.sharedScreens.authentication.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.authentication.Authentication
import com.example.warshaapp.model.shared.authentication.AuthenticationCraft
import com.example.warshaapp.model.shared.authentication.AuthenticationCraftList
import com.example.warshaapp.model.shared.getCraftOfWorker.GetCraftOfWorker
import com.example.warshaapp.repository.AuthenticationRepository
import com.example.warshaapp.repository.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val sharedRepository: SharedRepository
) :
    ViewModel() {
    val craftList: MutableState<WrapperClass<AuthenticationCraftList, Boolean, Exception>> =
        mutableStateOf(
            WrapperClass()
        )

    init {
        getCraftList()
    }

    suspend fun register(
        name: String,
        email: String,
        address: String,
        role: String,
        password: String,
        passwordConfirm: String
    ): WrapperClass<Authentication, Boolean, Exception> {
        return authenticationRepository.addNewUser(
            register = mapOf(
                "name" to name,
                "email" to email,
                "address" to address,
                "role" to role,
                "password" to password,
                "passwordConfirm" to passwordConfirm
            )
        )
    }

    suspend fun login(
        email: String,
        password: String,
    ): WrapperClass<Authentication, Boolean, Exception> {
        return authenticationRepository.addLoggedInUser(
            login = mapOf(
                "email" to email,
                "password" to password,
            )
        )
    }

    private fun getCraftList() {
        viewModelScope.launch {
            craftList.value = sharedRepository
                .getCraftList()
        }
    }

    suspend fun workerChooseCraft(
        token: String,
        myCraft: String,
        workerId: String,
    ): WrapperClass<AuthenticationCraft, Boolean, Exception> {
        return authenticationRepository.workerChooseCraft(
            token = token, myCraft = mapOf(
                "myCraft" to myCraft
            ), workerId = workerId
        )
    }

    suspend fun getCraftOfWorker(
        workerId: String
    ): WrapperClass<GetCraftOfWorker, Boolean, Exception> {
        return sharedRepository.getCraftOfWorker(workerId = workerId)
    }

}