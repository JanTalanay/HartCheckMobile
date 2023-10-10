package com.example.hartcheck.Remote.UsersRemote

import com.example.hartcheck.Model.Login
import com.example.hartcheck.Model.PreviousMedication
import com.example.hartcheck.Model.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UsersInterface {
    @POST("api/register")
    fun registerUser(@Body request: Users): Call<Users>
    @POST("api/login")
    fun loginUser(@Body request: Login): Call<Users>
    @GET("api/register")
    fun getRegisterUsers(): Call<List<Users>>

    @GET("api/register/{userID}")
    fun getRegisterUsersID(@Path("userID") resourceId: String): Call<Users>


}