package com.example.hartcheck.Remote.PaymentRemote

import com.example.hartcheck.Data.PaymentBook
import com.example.hartcheck.Model.Users
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentInterface {
    @POST("/generate-invoice")
    fun paymentBook(@Body request: PaymentBook): Call<ResponseBody>
}