package com.example.hartcheck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.graphics.Color
import android.widget.EditText
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat

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
        val patientID = arguments?.getInt(BPFragment.ARG_PATIENT_ID)

        val chart: LineChart = view.findViewById(R.id.lineChart)

        val entries1 = mutableListOf<Entry>()
        val entries2 = mutableListOf<Entry>()

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
    private fun readCSVFile() {//THIS SHIT WORKS
//        val textView = findViewById<TextView>(R.id.CSV)
//        val edittext = findViewById<EditText>(R.id.edit_text_csv)
//        val edittext2 = findViewById<EditText>(R.id.edit_text_csv2)
//        val bufferReader = BufferedReader(assets.open("com.samsung.shealth.blood_pressure.20230706212807.csv").reader())
//        val csvParser = CSVParser.parse(
//            bufferReader,
//            CSVFormat.DEFAULT
//        )
//        csvParser.forEach{
//            it?.let{
//                val datetime = it.get(3)
//                val systolic = it.get(13)
//                val diastolic = it.get(11)
//                if (systolic.isNumeric() && diastolic.isNumeric()) {
//                    list.add(Triple(datetime, systolic, diastolic))
//                }
//            }
//        }
    }
    private fun displayNextPair() {
//        val edittext = findViewById<EditText>(R.id.edit_text_csv)
//        val edittext2 = findViewById<EditText>(R.id.edit_text_csv2)
//        val edittext3 = findViewById<EditText>(R.id.edit_text_csv3)

//        if (currentIndex < list.size) {
//            val triple = list[currentIndex]
//            edittext.setText(triple.first)
//            edittext2.setText(triple.second)
//            edittext3.setText(triple.third)
//            currentIndex++
//        } else {
//            edittext.setText("No more triples in the list.")
//            edittext2.setText("")
//            edittext3.setText("")
//        }
    }
    fun String.isNumeric(): Boolean = this.matches("\\d+".toRegex())

    companion object {
        private const val ARG_PATIENT_ID = "patientID"
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(patientID: Int): BPFragment {
            val fragment = BPFragment()
            val args = Bundle()
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