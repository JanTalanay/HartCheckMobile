package com.example.hartcheck.Wrapper

import android.os.Parcelable
import com.example.hartcheck.Model.PatientsDoctor
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PatientsDoctorList(
    @SerializedName("\$values")
    val PatientsDoctor: List<PatientsDoctor>
):Parcelable
