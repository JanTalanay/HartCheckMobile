package com.example.hartcheck.Wrapper

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.example.hartcheck.Model.DoctorSchedule
import com.google.gson.annotations.SerializedName
@Parcelize
data class ConsultationAssign(
    @SerializedName("\$values")
    val ConsultationDates: List<DoctorSchedule>
): Parcelable
