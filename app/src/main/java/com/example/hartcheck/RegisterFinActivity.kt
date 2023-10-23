package com.example.hartcheck

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class RegisterFinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registerfin)
        val btn_confirm_register: Button = findViewById<Button>(R.id.btn_confirm_register)
        val link_terms:Button = findViewById(R.id.link_terms)

        link_terms.setOnClickListener {
            showTerms()
        }


        btn_confirm_register.setOnClickListener {
            confirmCred()
        }
        //Add error if terms is not checked
    }
    private fun showTerms(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_terms)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btn_ok:Button = dialog.findViewById(R.id.btn_terms_okay)

        btn_ok.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun confirmCred() {
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        val confirmEmail = findViewById<EditText>(R.id.txt_confirm_email)
        val confirmPass = findViewById<EditText>(R.id.txt_confirm_pass)




        if (confirmEmail.text.toString() == email && confirmPass.text.toString() == password) {
            Toast.makeText(this, "Email and password match", Toast.LENGTH_SHORT).show()
            getPatientID()
        } else {
            Toast.makeText(this, "Email or password does not match", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getPatientID() {
        val userID = intent.getIntExtra("userID", 0)
        val service = PatientsInstance.retrofitBuilder

        service.getPatientsID(userID).enqueue(object : Callback<Patients> {
            override fun onResponse(call: Call<Patients>, response: Response<Patients>) {
                if(response.isSuccessful){
                    response.body()?.let { patients ->
                        if(userID.equals(patients.usersID)){
                            Log.d("MainActivity", "connected: ${patients.patientID}")//Intent
                            Toast.makeText(this@RegisterFinActivity, "Registration Successful ${patients.patientID}", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@RegisterFinActivity, RegisterMedActivity::class.java)
                            intent.putExtra("patientID", patients.patientID)
                            startActivity(intent)

                        }else{
                            Log.d("MainActivity", "Wrong: " + response.code())
                        }
                    }
                } else {
                    Log.d("MainActivity", "Failed to connect: " + response.code())

                }
            }
            override fun onFailure(call: Call<Patients>, t: Throwable) {
                Log.d ("MainActivity", "Failed to connect: : " + t.message)
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.string()
                    Log.d("MainActivity", "Error response: $errorResponse")
                }
            }
        })
    }
}