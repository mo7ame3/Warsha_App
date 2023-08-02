package com.example.warshaapp.model.client.getMyOrder

data class GetMyOrder(
    val data: List<Data>? = null,
    val status: String,
    val message: String? = null,
)
