package com.example.hartcheck.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BMIType(
    @SerializedName("BMITypeID")
    @Expose
    val BMITypeID: Int? = null,

    @SerializedName("BMITypeName")
    @Expose
    val BMITypeName: String? = null,

    )