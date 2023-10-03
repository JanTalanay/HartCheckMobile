package com.example.hartcheck.Remote.BugReportRemote

import com.example.hartcheck.Model.BugReport
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BugReportInterface {
    @POST("api/BugReport")
    fun insertBugReport(@Body request: BugReport): Call<BugReport>
    @GET("api/BugReport")
    fun getBugReports(): Call<List<BugReport>>
    @DELETE("api/BugReport/{bugID}")
    fun deleteBugReports(@Path("bugID") resourceId: String): Call<BugReport>
    @PUT("api/BugReport/{bugID}")
    fun updateBugReports(@Path("bugID") resourceId: String, @Body updatedResource: BugReport): Call<BugReport>
}