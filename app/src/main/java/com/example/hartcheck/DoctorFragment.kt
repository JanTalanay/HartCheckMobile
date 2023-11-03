package com.example.hartcheck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Adapter.ListAdapter
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.Wrapper.DoctorScheduleDates
import com.example.hartcheck.Wrapper.PatientsDoctorAssign

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
    private lateinit var doctorAssign: PatientsDoctorAssign
    private lateinit var datesAssign: DoctorScheduleDates

    private lateinit var txt_emp:TextView
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
            doctorAssign = it.getParcelable<PatientsDoctorAssign>(ARG_DOCTOR_ASSIGN)!!
            datesAssign = it.getParcelable<DoctorScheduleDates>(ARG_DATES_ASSIGN)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor, container, false)
        val frag = false

        txt_emp = view.findViewById(R.id.txt_empty)

        //Default
        txt_emp.visibility = View.GONE

        doctorList = mutableListOf<DocData>()
        for (doctor in doctorAssign.HealthCareName) {
            val doctorName = "${doctor.firstName} ${doctor.lastName}"
            val docData = DocData(doctorName, "Info")
            doctorList.add(docData)
        }
//        doctorList = mutableListOf(
//            DocData("Doctor 1", "Info 1"),
//            DocData("Doctor 2", "Info 2"),
//            DocData("Doctor 3", "Info 3")
//        )

        recyclerView = view.findViewById(R.id.consulList)//test
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        listAdapter = ListAdapter(doctorList,frag,datesAssign,patientID)
        recyclerView.adapter = listAdapter

        recyclerView.visibility = View.VISIBLE

//        val names = doctorAssign.HealthCareName.joinToString(separator = ", ") { "${it.firstName} ${it.lastName}" }
//        Toast.makeText(context, "GOT UR: $userID", Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, "GOT UR: $patientID", Toast.LENGTH_SHORT).show()
//        val names = doctorAssign.HealthCareName.joinToString(separator = ", ") { "${it.firstName} ${it.lastName}" }
//        Toast.makeText(context, "GOT UR: $names", Toast.LENGTH_SHORT).show()

//        val names = datesAssign.DoctorDates.joinToString(separator = ", ") { "${it.doctorID} ${it.doctorSchedID} ${it.schedDateTime}" }
//        Toast.makeText(context, "GOT UR:$names", Toast.LENGTH_SHORT).show()
        return view
    }

    companion object {
        private const val ARG_PATIENT_ID = "patientID"
        private const val ARG_USER_ID = "userID"
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_DOCTOR_ASSIGN = "doctorAssign"
        private const val ARG_DATES_ASSIGN = "datesAssign"


        @JvmStatic
        fun newInstance(userID: Int, patientID: Int, doctorAssign: PatientsDoctorAssign, datesAssign: DoctorScheduleDates): DoctorFragment {
            val fragment = DoctorFragment()
            val args = Bundle()
            args.putInt(ARG_USER_ID, userID)
            args.putInt(ARG_PATIENT_ID, patientID)
            args.putParcelable(ARG_DOCTOR_ASSIGN, doctorAssign)
            args.putParcelable(ARG_DATES_ASSIGN, datesAssign)
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