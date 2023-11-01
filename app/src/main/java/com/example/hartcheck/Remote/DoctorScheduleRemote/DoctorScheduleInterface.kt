package com.example.hartcheck.Remote.DoctorScheduleRemote

import com.example.hartcheck.Data.DoctorScheduleResponse
import com.example.hartcheck.Model.DoctorSchedule
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DoctorScheduleInterface {
    @POST("api/DoctorSchedule")
    fun insertDoctorSched(@Body request: DoctorSchedule): Call<DoctorSchedule>
    @GET("api/DoctorSchedule")
    fun getDoctorSched(): Call<List<DoctorSchedule>>
    @GET("api/DoctorSchedule/{doctorSchedID}")
    fun getDoctorScheduleID(@Path("doctorSchedID") doctorSchedID: Int): Call<DoctorSchedule>

//    @GET("api/DoctorSchedule/patient/{patientID}")
//    fun getDoctorSchedulesForPatient(@Path("patientID") patientID: Int): Call<DoctorDetailsAndScheduleDto>

    @GET("api/DoctorSchedule/patient/{patientID}/schedules")
    fun getDoctorSchedulesForPatient(@Path("patientID") patientID: Int): Call<DoctorScheduleResponse>


//    @GET("api/DoctorSchedule/{patientID}")
//    fun getMedicalHisID(@Path("patientID") patientID: Int): Call<PreviousMedication>
//    @DELETE("api/MedicalHistory/{medicalHistoryID}")
//    fun deleteMedHis(@Path("medicalHistoryID") resourceId: String): Call<MedicalHistory>
//    @PUT("api/MedicalHistory/{medicalHistoryID}")
//    fun updateMedHis(@Path("medicalHistoryID") resourceId: String, @Body updatedResource: MedicalHistory): Call<MedicalHistory>
}