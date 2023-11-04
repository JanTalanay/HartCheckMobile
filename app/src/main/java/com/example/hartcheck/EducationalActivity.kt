package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.hartcheck.Model.EducationalResource
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.EducationalResourceRemote.EducationalResourceInstance
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import com.example.hartcheck.Wrapper.EducationalResourceInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class EducationalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_educational)
        val userID = intent.getIntExtra("userID", 0)
        val btnBackEduc = findViewById<Button>(R.id.btn_back_Educ)
        viewEduc()

        //back btn implement
        btnBackEduc.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }
    }
    private fun viewEduc() {
        val Educservice = EducationalResourceInstance.retrofitBuilder
        val streduc = findViewById<TextView>(R.id.str_educ)
        val strlink = findViewById<TextView>(R.id.str_link)
        val educResources = mutableListOf<EducationalResource>()

        Educservice.getEducResource().enqueue(object : Callback<EducationalResourceInfo> {
            override fun onResponse(call: Call<EducationalResourceInfo>, response: Response<EducationalResourceInfo>) {
                if (response.isSuccessful) {
                    val educResource = response.body()
                    if(educResource != null){
                        for(educResource in educResource.EducResource){
                            educResources.add(educResource)
//                            Log.d("MainActivity", "resourceID: ${educResource.resourceID}, text: ${educResource.text}, link: ${educResource.link}")
                        }
                    }
                }
                if (educResources.isNotEmpty()) {
                    val randomEducResource = educResources.random()
                    streduc.text = randomEducResource.text
                    strlink.text = randomEducResource.link
                }
                else {
                    Log.d("MainActivity", "Failed to connect: " + response.code())
                }
            }
            override fun onFailure(call: Call<EducationalResourceInfo>, t: Throwable) {
                Log.d ("MainActivity", "Failed to connect: : " + t.message)
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.string()
                    Log.d("MainActivity", "Error response: $errorResponse")
                }
            }
        })
    }
}