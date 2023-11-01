package com.example.hartcheck.Data

import com.example.hartcheck.Wrapper.DoctorScheduleWrapper
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DoctorScheduleResponse(
    @SerializedName("\$id")
    @Expose
    val id: String,

    @SerializedName("\$values")
    @Expose
    val doctorSchedules: Map<String, DoctorScheduleWrapper>
)
