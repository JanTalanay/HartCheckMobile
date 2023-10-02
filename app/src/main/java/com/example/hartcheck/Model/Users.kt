package com.example.hartcheck.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.DateFormat
import java.util.Date

data class Users(
    @SerializedName("usersID")
    @Expose
    val usersID: Int? = null,

    @SerializedName("email")
    @Expose
    val email: String? = null,

    @SerializedName("firstName")
    @Expose
    val firstName: String? = null,

    @SerializedName("lastName")
    @Expose
    val lastName: String? = null,

    @SerializedName("password")
    @Expose
    val password: String? = null,

//    @SerializedName("birthdate")
//    @Expose
//    val birthdate: Date? = null,

    @SerializedName("gender")
    @Expose
    val gender: Int? = null,
    @SerializedName("phoneNumber")
    @Expose
    val phoneNumber: Long? = null,

    @SerializedName("role")
    @Expose
    val role: Int? = null
)
