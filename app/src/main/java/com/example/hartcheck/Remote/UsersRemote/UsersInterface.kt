package com.example.hartcheck.Remote.UsersRemote

import com.example.hartcheck.Model.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsersInterface {
    @POST("api/Register")
    fun registerUser(@Body request: Users): Call<Users>
    @POST("api/login")
    fun loginUser(@Body request: Users): Call<Users>
    @GET("api/register")
    fun getRegisterUsers(): Call<List<Users>>


}