package com.example.hartcheck.Wrapper

import com.example.hartcheck.Model.BloodPressure
import com.google.gson.annotations.SerializedName

data class PrevBloodPressure(
    @SerializedName("\$values")
    val PrevBP: List<BloodPressure>
)
