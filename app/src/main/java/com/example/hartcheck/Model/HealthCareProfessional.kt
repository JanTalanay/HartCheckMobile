package com.example.hartcheck.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HealthCareProfessional(
    @SerializedName("doctorID")
    @Expose
    val doctorID: Int? = null,

    @SerializedName("usersID")
    @Expose
    val usersID: Int? = null,

    @SerializedName("clinic")
    @Expose
    val clinic: String? = null,

    @SerializedName("licenseID")
    @Expose
    val licenseID: Int? = null,

    @SerializedName("verification")
    @Expose
    val verification: Int? = null

)
