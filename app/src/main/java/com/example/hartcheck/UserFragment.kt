package com.example.hartcheck

import android.app.Dialog
import android.content.Context
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
import com.example.hartcheck.Data.OTPVerification
import com.example.hartcheck.Data.UserConfirm
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import okhttp3.ResponseBody
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
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var gsc: GoogleSignInClient
    private lateinit var patientEmail: String
    private var otpHash: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        //put some methods
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val userID = arguments?.getInt(ARG_USER_ID)
        val patientID = arguments?.getInt(ARG_PATIENT_ID)
        val patientName = arguments?.getString(ARG_PATIENT_NAME)
        val view = inflater.inflate(R.layout.fragment_user, container, false)


        val btn_back_userProfile = view.findViewById<Button>(R.id.btn_back_userProfile)
        val btn_edit_profile = view.findViewById<Button>(R.id.btn_edit_profile)
        val btn_logout = view.findViewById<Button>(R.id.btn_logout)
        val btn_change = view.findViewById<Button>(R.id.btn_change_pass)
        GoogleSignInOptions()

        btn_back_userProfile.setOnClickListener {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra("userID", userID)
            intent.putExtra("patientID", patientID)
            intent.putExtra("patientName", patientName)
            startActivity(intent)
        }
        btn_logout.setOnClickListener {
            val intent = Intent(activity, LoginMain::class.java)
            GoogleSignOut()
            startActivity(intent)
        }
        btn_edit_profile.setOnClickListener {
//            replaceFragment(EditProfileFragment.newInstance(userID!!, patientName!!))
//            val intent = Intent(activity, ConfirmUserEmailActivity::class.java)
//            intent.putExtra("userID", userID)
//            intent.putExtra("patientID", patientID)
//            intent.putExtra("patientName", patientName)
//            startActivity(intent)
            if (userID != null) {
                if (patientName != null) {
                    if (patientID != null) {
                        showModal(userID, patientName, patientID)
                    }
                }
            }


        }
        btn_change.setOnClickListener {
            val intent = Intent(activity, ForgotActivity::class.java)
            val change = true
            intent.putExtra("change", change)
            startActivity(intent)
        }

        viewUser()


        return view
    }//otp in da modal

    private fun showModal(userID: Int, patientName: String, patientID: Int){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_otp)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val userOtp: EditText = dialog.findViewById(R.id.edit_userOtp)
        val sentOTP: TextView = dialog.findViewById(R.id.sent_OTP)
        val btn_close:Button = dialog.findViewById(R.id.btn_modal_deny)
        val btn_confirm:Button = dialog.findViewById(R.id.btn_modal_confirm)


        btn_confirm.setOnClickListener {//confirm the OTP and hash
//            insertBP(dialog)
//            confirmUser(patientID, requireContext())//call the email automatically
//            replaceFragment(EditProfileFragment.newInstance(userID, patientName))
            verifyOTP(dialog, userID, patientName)
//
        }
        sentOTP.setOnClickListener {
            confirmUser(requireContext())
        }

        btn_close.setOnClickListener {
            dialog.dismiss()
        }
        getpatientEmail(patientID)
        dialog.show()
    }

    private fun viewUser() {
        val userID = arguments?.getInt(ARG_USER_ID)
        val service = UsersInstance.retrofitBuilder
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        userID?.let {
            service.getRegisterUsersID(it).enqueue(object : Callback<Users> {
                override fun onResponse(call: Call<Users>, response: Response<Users>) {
                    if(response.isSuccessful){
                        val user = response.body()
                        user?.let {
                            currentUser = it
                            val firstNameEditText = view?.findViewById<EditText>(R.id.input_fn_profile)
                            val lastNameEditText = view?.findViewById<EditText>(R.id.input_ln_profile)
                            val emailEditText = view?.findViewById<EditText>(R.id.input_email_profile)
                            val genderEditText = view?.findViewById<EditText>(R.id.input_gender_profile)
                            val birthdateEditText = view?.findViewById<EditText>(R.id.input_birthdate_profile)
                            val phoneEditText = view?.findViewById<EditText>(R.id.input_phone_profile)

                            firstNameEditText?.setText(it.firstName)
                            lastNameEditText?.setText(it.lastName)
                            emailEditText?.setText(it.email)
//                            genderEditText?.setText(it.gender?.toString())
                            genderEditText?.let { genderEdit ->
                                when (it.gender) {
                                    0 -> genderEdit.setText("Male")
                                    1 -> genderEdit.setText("Female")
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
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.frame_layout, fragment)
        fragmentTransaction?.commit()
    }
    private fun GoogleSignOut() {
        gsc.signOut().addOnSuccessListener {
            startActivity(Intent(context, LoginMain::class.java))
//            finish()
        }
    }
    private fun confirmUser(context: Context) {
        val userConfirm = UserConfirm(email = patientEmail)
        val userConfirmService = UsersInstance.retrofitBuilder
        userConfirmService.confirmUser(userConfirm).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                if (response.isSuccessful) {
                    otpHash = response.body()?.string() ?: ""
//                    Toast.makeText(context, "OTP Hash: $otpHash", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(context, "Registration DENIED", Toast.LENGTH_SHORT).show()
                    response.errorBody()?.let { errorBody ->
                        Log.d("MainActivity", "Response: ${errorBody.string()}")
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d ("MainActivity", "Registration failed: ${t}\"")
            }
        })
    }

    private fun getpatientEmail(patientID: Int){
        val service = PatientsInstance.retrofitBuilder

        service.getEmailByPatientId(patientID).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    patientEmail = response.body() ?: ""
//                    Toast.makeText(context, "Patient Email: $patientEmail", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("MainActivity", "Failed to connect: " + response.code())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // Handle the failure
                Log.d("MainActivity", "Failed to connect: : " + t.message)
            }
        })
    }

    private fun verifyOTP(dialog: Dialog, userID: Int, patientName: String){
        val userOtp: EditText = dialog.findViewById(R.id.edit_userOtp)
        if (userOtp.text.isNotEmpty()){
            val otp = userOtp.text.toString()
//            Toast.makeText(context, "$otp, $patientEmail, $otpHash", Toast.LENGTH_SHORT).show()
            val verify = OTPVerification(otp = otp, email = patientEmail, otpHash = otpHash)
            val OTPVerifyService = UsersInstance.retrofitBuilder
            OTPVerifyService.VerifyOTP(verify).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                    if (response.isSuccessful) {
                        Toast.makeText(context, "OTP Verified", Toast.LENGTH_SHORT).show()
                        replaceFragment(EditProfileFragment.newInstance(userID, patientName))
                        dialog.dismiss()
                    }
                    else {
                        Toast.makeText(context, "Wrong OTP, try again", Toast.LENGTH_SHORT).show()
                        response.errorBody()?.let { errorBody ->
                            Log.d("MainActivity", "Response: ${errorBody.string()}")

                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d ("MainActivity", "Registration failed: ")
                }
            })
            // Use otp for verification
//            Toast.makeText(this, "Your OTP that you enter is: $otp", Toast.LENGTH_SHORT).show()

        } else {
            // Show a message or handle the error
        }
    }

    private fun GoogleSignInOptions() {
        val gso = com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val activity = activity
        if (activity != null) {
            gsc = GoogleSignIn.getClient(activity, gso)
        }
    }
    companion object {
        private const val ARG_USER_ID = "userID"
        private const val ARG_PATIENT_ID = "patientID"
        private const val ARG_PATIENT_NAME = "patientName"
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(userID: Int, patientID: Int, patientName: String): UserFragment {
            val fragment = UserFragment()
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