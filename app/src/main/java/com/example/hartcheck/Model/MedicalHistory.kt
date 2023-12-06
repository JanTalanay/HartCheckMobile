package com.example.hartcheck.Model

data class MedicalHistory(
    var medicalHistoryID: Int? = null,
    var patientID: Int? = null,
    var medicalHistory: String? = null,
    var pastSurgicalHistory: String? = null,
    var date: String? = null
)
