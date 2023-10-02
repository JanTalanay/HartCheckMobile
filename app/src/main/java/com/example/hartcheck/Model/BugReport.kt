package com.example.hartcheck.Model

data class BugReport(
    var bugID: Int? = null,
    var usersID: Int? = null,
    var featureID: Int? = null,
    var description: String? = null,
    var user: Users
)
