package com.example.hartcheck.Plugin

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Sheets {
    private fun sendData(context: Context, action: String, value:String, dataInput: String) {
        val currentDate = LocalDate.now()
        val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))

        val url = "https://script.google.com/macros/s/AKfycbwEQubR5vk5D1H_vBQLp3paK19R5yojK3SbyHDkZevVQl5av28sPLav9JJ4AgQUCWPj/exec"

        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener<String> { response ->
                // Handle the response here
                fun onResponse(response: String) {
                    println("Added Success")
                }
            },
            Response.ErrorListener { error ->
                // Handle the error here
                fun onErrorResponse(error: VolleyError) {
                    println("error")
                    Log.d("Error", error.toString())
                }
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                // Add any request parameters here if needed
                val params = HashMap<String, String>()
                params["action"] = action
                params[value] = dataInput
                params["dateLogged"] = formattedDate.toString()
                return params
            }
        }

        val socketTimeOut = 50000
        val retryPolicy: RetryPolicy = DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.setRetryPolicy(retryPolicy)

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    fun loginData(context: Context) {
        sendData(context, "login", "isLogged", "1")
    }

    fun registerData(context: Context) {
        sendData(context, "register", "registered", "1")
    }

    fun bugReportData(context: Context) {
        sendData(context, "bugReported", "bugReported", "1")
    }
}