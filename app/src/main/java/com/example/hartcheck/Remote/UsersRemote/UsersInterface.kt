package com.example.hartcheck.Remote.UsersRemote

import com.example.hartcheck.Data.ChangePassword
import com.example.hartcheck.Data.ForgotPassword
import com.example.hartcheck.Data.OTPVerification
import com.example.hartcheck.Model.BugReport
import com.example.hartcheck.Model.Login
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.PreviousMedication
import com.example.hartcheck.Model.Users
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersInterface {
    @POST("api/register")
    fun registerUser(@Body request: Users): Call<Users>
    @POST("api/login")
    fun loginUser(@Body request: Login): Call<Users>
    @POST("api/register/ForgotPassword")
    fun forgotPassword(@Body request: ForgotPassword): Call<ResponseBody>

    @POST("api/register/VerifyOTP")
    fun VerifyOTP(@Body request: OTPVerification): Call<ResponseBody>

    @POST("api/register/ChangePassword")
    fun ChangePassword(@Body request: ChangePassword): Call<ResponseBody>

    @GET("api/register")
    fun getRegisterUsers(): Call<List<Users>>

    @GET("api/register/{userID}")
    fun getRegisterUsersID(@Path("userID") usersID: Int): Call<Users>
    @GET("api/register/GetUsersByEmail/{email}")
    fun getRegisterEmail(@Path("email") email: String): Call<Users>

    @DELETE("api/register/{userID}")
    fun deleteUser(@Path("userID") resourceId: Int): Call<Users>

    @PUT("api/register/{userID}")
    fun updateUser(@Path("userID") resourceId: String, @Body updatedResource: Users): Call<Users>


}