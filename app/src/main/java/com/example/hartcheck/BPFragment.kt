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
import com.example.hartcheck.Remote.BloodPressureRemote.BloodPressureInstance
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
    private val list = mutableListOf<Pair<String, String>>()
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

        Toast.makeText(context, "Bpfragment $userID AND $patientID", Toast.LENGTH_SHORT).show()

        val addBP: Button = view.findViewById(R.id.btn_add_bp)
        val backBP: Button = view.findViewById(R.id.btn_back_BloodP)
        val viewBP: Button = view.findViewById(R.id.btn_view_bp)



        val chart: LineChart = view.findViewById(R.id.lineChart)

        val entries1 = mutableListOf<Entry>()
        val entries2 = mutableListOf<Entry>()

        readCSVFile()
        createNotificationChannel()

        backBP.setOnClickListener {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra("userID", userID)
            intent.putExtra("patientID", patientID)
            startActivity(intent)
        }

        addBP.setOnClickListener {
            showModal()
        }

        viewBP.setOnClickListener {
            val intent = Intent(activity,PreviousBPActivity::class.java)
            intent.putExtra("userID", userID)
            intent.putExtra("patientID", patientID)
        }

        // Read data from CSV file
        val csvFile = resources.assets.open("test_sheet.csv")
        val reader = BufferedReader(InputStreamReader(csvFile))
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        //val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        for (record in csvParser) {
            //val xString = record.get("x")
            val x = record.get("x").toFloat()// should be date: val xDate = dateFormat.parse(xString)
            val y1 = record.get("y1").toFloat()
            val y2 = record.get("y2").toFloat()
            entries1.add(Entry(x, y1))
            entries2.add(Entry(x, y2))
//            entries1.add(Entry(xDate.time.toFloat(), y1))
//            entries2.add(Entry(xDate.time.toFloat(), y2))
        }

        //DB Method
//        val connection: Connection = DriverManager.getConnection(
//            "jdbc:sqlserver://your_server_address:your_port;databaseName=your_database_name;user=your_username;password=your_password"
//        )
//
//        // Execute a query to retrieve the data
//        val statement: Statement = connection.createStatement()
//        val resultSet: ResultSet = statement.executeQuery("SELECT x, y FROM your_table_name")
//
//        while (resultSet.next()) {
//            val x = resultSet.getFloat("x")
//            val y = resultSet.getFloat("y")
//            entries.add(Entry(x, y))
//        }
//
//        resultSet.close()
//        statement.close()
//        connection.close()

        val dataSet1 = LineDataSet(entries1, "Systolic")
        dataSet1.color = Color.RED

        val dataSet2 = LineDataSet(entries2, "Diastolic")
        dataSet2.color = Color.BLUE

        val lineData = LineData(dataSet1, dataSet2)
        chart.data = lineData

        //chart customization
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
        val systolic = dialog.findViewById<EditText>(R.id.edit_systolic)
        val diastolic = dialog.findViewById<EditText>(R.id.edit_diastolic)

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
                .setSmallIcon(R.drawable.ic_launcher_foreground)
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


    private fun insertBP(dialog: Dialog){
        val patientID = arguments?.getInt(ARG_PATIENT_ID)
//        val systolic = view?.findViewById<EditText>(R.id.edit_systolic)
//        val diastolic = view?.findViewById<EditText>(R.id.edit_diastolic)
        val systolic = dialog.findViewById<EditText>(R.id.edit_systolic)
        val diastolic = dialog.findViewById<EditText>(R.id.edit_diastolic)

        val BPsystolic = systolic?.text.toString()
        val BPdiastolic = diastolic?.text.toString()
        val current = LocalDateTime.now()// Get the current date and time
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") //Format the current date and time to a String
        val formatted = current.format(format)
        val dateTaken = LocalDateTime.parse(formatted, format)

        val BPInfo = BloodPressure(patientID = patientID, systolic = BPsystolic.toFloat(), diastolic = BPdiastolic.toFloat(), dateTaken = dateTaken.toString())

        val BPService = BloodPressureInstance.retrofitBuilder
        BPService.insertBloodPressure(BPInfo).enqueue(object : Callback<BloodPressure> {
            override fun onResponse(call: Call<BloodPressure>, response: Response<BloodPressure>) {
                if (response.isSuccessful) {

                    Toast.makeText(context, "ADDED NEW BP", Toast.LENGTH_SHORT).show()
//                    Log.d("MainActivity", "Response: ${response.body()}")


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
    private fun getprevBP() {

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

    private fun displayNextPair(view: View) {
        val systolic = view.findViewById<EditText>(R.id.edit_systolic)
        val diastolic = view.findViewById<EditText>(R.id.edit_diastolic)

        if (currentIndex < list.size) {
            val pair = list[currentIndex]
            systolic?.setText(pair.first)
            diastolic?.setText(pair.second)

            // Check if systolic or diastolic value has reached specific stage
            val systolic = pair.first.toInt()
            val diastolic = pair.second.toInt()
            val specificStage = 120 // Replace with your specific stage
            if (systolic >= 140 || diastolic >= 90) {
                sendNotification("High Blood Pressure (hypertension)")
            } else if (systolic >= 130 || (systolic >= 120 && diastolic >= 80)) {
                sendNotification("Elevated")
            } else if ((systolic >= 120 && systolic < 130) || (diastolic >= 80 && diastolic < 89)) {
                sendNotification("At Risk (prehypertension)")
            } else if (systolic < 120 && diastolic < 80) {
                sendNotification("Normal")
            }

            currentIndex++
        } else {
            systolic?.setText("No more pairs in the list.")
            diastolic?.setText("")
        }
    }
    fun String.isNumeric(): Boolean = this.matches("\\d+".toRegex())

    companion object {
        private const val ARG_PATIENT_ID = "patientID"
        private const val ARG_USER_ID = "userID"
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(userID: Int, patientID: Int): BPFragment {
            val fragment = BPFragment()
            val args = Bundle()
            args.putInt(ARG_USER_ID, userID)
            args.putInt(ARG_PATIENT_ID, patientID)
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BPFragment.
         */
        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            BPFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }

}