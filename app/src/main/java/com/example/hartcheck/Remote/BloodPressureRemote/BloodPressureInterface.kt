package com.example.hartcheck.Remote.BloodPressureRemote

import com.example.hartcheck.Model.BloodPressure
import com.example.hartcheck.Model.BugReport
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.PreviousMedication
import com.example.hartcheck.Wrapper.PrevBloodPressure
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BloodPressureInterface {
    @POST("api/BloodPressure")
    fun insertBloodPressure(@Body request: BloodPressure): Call<BloodPressure>
    @GET("api/BloodPressure")
    fun getBloodPressure(): Call<List<BloodPressure>>
    @GET("api/BloodPressure/{patientID}")
    fun getBloodPressureID(@Path("patientID") patientID: Int): Call<PrevBloodPressure>
    @DELETE("api/BloodPressure/{bloodPressureID}")
    fun deleteBloodPressure(@Path("bloodPressureID") resourceId: String): Call<BloodPressure>
    @PUT("api/BloodPressure/{bloodPressureID}")
    fun updateBloodPressure(@Path("bloodPressureID") resourceId: String, @Body updatedResource: BloodPressure): Call<BloodPressure>
}