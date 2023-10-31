package com.example.hartcheck

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Adapter.ListAdapter
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.Wrapper.PatientsDoctorAssign
import okhttp3.internal.notify
import java.util.Calendar

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
    private lateinit var line: ImageView
    private lateinit var txt_appointment:TextView
    private lateinit var txt_title:TextView

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
        val doctorAssign = arguments?.getParcelable<PatientsDoctorAssign>(ARG_DOCTOR_ASSIGN)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_consultation, container, false)
        val frag = true

        btn_avail = view.findViewById(R.id.btn_view_avail)
        txt_emp = view.findViewById(R.id.txt_empty)
//
//        line.visibility = View.VISIBLE
//        txt_appointment.visibility =View.VISIBLE
//        txt_title.visibility = View.VISIBLE

        doctorList = mutableListOf(
            DocData("Doctor 1", "Info 1"),
            DocData("Doctor 2", "Info 2"),
            DocData("Doctor 3", "Info 3")
        )

        recyclerView = view.findViewById(R.id.consulList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        listAdapter = ListAdapter(doctorList,frag)
        recyclerView.adapter = listAdapter

        //enable this as default
        txt_emp.visibility = View.VISIBLE
        btn_avail.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        btn_avail.setOnClickListener {
            userID?.let { it1 -> patientID?.let { it2 ->
                doctorAssign?.let { it3 ->
                    DoctorFragment.newInstance(it1,
                        it2, it3
                    )
                }
            } }
                ?.let { it2 -> replaceFragment(it2) }
        }
        return view
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.frame_layout, fragment)
        fragmentTransaction?.commit()
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
        }, 10000)  // Delay of 8 seconds
    }
    private fun createNotification() {
        val permission = "android.permission.POST_NOTIFICATIONS"
        val hasPermission = ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
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

    companion object {
        private const val ARG_PATIENT_ID = "patientID"
        private const val ARG_USER_ID = "userID"
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_DOCTOR_ASSIGN = "doctorAssign"

        @JvmStatic
        fun newInstance(userID: Int, patientID: Int, doctorAssign: PatientsDoctorAssign): ConsultationFragment {
            val fragment = ConsultationFragment()
            val args = Bundle()
            args.putInt(ARG_USER_ID, userID)
            args.putInt(ARG_PATIENT_ID, patientID)
            args.putParcelable(ARG_DOCTOR_ASSIGN, doctorAssign)
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