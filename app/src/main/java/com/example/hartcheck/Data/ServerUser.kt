package com.example.hartcheck.Data

data class ServerUser(
    val usersID: Int,
    val email: String,
    val password: String,
    val otpHash: String
)
