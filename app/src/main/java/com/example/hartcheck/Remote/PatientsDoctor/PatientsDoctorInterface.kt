package com.example.hartcheck.Remote.PatientsDoctor

import com.example.hartcheck.Wrapper.Test
import com.example.hartcheck.Model.PatientsDoctor
import com.example.hartcheck.Wrapper.PatientsDoctorAssign
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PatientsDoctorInterface {
    @POST("api/PatientsDoctor")
    fun insertPatientsDoctor(@Body request: PatientsDoctor): Call<PatientsDoctor>
    @GET("api/PatientsDoctor")
    fun getPatientsDoctor(): Call<List<PatientsDoctor>>
    @GET("api/PatientsDoctor/{patientID}")
    fun getPatientsDoctorID(@Path("patientID") patientID: Int): Call<PatientsDoctor>
    @GET("api/PatientsDoctor/{patientID}/healthcareprofessionals")
    fun getHealthCareProfessionals(@Path("patientID") patientID: Int): Call<PatientsDoctorAssign>

//    @GET("api/PatientsDoctor/{patientID}/healthcareprofessionals")
//    fun getHealthCareProfessionals(@Path("patientID") patientID: Int): Call<List<HealthCareProfName>>

}