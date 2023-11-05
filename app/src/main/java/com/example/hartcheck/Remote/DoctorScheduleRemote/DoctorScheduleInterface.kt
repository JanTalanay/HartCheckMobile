package com.example.hartcheck.Remote.DoctorScheduleRemote

import com.example.hartcheck.Model.DoctorSchedule
import com.example.hartcheck.Wrapper.DoctorScheduleDates
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

    @GET("api/DoctorSchedule/patient/{patientID}/schedules")
    fun getDoctorSchedulesForPatient(@Path("patientID") patientID: Int): Call<DoctorScheduleDates>

}