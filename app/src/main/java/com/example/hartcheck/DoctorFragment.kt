package com.example.hartcheck

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
import com.example.hartcheck.Adapter.ListAdapter
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.Remote.PatientsDoctorRemote.PatientsDoctorInstance
import com.example.hartcheck.Wrapper.DoctorInfoList
import com.example.hartcheck.Wrapper.PatientsDoctorAssign
import com.example.hartcheck.Wrapper.PatientsDoctorList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DoctorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DoctorFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var userID: Int? = null
    private var patientID: Int? = null

    private lateinit var txt_emp:TextView
    private lateinit var btnbackdoctor: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: ListAdapter
    private lateinit var doctorList: MutableList<DocData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            userID = it.getInt(ARG_USER_ID)
            patientID = it.getInt(ARG_PATIENT_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor, container, false)
        val frag = false

        txt_emp = view.findViewById(R.id.txt_empty)
        btnbackdoctor = view.findViewById(R.id.btn_back_doctor)

        doctorList = mutableListOf()
        GetDoctorsByPatientId(patientID!!) { doctorInfoList ->
            for (doctor in doctorInfoList.DoctorInfo) {
                val doctorName = "${doctor.firstName} ${doctor.lastName}"
                val docData = DocData(doctorSchedID = null, doctorID = doctor.doctorID, doctorName, "")
                doctorList.add(docData)
            }
            listAdapter.notifyDataSetChanged()
        }//this

//        getDoctorAssign(patientID!!) { doctorAssign ->
//            for (doctor in doctorAssign.HealthCareName) {
//                val doctorName = "${doctor.firstName} ${doctor.lastName}"
//                val docData = DocData(doctorSchedID = null, doctorName, "")
//                doctorList.add(docData)
//            }
//            listAdapter.notifyDataSetChanged()
//        }

        recyclerView = view.findViewById(R.id.consulList)//test
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        listAdapter = ListAdapter(doctorList,frag, patientID, userID, BookActivity::class.java)
        recyclerView.adapter = listAdapter
        //Default
        txt_emp.visibility = View.GONE//there are no currently available doctors (put a null checker to show or not)
        recyclerView.visibility = View.VISIBLE

        btnbackdoctor.setOnClickListener {
            replaceFragment(ConsultationFragment.newInstance(userID!!, patientID!!))
        }
        return view
    }
//    private fun getDoctorAssign(patientID: Int, onDoctorAssignRetrieved: (doctorAssign: PatientsDoctorAssign) -> Unit) {
//        val service = PatientsDoctorInstance.retrofitBuilder
//
//        service.getHealthCareProfessionals(patientID).enqueue(object : Callback<PatientsDoctorAssign> {
//            override fun onResponse(call: Call<PatientsDoctorAssign>, response: Response<PatientsDoctorAssign>) {
//                if (response.isSuccessful) {
//                    response.body()?.let { doctorAssign ->
//                        onDoctorAssignRetrieved(doctorAssign)
//                    }
//                } else {
//                    Log.d("TestActivity", "Error: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<PatientsDoctorAssign>, t: Throwable) {
//                Log.d("TestActivity", "Failure: ${t.message}")
//            }
//        })
//    }

    private fun GetDoctorsByPatientId(patientID: Int, onDoctorsRetrieved: (doctorInfoList: DoctorInfoList) -> Unit) {
        val service = PatientsDoctorInstance.retrofitBuilder

        service.getDoctorsByPatientId(patientID).enqueue(object : Callback<DoctorInfoList> {
            override fun onResponse(call: Call<DoctorInfoList>, response: Response<DoctorInfoList>) {
                if (response.isSuccessful) {
                    response.body()?.let { doctorInfoList ->
                        onDoctorsRetrieved(doctorInfoList)
                    }
                } else {
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DoctorInfoList>, t: Throwable) {
                Log.d("TestActivity", "Failure: ${t.message}")
            }
        })
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.frame_layout, fragment)
        fragmentTransaction?.commit()
    }


    companion object {
        private const val ARG_PATIENT_ID = "patientID"
        private const val ARG_USER_ID = "userID"
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"


        @JvmStatic
        fun newInstance(userID: Int, patientID: Int): DoctorFragment {
            val fragment = DoctorFragment()
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