package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.hartcheck.databinding.ActivityNavBinding

class NavActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        binding = ActivityNavBinding.inflate(layoutInflater)
        replaceFragment(BPFragment())//activate bp or sumthn

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

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}