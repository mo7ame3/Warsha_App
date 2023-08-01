package com.example.warshaapp.model.shared.craft

import com.example.warshaapp.model.shared.order.Orders

data class Craft(
    val __v: Int? = null,
    val _id: String,
    val avatar: String,
    val cloudinary_id: String? = null,
    val createdAt: String? = null,
    val id: String,
    val name: String,
    val orders: List<Orders>? = null,
    val updatedAt: String? = null,
    val workers: List<Any>? = null
)
