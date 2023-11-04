package com.example.hartcheck.Data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoctorInfo(
    @SerializedName("doctorID")
    @Expose
    val doctorID: Int? = null,

    @SerializedName("firstName")
    @Expose
    val firstName: String? = null,

    @SerializedName("lastName")
    @Expose
    val lastName: String? = null,
): Parcelable
