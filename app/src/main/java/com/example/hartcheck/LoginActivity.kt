package com.example.hartcheck

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.hartcheck.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var gradAnim: AnimationDrawable
    private lateinit var constLayout: ConstraintLayout
    private lateinit var googleSignIn: ImageButton
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        googleSignIn = findViewById(R.id.btn_google_reg)

        bgAnimationStart()
        googleSignInAccount()

//        binding.btnStart.setOnClickListener {
//            startDifficulty()
//
//        }
        binding.btnMainLogin.setOnClickListener {
            val intent = Intent(this@LoginActivity, LoginMain::class.java)
            startActivity(intent)
        }
        binding.btnMainRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity,  RegisterActivity::class.java)
            startActivity(intent)
        }
        googleSignIn.setOnClickListener {
            goToSignIn()

        }

    }
    private fun bgAnimationStart() {
        constLayout = binding.loginLayout
        gradAnim = constLayout.background as AnimationDrawable
        gradAnim.setEnterFadeDuration(2500)
        gradAnim.setExitFadeDuration(5000)
        gradAnim.start()
    }
    private fun googleSignInAccount(): GoogleSignInAccount? {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)

        val account: GoogleSignInAccount? = GoogleSignIn
            .getLastSignedInAccount(this)
        if(account!=null){
            goToHome(account.email)
        }
        return account
    }
    private fun goToSignIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode==1000){
            val task: Task<GoogleSignInAccount> = GoogleSignIn
                .getSignedInAccountFromIntent(data)

            try{
                val account = task.getResult(ApiException::class.java)
                goToHome(account.email)

            }
            catch (e:java.lang.Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToHome(email: String?) {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        finish()
    }
}