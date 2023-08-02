package com.example.warshaapp.model.client.offerOfAnOrder

import com.example.warshaapp.model.shared.order.Orders
import com.example.warshaapp.model.shared.user.User

data class Data(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val id: String,
    val order: Orders,
    val status: String,
    val text: String? = null,
    val updatedAt: String,
    val worker: User
)
