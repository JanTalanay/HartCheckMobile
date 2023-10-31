package com.example.hartcheck.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DoctorSchedule(
    @SerializedName("doctorSchedID")
    @Expose
    val doctorSchedID: Int,

    @SerializedName("doctorID")
    @Expose
    val doctorID: Int,

    @SerializedName("schedDateTime")
    @Expose
    val schedDateTime: String
)
