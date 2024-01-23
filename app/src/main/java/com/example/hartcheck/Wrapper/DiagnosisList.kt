package com.example.hartcheck.Wrapper

import android.os.Parcelable
import com.example.hartcheck.Model.Diagnosis
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DiagnosisList(
    @SerializedName("\$values")
    val DiagnosisInfo: List<Diagnosis>
): Parcelable
