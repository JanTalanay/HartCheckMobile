package com.example.hartcheck

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Adapter.AppointmentAdapter
import com.example.hartcheck.Adapter.ListAdapter
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.ConsultationRemote.ConsultationInstance
import com.example.hartcheck.Wrapper.DoctorScheduleDates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppointHistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppointHistFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: ListAdapter
    private lateinit var appointAdapter: AppointmentAdapter
    private lateinit var doctorList: MutableList<DocData>
    private lateinit var btnbackapphist: Button
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
        val patientName = arguments?.getString(ARG_PATIENT_NAME)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_appoint_hist, container, false)
        val frag = true

        btnbackapphist = view.findViewById(R.id.btn_back_app_hist)
        txt_emp = view.findViewById(R.id.txt_empty)



        doctorList = mutableListOf()
        getConsultationAssign(patientID!!) { doctorSchedules ->
            val doctorIDs = doctorSchedules.DoctorDates.mapNotNull { it.doctorID }
            GlobalScope.launch(Dispatchers.Main) {
                getDoctorInfo(requireContext(), doctorIDs) { doctorsInfo ->
                    for ((index, doctor) in doctorsInfo.withIndex()) {
                        val doctorSchedID = doctorSchedules.DoctorDates[index].doctorSchedID
                        val doctorID = doctorSchedules.DoctorDates[index].doctorID
                        val doctorName = "${doctor.firstName} ${doctor.lastName}"
                        val scheduleInfo = doctorSchedules.DoctorDates[index].schedDateTime
                        doctorList.add(DocData(doctorSchedID!!,doctorID,doctorName, formatDateTime(scheduleInfo!!)))
                    }
                    appointAdapter = AppointmentAdapter(doctorList,frag, patientID,patientName = null, userID, AppointmentHistDetailsFragment::class.java)
                    recyclerView.adapter = appointAdapter
                }
            }
        }






        recyclerView = view.findViewById(R.id.consulList_apphist)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        appointAdapter = AppointmentAdapter(doctorList,frag, patientID,patientName = null, userID, AppointmentHistDetailsFragment::class.java)
        recyclerView.adapter = appointAdapter

        //enable this as default
        txt_emp.visibility = View.GONE// you don't have any appointments today (put a null checker to show or not)
        recyclerView.visibility = View.VISIBLE

        //code above causes the error and intenting problems




        btnbackapphist.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("patientID", patientID)
            intent.putExtra("patientName", patientName)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }
        return view
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.frame_layout, fragment)
        fragmentTransaction?.commit()
    }
    private fun formatDateTime(originalDateTime: String): String {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss"
        val outputPattern = "MMMM dd, yyyy 'at' hh:mm a"

        val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.US)

        val dateTime = inputFormat.parse(originalDateTime)
        return outputFormat.format(dateTime)
    }

    private fun getConsultationAssign(patientID: Int, onConsultationAssignRetrieved: (doctorSchedules: DoctorScheduleDates) -> Unit) {
        val consultationAssignService = ConsultationInstance.retrofitBuilder
        consultationAssignService.getConsultationAssign(patientID).enqueue(object :
            Callback<DoctorScheduleDates> {
            override fun onResponse(call: Call<DoctorScheduleDates>, response: Response<DoctorScheduleDates>) {
                if (response.isSuccessful) {
                    val doctorSchedules = response.body()
                    if (doctorSchedules != null) {
                        onConsultationAssignRetrieved(doctorSchedules)
                        val doctorIDs = doctorSchedules.DoctorDates.map { it.doctorID }
                        GlobalScope.launch(Dispatchers.Main) {
                            getDoctorInfo(requireContext(), doctorIDs) { doctorsInfo ->
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
        //getting the doctor first and last name base on the doctorID from getConsultationAssign
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

    companion object {
        private const val ARG_PATIENT_ID = "patientID"
        private const val ARG_USER_ID = "userID"
        private const val ARG_PATIENT_NAME = "patientName"
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(userID: Int, patientID: Int, patientName: String): AppointHistFragment {
            val fragment = AppointHistFragment()
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