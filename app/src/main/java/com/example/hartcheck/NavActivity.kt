package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.hartcheck.databinding.ActivityNavBinding

class NavActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_states()

        binding.navBar.setOnItemSelectedListener {
            when(it){

                R.id.nav_profile -> replaceFragment(UserFragment())
                R.id.nav_consultations -> replaceFragment(ConsultationFragment())
                R.id.nav_bp -> replaceFragment(BPFragment())
                R.id.nav_chat -> replaceFragment(ChatFragment())
                else ->{

                }


            }
            true
        }


    }

    private fun btn_states(){
        val buttonState = intent.getStringExtra("BUTTON_STATE")

        when (buttonState) {
            "btn_bp" -> {
                replaceFragment(BPFragment())
            }
            "btn_consul" -> {
                replaceFragment(ConsultationFragment())
            }
            "btn_chat" -> {
                replaceFragment(ChatFragment())
            }
            "btn_profile" -> {
                replaceFragment(UserFragment())
            }
            else -> {

            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}