package com.example.songapp

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialogo.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMain.setOnClickListener(){
            saluda()
        }
    }
    fun saluda(){
        val dialog = LayoutInflater.from(this).inflate(R.layout.dialogo, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(dialog)
            .setTitle("Ingresar un nuevo recordatorio")
        val  mAlertDialog = mBuilder.show()
        dialog.cerrar.setOnClickListener(){
            mAlertDialog.dismiss()
        }
    }
}