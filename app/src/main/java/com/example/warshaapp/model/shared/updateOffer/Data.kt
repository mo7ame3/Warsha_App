package com.example.warshaapp.model.shared.updateOffer

import com.example.warshaapp.model.shared.order.Orders
import com.example.warshaapp.model.shared.user.User

data class Data(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val id: String,
    val order: Orders,
    val status: String,
    val text: String,
    val updatedAt: String,
    val createAt: String,
    val worker: User
)
