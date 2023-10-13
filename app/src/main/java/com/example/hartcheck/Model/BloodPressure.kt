package com.example.hartcheck.Model

data class BloodPressure(
    var bloodPressureID: Int,
    var patientID: Int,
    var systolic: Int,
    var diastolic: Int,
    var dateTaken: String?
)
