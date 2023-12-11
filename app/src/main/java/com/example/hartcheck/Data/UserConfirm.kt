package com.example.hartcheck.Data

data class UserConfirm(
    val email: String,
    var otpHash: String? = null
)
