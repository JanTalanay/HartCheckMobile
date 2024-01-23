package com.example.hartcheck.Wrapper

import android.os.Parcelable
import com.example.hartcheck.Model.Medicine
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MedicineList(
    @SerializedName("\$values")
    val MedicineInfo: List<Medicine>
):Parcelable
