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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialogo.*
import kotlinx.android.synthetic.main.dialogo.view.*
import kotlinx.android.synthetic.main.dialogo.view.remember

class MainActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        val name: String = user.uid
        saludo.setText("Hola "+user.email)
        btnMain.setOnClickListener(){
            newRemember()
        }
    }
    fun newRemember(){
        val dialog = LayoutInflater.from(this).inflate(R.layout.dialogo, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(dialog)
            .setTitle("Ingresar un nuevo recordatorio")
        val  mAlertDialog = mBuilder.show()
        dialog.cerrar.setOnClickListener(){
            database = FirebaseDatabase.getInstance()
            databaseReference = database.reference.child("Users/${user.uid}")
            //Declare and Initialise the Task
            val task = Task.create()
            //Set Task Description and isDone Status
            task.taskDesc = dialog.remember.text.toString()
            task.taskAut = user.email
            task.done = false

            val newTask= databaseReference.child("task").push()
            task.objectId = newTask.key
            newTask.setValue(task)
            mAlertDialog.dismiss()
            Toast.makeText(this, "Se ha guardado tu recordatorio", Toast.LENGTH_SHORT).show()
        }
    }
}