package com.example.hartcheck.Remote.MedHisRemote

import com.example.hartcheck.Remote.UsersRemote.UsersInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MedHisInstance {
    val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://10.0.2.2:5179/")
//        .baseUrl("http://10.0.2.2:5224/")
        .build()
        .create(MedHisInterface::class.java)
}