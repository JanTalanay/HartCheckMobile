package com.example.hartcheck.Remote.MedHisRemote

import com.example.hartcheck.Model.MedicalHistory
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MedHisInterface {
    @POST("api/MedicalHistory")
    fun insertMedHis(@Body request: MedicalHistory): Call<MedicalHistory>
    @GET("api/MedicalHistory")
    fun getMedHis(): Call<List<MedicalHistory>>
    @DELETE("api/MedicalHistory/{medicalHistoryID}")
    fun deleteMedHis(@Path("medicalHistoryID") resourceId: String): Call<MedicalHistory>
    @PUT("api/MedicalHistory/{medicalHistoryID}")
    fun updateMedHis(@Path("medicalHistoryID") resourceId: String, @Body updatedResource: MedicalHistory): Call<MedicalHistory>
}