package com.example.hartcheck.Remote.PatientsRemote

import com.example.hartcheck.Model.Patients
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PatientsInterface {
    @GET("api/Patients")
    fun getPatients(): Call<List<Patients>>
    @GET("api/Patients/{patientsID}")
    fun getPatientsID(): Call<List<Patients>>
}