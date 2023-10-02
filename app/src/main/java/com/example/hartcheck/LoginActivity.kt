package com.example.hartcheck

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.hartcheck.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var gradAnim: AnimationDrawable
    private lateinit var constLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        bgAnimationStart()

//        binding.btnStart.setOnClickListener {
//            startDifficulty()
//
//        }
    }
    private fun bgAnimationStart() {
        constLayout = binding.loginLayout
        gradAnim = constLayout.background as AnimationDrawable
        gradAnim.setEnterFadeDuration(2500)
        gradAnim.setExitFadeDuration(5000)
        gradAnim.start()
    }
}