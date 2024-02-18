package com.example.hartcheck.Remote.ConditionsRemote

import com.example.hartcheck.Wrapper.ConditionsList
import com.example.hartcheck.Wrapper.DiagnosisList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ConditionInterface {
    @GET("api/Condition/{patientID}")
    fun getConditionByPatientID(@Path("patientID") patientID: Int): Call<ConditionsList>

    @GET("/api/Condition/{patientID}/{doctorID}")
    fun getConditionByPatientIDAndDoctorID(@Path("patientID") patientID: Int, @Path("doctorID") doctorID: Int
    ): Call<ConditionsList>
}