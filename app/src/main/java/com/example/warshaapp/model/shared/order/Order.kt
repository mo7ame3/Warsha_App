package com.example.warshaapp.model.shared.order

data class Order(
    val __v: Int,
    val _id: String,
    val avatar: String,
    val cloudinary_id: String,
    val craft: String,
    val createdAt: String,
    val description: String,
    val notDoneNotDeleteOrder: Boolean,
    val offers: List<Any>,
    val orderDifficulty: String,
    val orderDone: Boolean,
    val orderHavingOffers: Boolean,
    val title: String,
    val updatedAt: String,
    val user: String
)
