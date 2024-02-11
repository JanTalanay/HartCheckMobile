package com.example.hartcheck

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Adapter.AppointmentAdapter
import com.example.hartcheck.Adapter.ConsultationAdapter
import com.example.hartcheck.Adapter.ExpandableAdapter
import com.example.hartcheck.Data.ConsultationData
import com.example.hartcheck.Data.DocData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppointmentHistDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppointmentHistDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var consultationAdapter: ConsultationAdapter
    //private lateinit var consulList: MutableList<ConsultationData>
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
        val view = inflater.inflate(R.layout.fragment_appointment_hist_details, container, false)
        val frag = true

//        recyclerView = view.findViewById(R.id.consulList)
//        btnbackapphist = view.findViewById(R.id.btn_back_appoint_hist)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        consultationAdapter = ConsultationAdapter(consulList,frag, patientID,patientName = null, userID, AppointmentHistDetailsFragment::class.java)
//        recyclerView.adapter = consultationAdapter

        //enable this as default
//        txt_emp.visibility = View.GONE// you don't have any appointments today (put a null checker to show or not)
//        recyclerView.visibility = View.VISIBLE

//        val history = listOf(
//            ConsultationData("Condition           v","condition goes here"),//use get conditions here
//            ConsultationData("Diagnosis           v","diagnosis goes here"),
//            ConsultationData("Prescription        v","medicine goes here")
//
//        )


//        consultationAdapter = ConsultationAdapter(history)
//        recyclerView.adapter = consultationAdapter


        btnbackapphist.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)//change
            intent.putExtra("patientID", patientID)
            intent.putExtra("patientName", patientName)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }
        return view

    }

    companion object {
        private const val ARG_PATIENT_ID = "patientID"
        private const val ARG_USER_ID = "userID"
        private const val ARG_PATIENT_NAME = "patientName"
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(patientID: Int, patientName: String, userID: Int): AppointmentHistDetailsFragment {
            val fragment = AppointmentHistDetailsFragment()
            val args = Bundle()
            args.putInt("patientID", patientID)
            args.putString("patientName", patientName)
            args.putInt("userID", userID)
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