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
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.Model.Consultation
import com.example.hartcheck.Remote.ConsultationRemote.ConsultationInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentDetailsActivity : AppCompatActivity() {

    private lateinit var btn_request:Button
    private lateinit var btn_cancel_sched:Button
    private lateinit var txtDoctorName:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_details)
        val selectedDoctor = intent.getParcelableExtra<DocData>("selectedDoctor")
//        val doctorID = intent.getIntExtra("doctorID", -1)

        btn_request = findViewById(R.id.btn_request_appointment)
        btn_cancel_sched = findViewById(R.id.btn_cancel_appointment)

        btn_cancel_sched.setOnClickListener {
            showModal()
        }
//        Toast.makeText(this, "Received data: ${selectedDoctor?.name}", Toast.LENGTH_LONG).show()
//        Toast.makeText(this, "Received data: ${selectedDoctor?.doctorSchedID}", Toast.LENGTH_LONG).show()
//        Toast.makeText(this, "Received data: ${selectedDoctor?.appointmentDate}", Toast.LENGTH_LONG).show()
        txtDoctorName = findViewById(R.id.txt_doctor_name_appointment_details)
        txtDoctorName.text = selectedDoctor?.name
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
}