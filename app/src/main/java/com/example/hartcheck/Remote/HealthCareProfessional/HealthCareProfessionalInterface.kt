package com.example.hartcheck.Remote.HealthCareProfessional

import com.example.hartcheck.Model.HealthCareProfessional
import com.example.hartcheck.Model.MedicalHistory
import com.example.hartcheck.Model.PreviousMedication
import com.example.hartcheck.Model.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HealthCareProfessionalInterface {
    @POST("api/HealthCareProfessional")
    fun insertHealthCareProf(@Body request: HealthCareProfessional): Call<HealthCareProfessional>
    @GET("api/HealthCareProfessional")
    fun getHealthCareProf(): Call<List<HealthCareProfessional>>
    @GET("api/HealthCareProfessional/{doctorID}")
    fun getHealthCareProfessionalID(@Path("doctorID") doctorID: Int): Call<HealthCareProfessional>
//    @GET("api/MedicalHistory/{patientID}")
//    fun getMedicalHisID(@Path("patientID") patientID: Int): Call<PreviousMedication>
//    @DELETE("api/MedicalHistory/{medicalHistoryID}")
//    fun deleteMedHis(@Path("medicalHistoryID") resourceId: String): Call<MedicalHistory>
//    @PUT("api/MedicalHistory/{medicalHistoryID}")
//    fun updateMedHis(@Path("medicalHistoryID") resourceId: String, @Body updatedResource: MedicalHistory): Call<MedicalHistory>
}