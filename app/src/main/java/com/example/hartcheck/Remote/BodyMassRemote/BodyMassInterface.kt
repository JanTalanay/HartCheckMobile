package com.example.hartcheck.Remote.BodyMassRemote

import com.example.hartcheck.Model.BodyMass
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.PreviousMedication
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BodyMassInterface {
    @POST("api/bodymass")
    fun insertBodyMass(@Body request: BodyMass): Call<BodyMass>
    @GET("api/bodymass")
    fun getBodyMass(): Call<List<BodyMass>>
    @GET("api/bodymass/{patientID}")
    fun getBodyMassID(@Path("patientID") patientID: Int): Call<PreviousMedication>
    @DELETE("api/bodymass/{bodyMassID}")
    fun deleteBodyMass(@Path("bodyMassID") resourceId: String): Call<BodyMass>
    @PUT("api/bodymass/{bodyMassID}")
    fun updateBodyMass(@Path("bodyMassID") resourceId: String, @Body updatedResource: BodyMass): Call<BodyMass>
}