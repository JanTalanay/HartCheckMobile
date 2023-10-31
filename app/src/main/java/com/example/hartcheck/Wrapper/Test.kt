package com.example.hartcheck.Wrapper

import com.example.hartcheck.Model.HealthCareProfName
import com.google.gson.annotations.SerializedName

data class Test(
    @SerializedName("\$values")
    val HealthCareName: List<HealthCareProfName>
)
