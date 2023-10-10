package com.example.hartcheck.Remote.PreviousMedRemote

import com.example.hartcheck.Model.PreviousMedication
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PreviousMedInterface {
    @POST("api/PreviousMed")
    fun insertPrevMed(@Body request: PreviousMedication): Call<PreviousMedication>
    @GET("api/PreviousMed")
    fun getPrevMed(): Call<List<PreviousMedication>>
    @DELETE("api/PreviousMed/{prevMedID}")
    fun deletePrevMed(@Path("prevMedID") resourceId: String): Call<PreviousMedication>
    @PUT("api/PreviousMed/{prevMedID}")
    fun updatePrevMed(@Path("prevMedID") resourceId: String, @Body updatedResource: PreviousMedication): Call<PreviousMedication>
}