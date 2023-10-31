package com.example.hartcheck.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BloodPressure(
    var bloodPressureID: Int? = null,
    var patientID: Int? = null,
    var systolic: Float? = null,
    var diastolic: Float? = null,
    var dateTaken: String? = null,
): Parcelable
