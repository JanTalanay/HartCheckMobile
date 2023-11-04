package com.example.hartcheck.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DocData(
    val doctorSchedID: Int? = null,
    val doctorID: Int? = null,
    val name: String? = null,
    val appointmentDate: String? = null,

): Parcelable
