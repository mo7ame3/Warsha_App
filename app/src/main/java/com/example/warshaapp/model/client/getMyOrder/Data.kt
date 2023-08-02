package com.example.warshaapp.model.client.getMyOrder

import com.example.warshaapp.model.shared.user.User

data class Data(
    val __v: Int,
    val _id: String,
    val avatar: String,
    val cloudinary_id: String,
    val status: String,
    val craft: String,
    val createdAt: String,
    val createdDate: String,
    val description: String,
    val offers: List<Any>,
    val orderDifficulty: String,
    val orderDone: Boolean,
    val notDoneNotDeleteOrder: Boolean? = null,
    val orderHavingOffers: Boolean,
    val orderedTime: String,
    val title: String,
    val updatedAt: String,
    val user: User
)
