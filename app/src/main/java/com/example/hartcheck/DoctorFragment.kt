package com.example.hartcheck

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Adapter.ListAdapter
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.databinding.DoctorItemBinding

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

    private lateinit var txt_emp:TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: ListAdapter
    private lateinit var doctorList: List<DocData>
    private lateinit var doc_item: DoctorItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor, container, false)

        doc_item = DoctorItemBinding.inflate(inflater,container,false)
        //add doc_item propety

        txt_emp = view.findViewById(R.id.txt_empty)

        //Default
        txt_emp.visibility = View.GONE


        doctorList = listOf(
            DocData("Doctor 1", "Info 1"),
            DocData("Doctor 2", "Info 2"),
            DocData("Doctor 3", "Info 3")
        )

        recyclerView = view.findViewById(R.id.consulList)//test
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        listAdapter = ListAdapter(doctorList)
        recyclerView.adapter = listAdapter

        //for the recycler view
        doc_item.docLine.visibility = View.GONE
        doc_item.txtDocView.visibility =View.GONE
        doc_item.txtDocName.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DoctorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DoctorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}