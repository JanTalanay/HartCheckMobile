package com.example.hartcheck.Wrapper

import android.os.Parcelable
import com.example.hartcheck.Data.DoctorInfo
import com.example.hartcheck.Model.DoctorSchedule
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoctorInfoList(
    @SerializedName("\$values")
    val DoctorInfo: List<DoctorInfo>
):Parcelable
