package com.example.hartcheck.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
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

    @SerializedName("birthdate")
    @Expose
    val birthdate: String? = null,

    @SerializedName("gender")
    @Expose
    val gender: Int? = null,

    @SerializedName("isPregnant")
    @Expose
    val isPregnant: Boolean? = null,

    @SerializedName("phoneNumber")
    @Expose
    val phoneNumber: Long? = null,

    @SerializedName("role")
    @Expose
    val role: Int? = null,

    var otpHash: String? = null

): Parcelable
