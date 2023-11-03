package com.example.hartcheck

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Adapter.ListAdapter
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.ConsultationRemote.ConsultationInstance
import com.example.hartcheck.Wrapper.DoctorScheduleDates
import com.example.hartcheck.Wrapper.PatientsDoctorAssign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [ConsultationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConsultationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val CHANNEL_ID ="Your_Channel_ID"
    private val notificationID = 101

    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: ListAdapter
    private lateinit var doctorList: MutableList<DocData>
    private lateinit var btn_avail: Button
    private lateinit var txt_emp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val patientID = arguments?.getInt(ARG_PATIENT_ID)
        val userID = arguments?.getInt(ARG_USER_ID)
//        val doctorAssign = arguments?.getParcelable<PatientsDoctorAssign>(ARG_DOCTOR_ASSIGN)
//        val datesAssign = arguments?.getParcelable<DoctorScheduleDates>(ARG_DATE_ASSIGN)
//        val doctorSchedules = arguments?.getParcelable<DoctorScheduleDates>(ARG_DOCTOR_SCHEDULE)
//        val doctorsInfo = arguments?.getParcelableArrayList<Users>(ARG_DOCTOR_INFO)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_consultation, container, false)
        val frag = true

        btn_avail = view.findViewById(R.id.btn_view_avail)
        txt_emp = view.findViewById(R.id.txt_empty)

//        line.visibility = View.VISIBLE
//        txt_appointment.visibility =View.VISIBLE
//        txt_title.visibility = View.VISIBLE

//        doctorList = mutableListOf(
//            DocData("Doctor 1", "Info 1"),
//            DocData("Doctor 2", "Info 2"),
//            DocData("Doctor 3", "Info 3")
//        )

//        doctorList = mutableListOf<DocData>()
//        for ((index, doctor) in doctorsInfo!!.withIndex()) {
//            val doctorName = "${doctor.firstName} ${doctor.lastName}"
//            val scheduleInfo = doctorSchedules?.DoctorDates?.get(index)?.let {
//                "${it.schedDateTime?.let { it1 ->
//                    formatDateTime(
//                        it1
//                    )
//                }}"
//            } ?: "No schedule info available"
//            val docData = DocData(doctorName, scheduleInfo)
//            doctorList.add(docData)
//        }

        doctorList = mutableListOf()
        getConsultationAssign(patientID!!) { doctorSchedules ->
            val doctorIDs = doctorSchedules.DoctorDates.mapNotNull { it.doctorID }
            GlobalScope.launch(Dispatchers.Main) {
                getDoctorInfo(requireContext(), doctorIDs) { doctorsInfo ->
                    for ((index, doctor) in doctorsInfo.withIndex()) {
                        val doctorName = "${doctor.firstName} ${doctor.lastName}"
                        val scheduleInfo = doctorSchedules.DoctorDates[index].schedDateTime
                        doctorList.add(DocData(doctorName, formatDateTime(scheduleInfo!!)))
                    }
                    listAdapter = ListAdapter(doctorList,frag, patientID)
                    recyclerView.adapter = listAdapter
                }
            }
        }



        recyclerView = view.findViewById(R.id.consulList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        listAdapter = ListAdapter(doctorList,frag, patientID)
        recyclerView.adapter = listAdapter

        //enable this as default
        txt_emp.visibility = View.GONE// you don't have any appointments today (put a null checker to show or not)
        btn_avail.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE

        btn_avail.setOnClickListener {//available doctors
            replaceFragment(DoctorFragment.newInstance(userID!!,patientID))
        }
//        val names = datesAssign?.DoctorDates?.joinToString(separator = ", ") { "${it.doctorID} ${it.doctorSchedID} ${it.schedDateTime}" }
//        Toast.makeText(context, "GOT UR:$names", Toast.LENGTH_SHORT).show()

//        val names = doctorSchedules?.DoctorDates?.joinToString(separator = ", ") { "${it.doctorID} ${it.doctorSchedID} ${it.schedDateTime}" }
//        if (doctorsInfo != null) {
//            val doctorsInfoString = StringBuilder()
//            for (doctor in doctorsInfo) {
//                doctorsInfoString.append("Name: ${doctor.firstName} ${doctor.lastName}\n")
//            }
//            Toast.makeText(context, doctorsInfoString.toString(), Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(context, "No doctors info available", Toast.LENGTH_LONG).show()
//        }
//
//        Toast.makeText(context, "$patientID and $userID", Toast.LENGTH_SHORT).show()

        return view
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.frame_layout, fragment)
        fragmentTransaction?.commit()
    }
    private fun getConsultationAssign(patientID: Int, onConsultationAssignRetrieved: (doctorSchedules: DoctorScheduleDates) -> Unit) {
        val service = ConsultationInstance.retrofitBuilder

        service.getConsultationAssign(patientID).enqueue(object : Callback<DoctorScheduleDates> {
            override fun onResponse(call: Call<DoctorScheduleDates>, response: Response<DoctorScheduleDates>) {
                if (response.isSuccessful) {
                    val doctorSchedules = response.body()
                    if (doctorSchedules != null) {
                        val doctorIDs = doctorSchedules.DoctorDates.map { it.doctorID }
                        GlobalScope.launch(Dispatchers.Main) {
                            getDoctorInfo(requireContext(), doctorIDs) { doctorsInfo ->
                                onConsultationAssignRetrieved(doctorSchedules)
                                // Handle the doctorsInfo
                            }
                        }
                    }
                } else {
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DoctorScheduleDates>, t: Throwable) {
                Log.d("TestActivity", "Failure: ${t.message}")
            }
        })
    }


    private suspend fun getDoctorInfo(context: Context, doctorIDs: List<Int?>, onDoctorInfoRetrieved: (doctorsInfo: ArrayList<Users>) -> Unit) {
        val service = ConsultationInstance.retrofitBuilder
        val doctorsInfo = ArrayList<Users>()

        for (doctorID in doctorIDs) {
            val doctorInfo = service.getConsultationDoctor(doctorID!!).await()
            if (doctorInfo != null) {
                doctorsInfo.add(doctorInfo)
            }
        }
        onDoctorInfoRetrieved(doctorsInfo)
    }


    private fun Booked() {
        val startMillis: Long = Calendar.getInstance().run {
            set(2023, 10, 19, 7, 30)
            timeInMillis
        }
        val endMillis: Long = Calendar.getInstance().run {
            set(2023, 10, 19, 8, 30)
            timeInMillis
        }
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            .putExtra(CalendarContract.Events.TITLE, "Meeting")
            .putExtra(CalendarContract.Events.DESCRIPTION, "Team Meeting")
            .putExtra(CalendarContract.Events.EVENT_LOCATION, "Office")
            .putExtra(
                CalendarContract.Events.AVAILABILITY,
                CalendarContract.Events.AVAILABILITY_BUSY
            )
        startActivity(intent)
        // Check if the event has been created
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            createNotification()
        }, 10000)  // Delay of 10 seconds
    }
    private fun createNotification() {
        val permission = "android.permission.POST_NOTIFICATIONS"
        val hasPermission = ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle("Booked Doctor's Appointment")
            .setContentText("You have successfully booked an appointment.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if(hasPermission){
            with(NotificationManagerCompat.from(requireContext())) {
                notify(notificationID, builder.build())
            }
        }
        else{

        }
    }

    private fun formatDateTime(originalDateTime: String): String {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss"
        val outputPattern = "MMMM dd, yyyy 'at' hh:mm a"

        val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.US)

        val dateTime = inputFormat.parse(originalDateTime)
        return outputFormat.format(dateTime)
    }

    companion object {
        private const val ARG_PATIENT_ID = "patientID"
        private const val ARG_USER_ID = "userID"
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
//        private const val ARG_DOCTOR_ASSIGN = "doctorAssign"
//        private const val ARG_DATE_ASSIGN = "datesAssign"
//        private const val ARG_DOCTOR_INFO = "doctorSchedules"
//        private const val ARG_DOCTOR_SCHEDULE = "doctorsInfo"

        @JvmStatic
        fun newInstance(userID: Int, patientID: Int): ConsultationFragment {
            val fragment = ConsultationFragment()
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
    }
}