package com.example.hartcheck.Model

data class PreviousMedication(
    var prevMedID: Int? = null,
    var patientID: Int? = null,
    var previousMed: String? = null,
    var dosage: Float? = null,
    var date: String? = null
)
