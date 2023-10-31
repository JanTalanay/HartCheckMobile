package com.example.hartcheck.Remote.DoctorScheduleRemote

import com.example.hartcheck.Model.DoctorSchedule
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

interface DoctorScheduleInterface {
    @POST("api/DoctorSchedule")
    fun insertDoctorSched(@Body request: DoctorSchedule): Call<DoctorSchedule>
    @GET("api/DoctorSchedule")
    fun getDoctorSched(): Call<List<DoctorSchedule>>
    @GET("api/DoctorSchedule/{doctorSchedID}")
    fun getDoctorScheduleID(@Path("doctorSchedID") doctorSchedID: Int): Call<DoctorSchedule>
//    @GET("api/DoctorSchedule/{patientID}")
//    fun getMedicalHisID(@Path("patientID") patientID: Int): Call<PreviousMedication>
//    @DELETE("api/MedicalHistory/{medicalHistoryID}")
//    fun deleteMedHis(@Path("medicalHistoryID") resourceId: String): Call<MedicalHistory>
//    @PUT("api/MedicalHistory/{medicalHistoryID}")
//    fun updateMedHis(@Path("medicalHistoryID") resourceId: String, @Body updatedResource: MedicalHistory): Call<MedicalHistory>
}