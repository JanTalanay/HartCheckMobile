package com.example.hartcheck.Wrapper

import android.os.Parcelable
import com.example.hartcheck.Model.Condition
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ConditionsList(
    @SerializedName("\$values")
    val ConditionsInfo: List<Condition>
): Parcelable
