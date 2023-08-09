package com.example.warshaapp.model.worker.getMyOffer

import com.example.warshaapp.model.shared.order.Orders
import com.example.warshaapp.model.shared.user.User

data class Data(
    val _id: String,
    val id: String,
    val order: Orders,
    val status: String,
    val text: String,
    val worker: User
)
