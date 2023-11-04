package com.example.hartcheck.Model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
):Parcelable
