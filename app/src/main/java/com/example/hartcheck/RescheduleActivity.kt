package com.example.hartcheck

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.hartcheck.R

class RescheduleActivity : AppCompatActivity() {

    private lateinit var btn_resched: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reschedule)

        btn_resched = findViewById(R.id.btn_resched_appointment)

        btn_resched.setOnClickListener {
            showModal()
        }

    }

    private fun showModal(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_confirmation)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val modalText: TextView = dialog.findViewById(R.id.txt_modal)
        val btnAccept:Button = dialog.findViewById(R.id.btn_modal_yes)
        val btnClose:Button = dialog.findViewById(R.id.btn_modal_no)

        modalText.setText(R.string.p_resched_modal)

        btnAccept.setOnClickListener {
            //add resched here
            dialog.dismiss()
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}