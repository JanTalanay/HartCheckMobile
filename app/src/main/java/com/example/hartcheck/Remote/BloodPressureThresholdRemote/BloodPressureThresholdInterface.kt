package com.example.hartcheck.Remote.BloodPressureThresholdRemote

import com.example.hartcheck.Model.BloodPressureThreshold
import com.example.hartcheck.Wrapper.EducationalResourceInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BloodPressureThresholdInterface {
    @GET("api/BloodPressureThreshold/{patientID}")
    fun getBloodPressureThreshold(@Path("patientID") patientID: Int): Call<BloodPressureThreshold>
}