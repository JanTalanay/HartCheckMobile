package com.example.hartcheck.Data

data class ForgotPassword(
    val email: String,
    var otpHash: String? = null
)
