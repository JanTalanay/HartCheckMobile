package com.example.hartcheck.Remote.BugReport

import com.example.hartcheck.Model.BugReport
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface BugReportInterface {
    @POST("api/BugReport")
    fun insertBugReport(@Body request: BugReport): Call<BugReport>
    @GET("api/BugReport")
    fun getBugReports(): Call<List<BugReport>>
//    @DELETE("api/BugReport")
//    fun getBugReports():
}