package com.example.hartcheck.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PatientsDoctor(
    @SerializedName("patientDoctorID")
    @Expose
    var patientDoctorID: Int? = null,

    @SerializedName("patientID")
    @Expose
    var patientID: Int? = null,

    @SerializedName("doctorID")
    @Expose
    var doctorID: Int? = null
)
