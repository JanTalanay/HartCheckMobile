package com.example.hartcheck.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Diagnosis(
    var diagnosisID: Int,
    var consultationID: Int,
    var diagnosis: String
): Parcelable
