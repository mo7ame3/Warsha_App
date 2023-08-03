package com.example.warshaapp.data

data class MyCraftOrderData(
    val clientName: String? = null,
    val problemTitle: String,
    val problemType: String,
    val problemState: String? = null,
    val address: String? = null,
    //client profile ...complete Projects
    val workerName: String? = null,
    val workerRate: Int? = null,
)
