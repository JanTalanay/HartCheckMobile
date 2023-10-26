package com.example.hartcheck.Data

data class ChangePassword(
    val email: String? = null,
    val otp: String? = null,
    val otpHash: String? = null,
    val newPassword: String? = null
)
