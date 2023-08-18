package com.example.warshaapp.model.admin.updateCraft

data class CraftUpdate(
    val __v: Int,
    val _id: String,
    val avatar: String,
    val cloudinary_id: String,
    val createdAt: String,
    val id: String,
    val name: String,
    val orders: List<String>,
    val updatedAt: String,
    val workers: List<Any>
)
