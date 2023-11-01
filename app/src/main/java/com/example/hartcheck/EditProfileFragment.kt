package com.example.hartcheck

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var currentUser: Users? = null

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var userID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            userID = it.getInt(ARG_USER_ID)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        viewUser()
        val options = listOf("Select Gender", "Male", "Female")
        val input_gender = view.findViewById<Spinner>(R.id.input_gender_edit)
        val saveProfile = view.findViewById<Button>(R.id.btn_save_profile)
        val backEditProfile = view.findViewById<Button>(R.id.btn_back_edit_profile)
        val deleteAccount = view.findViewById<Button>(R.id.btn_delete_account)

        val adapter = ArrayAdapter(requireContext(), R.layout.app_list_item, options)
        adapter.setDropDownViewResource(R.layout.app_list_item)
        adapter.setDropDownViewResource(R.layout.app_list_item)
        input_gender.adapter = adapter

        saveProfile.setOnClickListener {
            updateProfile()
        }
        backEditProfile.setOnClickListener {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }
        deleteAccount.setOnClickListener {
            showModal()
        }
        return view

    }

    private fun showModal(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_confirmation)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val modalText: TextView = dialog.findViewById(R.id.txt_modal)
        val btnAccept:Button = dialog.findViewById(R.id.btn_modal_yes)
        val btnClose:Button = dialog.findViewById(R.id.btn_modal_no)

        modalText.setText(R.string.p_delete_acc)

        //add bp manually details here
        btnAccept.setOnClickListener {
            val intent = Intent(requireContext(),LoginActivity::class.java)
            deleteUser()
            dialog.dismiss()
            startActivity(intent)
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateProfile() {
        val updatedUser = Users(
            usersID = currentUser?.usersID,
            email = view?.findViewById<EditText>(R.id.input_email_edit)?.text.toString(),
            firstName = view?.findViewById<EditText>(R.id.input_fn_edit)?.text.toString(),
            lastName = view?.findViewById<EditText>(R.id.input_ln_edit)?.text.toString(),
            password = currentUser?.password,
            birthdate = view?.findViewById<EditText>(R.id.input_birthdate_edit)?.text.toString(),
            gender = currentUser?.gender,
            phoneNumber = view?.findViewById<EditText>(R.id.input_phone_edit)?.text.toString().toLong(),
            role = currentUser?.role
        )
        val service = UsersInstance.retrofitBuilder
        service.updateUser(currentUser?.usersID.toString(), updatedUser).enqueue(object :
            Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if(response.isSuccessful){
                    Toast.makeText(context, "User updated successfully", Toast.LENGTH_SHORT).show()
                    currentUser = response.body()
                } else {
                    Log.d("MainActivity", "Failed to connect: " + response.code())
                }
            }
            override fun onFailure(call: Call<Users>, t: Throwable) {
                Log.d ("MainActivity", "Failed to connect: : " + t.message)
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.string()
                    Log.d("MainActivity", "Error response: $errorResponse")
                }
            }
        })
    }

    private fun viewUser() {
        val service = UsersInstance.retrofitBuilder
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        userID?.let {
            service.getRegisterUsersID(it).enqueue(object : Callback<Users> {
                override fun onResponse(call: Call<Users>, response: Response<Users>) {
                    if(response.isSuccessful){
                        val user = response.body()
                        user?.let {
                            currentUser = it
                            val firstNameEditText = view?.findViewById<EditText>(R.id.input_fn_edit)
                            val lastNameEditText = view?.findViewById<EditText>(R.id.input_ln_edit)
                            val emailEditText = view?.findViewById<EditText>(R.id.input_email_edit)
                            val genderEditText = view?.findViewById<Spinner>(R.id.input_gender_edit)
                            val birthdateEditText = view?.findViewById<EditText>(R.id.input_birthdate_edit)
                            val phoneEditText = view?.findViewById<EditText>(R.id.input_phone_edit)

                            firstNameEditText?.setText(it.firstName)
                            lastNameEditText?.setText(it.lastName)
                            emailEditText?.setText(it.email)
//                            genderEditText?.setText(it.gender?.toString())
                            genderEditText?.let { spinner ->
                                when (it.gender) {
                                    0 -> spinner.setSelection(1) // Male
                                    1 -> spinner.setSelection(2) // Female
                                    else -> spinner.setSelection(0) // Default selection
                                }
                            }
//                            birthdateEditText?.setText(it.birthdate)
                            birthdateEditText?.let { birthdateEdit ->
                                val birthdate = dateFormat.parse(it.birthdate)
                                birthdateEdit.setText(dateFormat.format(birthdate))
                            }
                            phoneEditText?.setText(it.phoneNumber?.toString())
                        }
                    } else {
                        Log.d("MainActivity", "Failed to connect: " + response.code())
                    }
                }
                override fun onFailure(call: Call<Users>, t: Throwable) {
                    Log.d ("MainActivity", "Failed to connect: : " + t.message)
                    if (t is HttpException) {
                        val errorResponse = t.response()?.errorBody()?.string()
                        Log.d("MainActivity", "Error response: $errorResponse")
                    }
                }
            })
        }?: run {
        }
    }
    private fun deleteUser(){
        val userDeleteServices = UsersInstance.retrofitBuilder
        userID?.let {
            userDeleteServices.deleteUser(it).enqueue(object : Callback<Users> {
                override fun onResponse(call: Call<Users>, response: Response<Users>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "User is Deleted successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, LoginMain::class.java)
                        startActivity(intent)
                    } else {
                        Log.d("MainActivity", "Failed to connect: " + response.code())
                    }
                }

                override fun onFailure(call: Call<Users>, t: Throwable) {
                    Log.d ("MainActivity", "Failed to connect: : " + t.message)
                    if (t is HttpException) {
                        val errorResponse = t.response()?.errorBody()?.string()
                        Log.d("MainActivity", "Error response: $errorResponse")
                    }
                }
            })
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_USER_ID = "userID"

        @JvmStatic
        fun newInstance(userID: Int): EditProfileFragment {
            val fragment = EditProfileFragment()
            val args = Bundle()
            args.putInt(ARG_USER_ID, userID)
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