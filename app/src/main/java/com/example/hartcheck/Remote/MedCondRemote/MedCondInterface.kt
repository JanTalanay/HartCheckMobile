package com.example.hartcheck.Remote.MedCondRemote

import com.example.hartcheck.Model.MedicalCondition
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MedCondInterface {
    @POST("api/MedicalCondition")
    fun insertMedCond(@Body request: MedicalCondition): Call<MedicalCondition>
    @GET("api/MedicalCondition")
    fun getMedCond(): Call<List<MedicalCondition>>
    @DELETE("api/MedicalCondition/{medCondID}")
    fun deleteMedCond(@Path("medCondID") resourceId: String): Call<MedicalCondition>
    @PUT("api/MedicalCondition/{medCondID}")
    fun updateMedCond(@Path("medCondID") resourceId: String, @Body updatedResource: MedicalCondition): Call<MedicalCondition>
}