package com.example.hartcheck.Remote.PatientsRemote

import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PatientsInterface {
    @GET("api/Patient")
    fun getPatients(): Call<List<Patients>>
    @GET("api/Patient/{usersID}")
    fun getPatientsID(@Path("usersID") usersID: Int): Call<Patients>

    @POST("api/Patient")
    fun insetPatients(@Body request: Patients): Call<Patients>
}