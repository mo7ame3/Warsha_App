package com.example.warshaapp.model.shared.authentication

import com.example.warshaapp.model.shared.user.User

data class AuthenticationCraft(
    val status: String,
    val user: User? = null
)
