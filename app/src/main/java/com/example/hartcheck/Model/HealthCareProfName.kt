package com.example.hartcheck.Model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HealthCareProfName(
    @SerializedName("firstName")
    @Expose
    val firstName: String,

    @SerializedName("lastName")
    @Expose
    val lastName: String
): Parcelable

