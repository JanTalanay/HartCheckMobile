package com.example.hartcheck

import android.app.Dialog
import android.content.Context
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
import androidx.fragment.app.Fragment
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.Data.RescheduleAppointment
import com.example.hartcheck.Model.Consultation
import com.example.hartcheck.Remote.ConsultationRemote.ConsultationInstance
import com.example.hartcheck.Remote.PatientsDoctorRemote.PatientsDoctorInstance
import com.example.hartcheck.Remote.PaymentRemote.PaymentInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentDetailsActivity : AppCompatActivity() {

    private lateinit var btn_request:Button
    private lateinit var btn_cancel_sched:Button
    private lateinit var btnbackappointdetails:Button
    private lateinit var txtDoctorName:TextView
    private lateinit var txtappointsched:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_details)
        val selectedDoctor = intent.getParcelableExtra<DocData>("selectedDoctor")
        val userID = intent.getIntExtra("userID", 0)
        val patientID = intent.getIntExtra("patientID", 0)
        val patientName = intent.getStringExtra("patientName")


        btn_request = findViewById(R.id.btn_request_appointment)//add input modal
        btn_cancel_sched = findViewById(R.id.btn_cancel_appointment)
        btnbackappointdetails = findViewById(R.id.btn_back_appoint_details)
        btnbackappointdetails.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("patientID", patientID)
            intent.putExtra("userID", userID)
            intent.putExtra("patientName", patientName)
            startActivity(intent)
        }
        btn_request.setOnClickListener {
            showReschedModal()
        }

        btn_cancel_sched.setOnClickListener {
            showModal()
        }
        txtDoctorName = findViewById(R.id.txt_doctor_name_appointment_details)
        txtappointsched = findViewById(R.id.txt_appoint_sched)
        txtDoctorName.text = selectedDoctor?.name
        txtappointsched.text = selectedDoctor?.appointmentDate
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
    private fun showReschedModal(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_resched_input)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//        val email: EditText = dialog.findViewById(R.id.modal_edit_email)
        val btnSend:Button = dialog.findViewById(R.id.btn_modal_send)
        val btnClose:Button = dialog.findViewById(R.id.btn_modal_cancel)


        btnSend.setOnClickListener {
            //add cancel appoint here
            rescheduleAppointment(dialog)
            dialog.dismiss()

        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showModal(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_confirmation)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val modalText: TextView = dialog.findViewById(R.id.txt_modal)
        val btnAccept:Button = dialog.findViewById(R.id.btn_modal_yes)
        val btnClose:Button = dialog.findViewById(R.id.btn_modal_no)

        modalText.setText(R.string.p_cancel_modal)

        btnAccept.setOnClickListener {
            //add cancel appoint here
            cancelConsultation(dialog)
            dialog.dismiss()

        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun cancelConsultation(dialog: Dialog){
        val selectedDoctor = intent.getParcelableExtra<DocData>("selectedDoctor")
        val consultationAssignService = ConsultationInstance.retrofitBuilder

//        val consultInfo = Consultation(doctorSchedID = doctorSchedID)

        consultationAssignService.deleteConsultation(selectedDoctor?.doctorSchedID!!).enqueue(object : Callback<Consultation> {
            override fun onResponse(call: Call<Consultation>, response: Response<Consultation>) {
                if(response.isSuccessful){
                    Toast.makeText(this@AppointmentDetailsActivity, "Cancelled", Toast.LENGTH_LONG).show()
                    Log.d("MainActivity", "Success: ${response.body()}")
                }
                else{
                    Log.d("MainActivity", "Error: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<Consultation>, t: Throwable) {
                Log.d("MainActivity", "Failure: ${t.message}")
            }
        })
    }
    private fun rescheduleAppointment(dialog: Dialog){
        val selectedDoctor = intent.getParcelableExtra<DocData>("selectedDoctor")
        val email: EditText = dialog.findViewById(R.id.modal_edit_email)

        val rescheduleAppointmentService = PatientsDoctorInstance.retrofitBuilder

        val patientEmail = email.text.toString()
        val name = selectedDoctor?.name.toString()

        val reschedAppointment = RescheduleAppointment(email = patientEmail, doctorName = name)

        rescheduleAppointmentService.rescheduleAppointmentRequest(reschedAppointment).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                if (response.isSuccessful) {
                    Toast.makeText(this@AppointmentDetailsActivity, "Reschedule Sent!", Toast.LENGTH_LONG).show()
                }
                else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("MainActivity", "Error: $errorBody")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d ("MainActivity", "Registration failed: ${t}\"")
            }
        })
    }
}