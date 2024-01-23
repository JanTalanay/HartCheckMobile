package com.example.hartcheck.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Medicine(
    var medicineID: Int? = null,
    var consultationID: Int? = null,
    var medicine: String? = null,
    var dateTime: String? = null,
    var dosage: Float? = null
): Parcelable
