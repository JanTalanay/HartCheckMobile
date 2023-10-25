package com.example.hartcheck.Model

data class BloodPressure(
    var bloodPressureID: Int? = null,
    var patientID: Int? = null,
    var systolic: Float? = null,
    var diastolic: Float? = null,
    var dateTaken: String? = null,
)
