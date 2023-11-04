package com.example.hartcheck.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BloodPressureThreshold(
    @SerializedName("thresholdID")
    @Expose
    val thresholdID: Int? = null,

    @SerializedName("patientID")
    @Expose
    val patientID: Int? = null,

    @SerializedName("doctorID")
    @Expose
    val doctorID: Int? = null,

    @SerializedName("systolicLevel")
    @Expose
    val systolicLevel: Float,

    @SerializedName("diastolicLevel")
    @Expose
    val diastolicLevel: Float
)
