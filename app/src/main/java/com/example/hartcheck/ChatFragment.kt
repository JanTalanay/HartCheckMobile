package com.example.hartcheck

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
import com.example.hartcheck.Adapter.ListAdapter
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.Remote.PatientsDoctorRemote.PatientsDoctorInstance
import com.example.hartcheck.Wrapper.DoctorInfoList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var userID: Int? = null
    private var patientID: Int? = null

    private lateinit var txt_emp: TextView
    private lateinit var btnbackchat: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: ListAdapter
    private lateinit var doctorList: MutableList<DocData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
//            userID = it.getInt(ARG_USER_ID)
//            patientID = it.getInt(ARG_PATIENT_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val patientID = arguments?.getInt(ARG_PATIENT_ID)
        val userID = arguments?.getInt(ARG_USER_ID)
        val patientName = arguments?.getString(ARG_PATIENT_NAME)

        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        val frag = false

        txt_emp = view.findViewById(R.id.txt_empty)
        btnbackchat = view.findViewById(R.id.btn_back_chat)

        doctorList = mutableListOf()
        GetDoctorsByPatientId(patientID!!) { doctorInfoList ->
            for (doctor in doctorInfoList.DoctorInfo) {
                val doctorName = "${doctor.firstName} ${doctor.lastName}"
                val docData = DocData(doctorSchedID = null, doctorID = doctor.doctorID, doctorName, "")
                doctorList.add(docData)
            }
            listAdapter.notifyDataSetChanged()
        }//this
        recyclerView = view.findViewById(R.id.consulList)//test
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        listAdapter = ListAdapter(doctorList,frag, patientID,patientName, userID, ChatMainActivity::class.java)
        recyclerView.adapter = listAdapter
        //Default
        txt_emp.visibility = View.GONE//there are no currently available doctors (put a null checker to show or not)
        recyclerView.visibility = View.VISIBLE

        btnbackchat.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("patientID", patientID)
            intent.putExtra("userID", userID)
            intent.putExtra("patientName", patientName)
            startActivity(intent)
        }
        return view
    }

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

    companion object {
        private const val ARG_PATIENT_ID = "patientID"
        private const val ARG_USER_ID = "userID"
        private const val ARG_PATIENT_NAME = "patientName"
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(userID: Int, patientID: Int, patientName: String): ChatFragment {
            val fragment = ChatFragment()
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