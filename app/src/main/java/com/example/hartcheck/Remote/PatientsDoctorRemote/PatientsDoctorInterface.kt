package com.example.hartcheck.Remote.PatientsDoctorRemote

import com.example.hartcheck.Model.PatientsDoctor
import com.example.hartcheck.Wrapper.DoctorInfoList
import com.example.hartcheck.Wrapper.PatientsDoctorAssign
import com.example.hartcheck.Wrapper.PatientsDoctorList
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
    fun getPatientsPatientID(@Path("patientID") patientID: Int): Call<PatientsDoctorList>
    @GET("api/PatientsDoctor/{patientID}/healthcareprofessionals")
    fun getHealthCareProfessionals(@Path("patientID") patientID: Int): Call<PatientsDoctorAssign>
    @GET("api/PatientsDoctor/{patientID}/doctors")
    fun getDoctorsByPatientId(@Path("patientID") patientID: Int): Call<DoctorInfoList>

//    @GET("api/PatientsDoctor/{patientID}/healthcareprofessionals")
//    fun getHealthCareProfessionals(@Path("patientID") patientID: Int): Call<List<HealthCareProfName>>

}