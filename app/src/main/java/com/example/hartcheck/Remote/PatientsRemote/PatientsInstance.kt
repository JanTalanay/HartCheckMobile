package com.example.hartcheck.Remote.PatientsRemote

import com.example.hartcheck.Remote.PreviousMedRemote.PreviousMedInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PatientsInstance {
    val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://10.0.2.2:5179/")
//        .baseUrl("http://10.0.2.2:5224/")
        .build()
        .create(PatientsInterface::class.java)
}