package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleSign : AppCompatActivity() {

    private lateinit var  gImage:ImageView
    private lateinit var gName:TextView
    private lateinit var gEmail:TextView
    private lateinit var gID:TextView
    private lateinit var gSignOut:Button

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign)


        gImage = findViewById(R.id.g_image)
        gName = findViewById(R.id.g_name)
        gEmail = findViewById(R.id.g_email)
        gID = findViewById(R.id.g_id)
        gSignOut = findViewById(R.id.g_sign_out)


        GoogleSigned()
        gSignOut.setOnClickListener {
            goSignOut()
        }
    }

    private fun GoogleSigned() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)

        val account: GoogleSignInAccount? = GoogleSignIn
            .getLastSignedInAccount(this)

        if (account != null) {
            gName.text = account.displayName
            gEmail.text = account.email
            gID.text = account.id
        } else {
            goSignOut()
        }
    }

    private fun goSignOut() {
        gsc.signOut().addOnSuccessListener {
            startActivity(Intent(this, LoginMain::class.java))
            finish()
        }
    }
}