package com.example.hartcheck.Wrapper

import android.os.Parcelable
import com.example.hartcheck.Model.DoctorSchedule
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoctorScheduleDates(
    @SerializedName("\$values")
    val DoctorDates: List<DoctorSchedule>
): Parcelable
