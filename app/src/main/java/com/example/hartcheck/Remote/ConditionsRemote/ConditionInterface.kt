package com.example.hartcheck.Remote.ConditionsRemote

import com.example.hartcheck.Wrapper.ConditionsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ConditionInterface {
    @GET("api/Condition/{patientID}")
    fun getConditionByPatientID(@Path("patientID") patientID: Int): Call<ConditionsList>
}