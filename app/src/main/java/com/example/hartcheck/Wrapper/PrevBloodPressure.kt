package com.example.hartcheck.Wrapper

import android.os.Parcelable
import com.example.hartcheck.Model.BloodPressure
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PrevBloodPressure(
    @SerializedName("\$values")
    val PrevBP: List<BloodPressure>
): Parcelable
