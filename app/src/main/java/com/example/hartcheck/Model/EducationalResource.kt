package com.example.hartcheck.Model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EducationalResource(
    @SerializedName("resourceID")
    @Expose
    val resourceID: Int,

    @SerializedName("text")
    @Expose
    val text: String,

    @SerializedName("link")
    @Expose
    val link: String
): Parcelable
