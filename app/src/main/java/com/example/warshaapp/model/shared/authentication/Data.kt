package com.example.warshaapp.model.shared.authentication

import com.example.warshaapp.model.shared.craft.Craft
import com.example.warshaapp.model.shared.user.User


data class Data(
    val user: User,
    val crafts: List<Craft>
)