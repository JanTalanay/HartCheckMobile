package com.example.hartcheck.Model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoctorSchedule(
    @SerializedName("doctorSchedID")
    @Expose
    val doctorSchedID: Int? = null,

    @SerializedName("doctorID")
    @Expose
    val doctorID: Int? = null,

    @SerializedName("schedDateTime")
    @Expose
    val schedDateTime: String? = null
): Parcelable
