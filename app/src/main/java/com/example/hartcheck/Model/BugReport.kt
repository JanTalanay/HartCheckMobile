package com.example.hartcheck.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BugReport(
    var bugID: Int? = null,
    var usersID: Int? = null,
    var featureID: Int? = null,
    var description: String? = null,
//    var user: Users
)
