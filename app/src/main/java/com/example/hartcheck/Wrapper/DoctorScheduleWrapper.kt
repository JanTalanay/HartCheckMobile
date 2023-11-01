package com.example.hartcheck.Wrapper

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DoctorScheduleWrapper(
    @SerializedName("\$id")
    @Expose
    val id: String,

    @SerializedName("\$values")
    @Expose
    val values: List<String>
)
