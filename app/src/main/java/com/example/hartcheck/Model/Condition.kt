package com.example.hartcheck.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Condition(
    var conditionID: Int,
    var consultationID: Int,
    var condition: String
):Parcelable
