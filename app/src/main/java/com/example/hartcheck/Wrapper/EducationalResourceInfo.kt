package com.example.hartcheck.Wrapper

import android.os.Parcelable
import com.example.hartcheck.Model.EducationalResource
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EducationalResourceInfo(
    @SerializedName("\$values")
    val EducResource: List<EducationalResource>

):Parcelable
