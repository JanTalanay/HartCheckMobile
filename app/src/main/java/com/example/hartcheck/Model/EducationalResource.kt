package com.example.hartcheck.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EducationalResource(
    @SerializedName("usersID")
    @Expose
    val usersID: Int? = null,
)
