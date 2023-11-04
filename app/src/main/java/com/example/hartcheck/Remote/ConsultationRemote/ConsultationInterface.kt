package com.example.hartcheck.Remote.ConsultationRemote

import com.example.hartcheck.Model.BugReport
import com.example.hartcheck.Model.Consultation
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.PreviousMedication
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Wrapper.DoctorScheduleDates
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ConsultationInterface {
    @GET("api/Consultation")
    fun getConsultation(): Call<List<Consultation>>
    @POST("api/Consultation")
    fun insertConsultation(@Body request: Consultation): Call<Consultation>
    @GET("api/Consultation/{doctorSchedID}/schedule")
    fun getConsultationDoctorSchedID(@Path("doctorSchedID") doctorSchedID: Int): Call<Consultation>
    @GET("api/Consultation/{patientID}")
    fun getConsultationID(@Path("patientID") patientID: Int): Call<Consultation>
    @DELETE("api/Consultation/{doctorSchedID}")
    fun deleteConsultation(@Path("doctorSchedID") resourceId: Int): Call<Consultation>
    @PUT("api/Consultation/{consultationID}")
    fun updateConsultation(@Path("consultationID") resourceId: String, @Body updatedResource: Consultation): Call<Consultation>

    @GET("api/Consultation/{patientID}/dates")
    fun getConsultationAssign(@Path("patientID") patientID: Int): Call<DoctorScheduleDates>

    @GET("api/Consultation/doctor/{doctorID}/name")
    fun getConsultationDoctor(@Path("doctorID") doctorID: Int): Call<Users>

}