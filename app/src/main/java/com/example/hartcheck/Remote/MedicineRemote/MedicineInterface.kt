package com.example.hartcheck.Remote.MedicineRemote

import com.example.hartcheck.Wrapper.ConditionsList
import com.example.hartcheck.Wrapper.DiagnosisList
import com.example.hartcheck.Wrapper.MedicineList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MedicineInterface {
    @GET("api/Medicine/{patientID}")
    fun getMedicinesByPatientID(@Path("patientID") patientID: Int): Call<MedicineList>

    @GET("/api/Medicine/{patientID}/{doctorID}")
    fun getMedicineByPatientIDAndDoctorID(@Path("patientID") patientID: Int, @Path("doctorID") doctorID: Int
    ): Call<MedicineList>
}