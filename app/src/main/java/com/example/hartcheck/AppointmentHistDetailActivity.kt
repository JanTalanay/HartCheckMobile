package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Adapter.ConsultationAdapter
import com.example.hartcheck.Data.ConsultationData
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.Remote.ConditionsRemote.ConditionsInstance
import com.example.hartcheck.Remote.DiagnosisRemote.DiagnosisInstance
import com.example.hartcheck.Remote.MedicineRemote.MedicineInstance
import com.example.hartcheck.Wrapper.ConditionsList
import com.example.hartcheck.Wrapper.DiagnosisList
import com.example.hartcheck.Wrapper.MedicineList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class AppointmentHistDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var consultationAdapter: ConsultationAdapter
    private lateinit var consulList: MutableList<ConsultationData>
    private lateinit var btnbackapphist: Button
    private lateinit var txt_emp: TextView
    private var history = mutableListOf<ConsultationData>()
    private lateinit var txtDoctorName:TextView
    private lateinit var txtappointsched:TextView
    private lateinit var txtPatientName:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_hist_detail)
        val selectedDoctor = intent.getParcelableExtra<DocData>("selectedDoctor")
        val userID = intent.getIntExtra("userID",  0)
        val patientID = intent.getIntExtra("patientID",  0)
        val patientName = intent.getStringExtra("patientName")
//        val doctorID = intent.getIntExtra("doctorID",  0)

//        Toast.makeText(this, "Doctor ID: $doctorID,userID: $userID ", Toast.LENGTH_LONG).show()
//        //Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_appointment_hist_details, container, false)
//        val frag = false
        recyclerView = findViewById(R.id.recyclerView_hist)
        btnbackapphist = findViewById(R.id.btn_back_appoint_hist)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        consultationAdapter = ConsultationAdapter(consulList)
//        recyclerView.adapter = consultationAdapter

//        enable this as default
//        txt_emp.visibility = GONE// you don't have any appointments today (put a null checker to show or not)
        recyclerView.visibility = VISIBLE

        history = mutableListOf(
            ConsultationData("Condition", "condition goes here"),
            ConsultationData("Diagnosis", "diagnosis goes here"),
            ConsultationData("Prescription", "medicine goes here")
        )
        txtDoctorName = findViewById(R.id.txt_appoint_his_doctor)
        txtappointsched = findViewById(R.id.txt_appoint_his_date)
        txtPatientName = findViewById(R.id.txt_appoint_his_patientName)
        txtDoctorName.text = selectedDoctor?.name
        txtappointsched.text = selectedDoctor?.appointmentDate
        txtPatientName.text = patientName
//        Toast.makeText(this, selectedDoctor?.name, Toast.LENGTH_LONG).show()
//        Toast.makeText(this, selectedDoctor?.appointmentDate, Toast.LENGTH_LONG).show()
//        Toast.makeText(this, patientName, Toast.LENGTH_LONG).show()

        consultationAdapter = ConsultationAdapter(history)
        recyclerView.adapter = consultationAdapter

        getDiagnosisList()
        getMedicinesList()
        getConsultationsList()

        btnbackapphist.setOnClickListener {
            val intent = Intent(this@AppointmentHistDetailActivity, HomeActivity::class.java)
                intent.putExtra("patientID", patientID)
                intent.putExtra("patientName", patientName)
                intent.putExtra("userID", userID)
            startActivity(intent)
        }
    }

    private fun getDiagnosisList() {
        val patientID = intent.getIntExtra("patientID",   0)
        val doctorID = intent.getIntExtra("doctorID",   0) // Retrieve the doctorID from the intent
        val service = DiagnosisInstance.retrofitBuilder

        service.getDiagnosisByPatientIDAndDoctorID(patientID, doctorID).enqueue(object : Callback<DiagnosisList> {
            override fun onResponse(call: Call<DiagnosisList>, response: Response<DiagnosisList>) {
                if (response.isSuccessful) {
                    val diagnosis = response.body()
                    if (diagnosis != null && diagnosis.DiagnosisInfo.isNotEmpty()) {
                        // Update the description field with the fetched diagnosis data and dateTime
                        val diagnosisText = diagnosis.DiagnosisInfo.joinToString(separator = "\n") { "${it.diagnosis} - ${formatDateTime(it.dateTime)} \n" }
                        history[1] = ConsultationData("Diagnosis", diagnosisText)
                        consultationAdapter.notifyDataSetChanged() // Notify the adapter that the data has changed
                    } else {
                        Toast.makeText(this@AppointmentHistDetailActivity, "No diagnoses found for this patient and doctor.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AppointmentHistDetailActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DiagnosisList>, t: Throwable) {
                Toast.makeText(this@AppointmentHistDetailActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getMedicinesList() {
        val patientID = intent.getIntExtra("patientID",   0)
        val doctorID = intent.getIntExtra("doctorID",   0) // Retrieve the doctorID from the intent
        val service = MedicineInstance.retrofitBuilder

        service.getMedicineByPatientIDAndDoctorID(patientID, doctorID).enqueue(object : Callback<MedicineList> {
            override fun onResponse(call: Call<MedicineList>, response: Response<MedicineList>) {
                if (response.isSuccessful) {
                    val medicine = response.body()
                    if (medicine != null && medicine.MedicineInfo.isNotEmpty()) {
                        // Concatenate medicine details into a single string
                        val medicineDetails = medicine.MedicineInfo.joinToString(separator = "\n") {
                            "${it.medicine} - ${formatDateTime(it.dateTime!!)} Dosage: ${it.dosage} \n"
                        }
                        // Update the description field with the fetched medicine data
                        history[2] = ConsultationData("Prescription", medicineDetails)
                        consultationAdapter.notifyDataSetChanged() // Notify the adapter that the data has changed
                    } else {
                        Toast.makeText(this@AppointmentHistDetailActivity, "No medicines found for this patient.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AppointmentHistDetailActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MedicineList>, t: Throwable) {
                Toast.makeText(this@AppointmentHistDetailActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getConsultationsList() {
        val patientID = intent.getIntExtra("patientID",  0)
        val doctorID = intent.getIntExtra("doctorID",   0) // Retrieve the doctorID from the intent
        val service = ConditionsInstance.retrofitBuilder

        service.getConditionByPatientIDAndDoctorID(patientID, doctorID).enqueue(object : Callback<ConditionsList> {
            override fun onResponse(call: Call<ConditionsList>, response: Response<ConditionsList>) {
                if (response.isSuccessful) {
                    val conditions = response.body()
                    if (conditions != null && conditions.ConditionsInfo.isNotEmpty()) {
                        // Update the description field with the fetched conditions data
                        history[0] = ConsultationData("Condition", conditions.ConditionsInfo.joinToString(separator = "\n") { "${it.condition} - ${formatDate(it.date)}" })
                        consultationAdapter.notifyDataSetChanged() // Notify the adapter that the data has changed
                    } else {
                        Toast.makeText(this@AppointmentHistDetailActivity, "No conditions found for this patient.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AppointmentHistDetailActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ConditionsList>, t: Throwable) {
                Toast.makeText(this@AppointmentHistDetailActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun formatDateTime(originalDateTime: String): String {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss"
        val outputPattern = "MMMM dd, yyyy 'at' hh:mm a"

        val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.US)

        val dateTime = inputFormat.parse(originalDateTime)
        return outputFormat.format(dateTime)
    }
    private fun formatDate(originalDateTime: String): String {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss"
        val outputPattern = "MMMM dd, yyyy"

        val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.US)

        val dateTime = inputFormat.parse(originalDateTime)
        return outputFormat.format(dateTime)
    }
}