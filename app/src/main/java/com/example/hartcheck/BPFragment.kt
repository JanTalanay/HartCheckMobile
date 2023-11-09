package com.example.hartcheck

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hartcheck.Model.BloodPressure
import com.example.hartcheck.Model.BloodPressureThreshold
import com.example.hartcheck.Remote.BloodPressureRemote.BloodPressureInstance
import com.example.hartcheck.Remote.BloodPressureThresholdRemote.BloodPressureThresholdInstance
import com.example.hartcheck.Wrapper.PrevBloodPressure
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//sql
//import java.sql.Connection
//import java.sql.DriverManager
//import java.sql.ResultSet
//import java.sql.Statement


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"




/**
 * A simple [Fragment] subclass.
 * Use the [BPFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BPFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val systolic = mutableListOf<Entry>()
    private val diastolic = mutableListOf<Entry>()
    private val list = mutableListOf<Pair<String, String>>()
    private var systolicThreshold: Float = 0f
    private var diastolicThreshold: Float = 0f
    private var currentIndex = 0
    private val CHANNEL_ID ="Your_Channel_ID"
    private val notificationID = 101
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            sendNotification("Permission granted")
        } else {
            // Handle the case where the user denies the permission request
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        //fix
        val view = inflater.inflate(R.layout.fragment_b_p, container, false)
        val patientID = arguments?.getInt(ARG_PATIENT_ID)
        val userID = arguments?.getInt(ARG_USER_ID)
        val patientName = arguments?.getString(ARG_PATIENT_NAME)

        val addBP: Button = view.findViewById(R.id.btn_add_bp)
        val backBP: Button = view.findViewById(R.id.btn_back_BloodP)
        val prevBP: Button = view.findViewById(R.id.btn_view_prev_bp)


        readCSVFile()
        getBPThreshold()
        createNotificationChannel()

        backBP.setOnClickListener {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra("userID", userID)
            intent.putExtra("patientID", patientID)
            intent.putExtra("patientName", patientName)
            startActivity(intent)
        }
        prevBP.setOnClickListener {
            val userID = arguments?.getInt(ARG_USER_ID)
            val patientID = arguments?.getInt(ARG_PATIENT_ID)
            getprevBP(userID!!, patientID!!)
        }
        addBP.setOnClickListener {
            showModal()
        }
        getBpList(patientID!!)
        return view
    }

    private fun showModal(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_bp_input)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btn_close:Button = dialog.findViewById(R.id.btn_modal_deny)
        val syncBP = dialog.findViewById<Button>(R.id.btn_modal_sync)
        val confirmBP = dialog.findViewById<Button>(R.id.btn_modal_confirm)

        //add bp manually details here
        syncBP.setOnClickListener {
            dialog.window?.decorView?.let { displayNextPair(it) }
        }
        confirmBP.setOnClickListener {
            insertBP(dialog)
        }

        btn_close.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification(message: String) {
        val permission = "android.permission.POST_NOTIFICATIONS"
        val hasPermission = requireActivity().checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            val builder = NotificationCompat.Builder(requireActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Blood Pressure Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(requireActivity())) {
                notify(notificationID, builder.build())
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissionLauncher.launch(permission)
            }
        }
    }
    private fun setupChart() {
        val chart: LineChart = requireView().findViewById(R.id.lineChart)

        val dataSet1 = LineDataSet(systolic, "Systolic")
        dataSet1.color = Color.RED
        dataSet1.valueTextSize = 11f

        val dataSet2 = LineDataSet(diastolic, "Diastolic")
        dataSet2.color = Color.BLUE
        dataSet2.valueTextSize = 11f

        val lineData = LineData(dataSet1, dataSet2)
        chart.data = lineData

        // Chart customization code...
        val description = Description()
        description.text = "BP Chart"
        chart.description = description

        chart.xAxis.position =  XAxisPosition.BOTTOM
        chart.axisLeft.axisMaximum = 200f
        chart.axisLeft.axisMinimum = 0f
        chart.axisRight.isEnabled = false

        chart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        chart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        chart.legend.setDrawInside(false)

        chart.invalidate()
    }
    private fun getBpList(patientID:Int){
        val service = BloodPressureInstance.retrofitBuilder
        service.getBloodPressureID(patientID).enqueue(object : Callback<PrevBloodPressure> {
            override fun onResponse(call: Call<PrevBloodPressure>, response: Response<PrevBloodPressure>) {
                if (response.isSuccessful) {
                    val BPlist = response.body()
                    if (BPlist!=null){
                        var count = 0f
                        for(x in BPlist.PrevBP){
                            count++
                            systolic.add(Entry(count,x.systolic!!))
                            diastolic.add(Entry(count, x.diastolic!!))
                            Log.d("BP", "systolic: $systolic" + response.code())
                            Log.d("BP", "diastolic: $diastolic " + response.code())
                            setupChart()

                        }
                        Log.d("BP", "Prepared data: systolic=$systolic, diastolic=$diastolic")
                    }
                } else {
                    // Handle the error
                    Log.d("BP", "Failed to connect: " + response.code())
                }
            }

            override fun onFailure(call: Call<PrevBloodPressure>, t: Throwable) {
                // Handle the failure
                Log.d("BP", "Failed to connect: " + t.message)
            }
        })

    }
    private fun insertBP(dialog: Dialog){
        val patientID = arguments?.getInt(ARG_PATIENT_ID)
        val systolic = dialog.findViewById<EditText>(R.id.edit_systolic)
        val diastolic = dialog.findViewById<EditText>(R.id.edit_diastolic)

        val BPsystolic = systolic?.text.toString()
        val BPdiastolic = diastolic?.text.toString()

        // Check if systolic and diastolic values are not empty
        if (BPsystolic.isEmpty() || BPdiastolic.isEmpty()) {
            Toast.makeText(context, "Please enter both systolic and diastolic values", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if systolic and diastolic values are numeric
        if (!BPsystolic.isNumeric() || !BPdiastolic.isNumeric()) {
            Toast.makeText(context, "Please enter valid numeric values", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if systolic and diastolic values are within valid range
        val systolicValue = BPsystolic.toFloat()
        val diastolicValue = BPdiastolic.toFloat()
        if (systolicValue < 90 || systolicValue > 140 || diastolicValue < 60 || diastolicValue > 90) {
            Toast.makeText(context, "Please enter values within the valid range (Systolic: 90-140, Diastolic: 60-90)", Toast.LENGTH_SHORT).show()
            return
        }


        val current = LocalDateTime.now()// Get the current date and time
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") //Format the current date and time to a String
        val formatted = current.format(format)
        val dateTaken = LocalDateTime.parse(formatted, format)

        val BPInfo = BloodPressure(patientID = patientID, systolic = BPsystolic.toFloat(), diastolic = BPdiastolic.toFloat(), dateTaken = dateTaken.toString())

        val BPService = BloodPressureInstance.retrofitBuilder
        BPService.insertBloodPressure(BPInfo).enqueue(object : Callback<BloodPressure> {
            override fun onResponse(call: Call<BloodPressure>, response: Response<BloodPressure>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Blood Pressure Data Added", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle the error response
                    response.errorBody()?.let { errorBody ->
                        Log.d("MainActivity", "Response: ${errorBody.string()}")

                    }
                }
            }

            override fun onFailure(call: Call<BloodPressure>, t: Throwable) {
                // Handle network or other exceptions
                Log.d("MainActivity", "Response:" + t.message)

            }
        })
    }
    private fun getprevBP(userID: Int, patientID: Int) {
        val service = BloodPressureInstance.retrofitBuilder
        val patientName = arguments?.getString(ARG_PATIENT_NAME)

        service.getBloodPressureID(patientID).enqueue(object : Callback<PrevBloodPressure> {
            override fun onResponse(call: Call<PrevBloodPressure>, response: Response<PrevBloodPressure>) {
                if (response.isSuccessful) {
                    response.body()?.let { prevBP ->
                        // Parse the response data into your PrevBloodPressure object
                        val prevBPList = prevBP.PrevBP

                        // Start PreviousBPActivity and pass the retrieved list
                        val intent = Intent(activity, PreviousBPActivity::class.java)
                        intent.putExtra("userID", userID)
                        intent.putExtra("patientID", patientID)
                        intent.putExtra("patientName", patientName)
                        intent.putParcelableArrayListExtra("prevBPList", ArrayList(prevBPList))
                        startActivity(intent)
                    }
                } else {
                    // Handle the error
                    Log.d("MainActivity", "Failed to connect: " + response.code())
                }
            }

            override fun onFailure(call: Call<PrevBloodPressure>, t: Throwable) {
                // Handle the failure
                Log.d("MainActivity", "Failed to connect: : " + t.message)
            }
        })
    }
    private fun readCSVFile() {//THIS SHIT WORKS
        val csvFile = resources.assets.open("com.samsung.shealth.blood_pressure.20230706212807.csv")
        val reader = BufferedReader(InputStreamReader(csvFile))
        val csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT)
        csvParser.forEach{
            it?.let{
                val systolic = it.get(13)
                val diastolic = it.get(11)
                if (systolic.isNumeric() && diastolic.isNumeric()) {
                    list.add(Pair(systolic, diastolic))
                }
            }
        }
    }

    private fun displayNextPair(view: View) {//checks the blood pressure threshold
        val systolic = view.findViewById<EditText>(R.id.edit_systolic)
        val diastolic = view.findViewById<EditText>(R.id.edit_diastolic)

        if (currentIndex < list.size) {
            val pair = list[currentIndex]
            systolic?.setText(pair.first)
            diastolic?.setText(pair.second)

            // Check if systolic or diastolic value has reached specific stage
            val systolicValue = pair.first.toFloat()
            val diastolicValue = pair.second.toFloat()
            if (systolicValue >= systolicThreshold || diastolicValue >= diastolicThreshold) {
                sendNotification("High Blood Pressure (hypertension)")
            } else if (systolicValue >= (systolicThreshold - 10) || (systolicValue >= (systolicThreshold - 20) && diastolicValue >= (diastolicThreshold - 10))) {
                sendNotification("Elevated")
            } else if ((systolicValue >= (systolicThreshold - 20) && systolicValue < systolicThreshold) || (diastolicValue >= (diastolicThreshold - 10) && diastolicValue < diastolicThreshold)) {
                sendNotification("At Risk (prehypertension)")
            } else if (systolicValue < (systolicThreshold - 20) && diastolicValue < (diastolicThreshold - 10)) {
                sendNotification("Normal")
            }

            currentIndex++
        } else {
            systolic?.setText("No more pairs in the list.")
            diastolic?.setText("")
        }
    }
    private fun getBPThreshold() {
        val service = BloodPressureThresholdInstance.retrofitBuilder
        val patientID = arguments?.getInt(ARG_PATIENT_ID)

        service.getBloodPressureThreshold(patientID!!).enqueue(object : Callback<BloodPressureThreshold> {
            override fun onResponse(call: Call<BloodPressureThreshold>, response: Response<BloodPressureThreshold>) {
                if(response.isSuccessful){
                    val BPThreshold = response.body()
                    if(BPThreshold != null){
                        systolicThreshold = BPThreshold.systolicLevel.toFloat()
                        diastolicThreshold = BPThreshold.diastolicLevel.toFloat()
                        Log.d ("MainActivity", "Set Threshold: $systolicThreshold and $diastolicThreshold")
                    } else {
                        // Set default thresholds if BPThreshold is null
                        systolicThreshold = 140.0f
                        diastolicThreshold = 90.0f
                        Log.d ("MainActivity", "Set default Threshold: $systolicThreshold and $diastolicThreshold")
                    }
                }
                else{
                    Log.d("MainActivity", "Error response: ${response.body()}")
                }
            }
            override fun onFailure(call: Call<BloodPressureThreshold>, t: Throwable) {
                Log.d ("MainActivity", "Failed to connect: : " + t.message)
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.string()
                    Log.d("MainActivity", "Error response: $errorResponse")
                }
            }
        })
    }

    fun String.isNumeric(): Boolean = this.matches("\\d+".toRegex())

    companion object {
        private const val ARG_PATIENT_ID = "patientID"
        private const val ARG_USER_ID = "userID"
        private const val ARG_PATIENT_NAME = "patientName"
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(userID: Int, patientID: Int, patientName: String): BPFragment {
            val fragment = BPFragment()
            val args = Bundle()
            args.putInt(ARG_USER_ID, userID)
            args.putInt(ARG_PATIENT_ID, patientID)
            args.putString(ARG_PATIENT_NAME, patientName)
            fragment.arguments = args
            return fragment
        }
        @JvmStatic
        fun newInstance(param1: String, param2: String): UserFragment {
            val fragment = UserFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}