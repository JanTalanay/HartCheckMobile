package com.example.hartcheck.Remote.DiagnosisRemote

import com.example.hartcheck.Wrapper.DiagnosisList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DiagnosisInterface {
    @GET("api/Diagnosis/{patientID}")
    fun getDiagnosisByPatientID(@Path("patientID") patientID: Int): Call<DiagnosisList>
}