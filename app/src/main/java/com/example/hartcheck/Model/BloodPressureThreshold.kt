package com.example.hartcheck.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BloodPressureThreshold(
    @SerializedName("thresholdID")
    @Expose
    val thresholdID: Int,

    @SerializedName("patientID")
    @Expose
    val patientID: Int,

    @SerializedName("doctorID")
    @Expose
    val doctorID: Int,

    @SerializedName("systolicLevel")
    @Expose
    val systolicLevel: Int,

    @SerializedName("diastolicLevel")
    @Expose
    val diastolicLevel: Int
)
