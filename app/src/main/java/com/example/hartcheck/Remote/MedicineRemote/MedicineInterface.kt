package com.example.hartcheck.Remote.MedicineRemote

import com.example.hartcheck.Wrapper.ConditionsList
import com.example.hartcheck.Wrapper.MedicineList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MedicineInterface {
    @GET("api/Medicine/{patientID}")
    fun getMedicinesByPatientID(@Path("patientID") patientID: Int): Call<MedicineList>
}