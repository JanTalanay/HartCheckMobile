package com.example.hartcheck.Remote.EducationalResourceRemote

import com.example.hartcheck.Wrapper.EducationalResourceInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EducationalResourceInterface {
    @GET("api/EducationalResource")
    fun getEducResource(): Call<EducationalResourceInfo>
}