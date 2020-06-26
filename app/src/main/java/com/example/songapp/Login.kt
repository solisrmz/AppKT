package com.example.songapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlin.properties.Delegates

class Login : AppCompatActivity() {
    private val TAG = "LoginActivity"
    //global variables
    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    //Instancia para autentificar
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        initialise()
    }

    private fun initialise() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        mAuth = FirebaseAuth.getInstance()
    }

    private fun loginUser() {
        //Obtenemos usuario y contraseña
        email = etEmail.text.toString()
        password = etPassword.text.toString()
        //Verificamos que los campos no este vacios
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            //Iniciamos sesión con el método signIn y enviamos usuario y contraseña
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    //Verificamos que la tarea se ejecutó correctamente
                        task ->
                    if (task.isSuccessful) {
                        // Si se inició correctamente la sesión vamos a la vista Home de la aplicación
                        inicio(email) // Creamos nuestro método en la parte de abajo
                    } else {
                        // sino le avisamos el usuairo que orcurrio un problema
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

    fun inicio(mail:String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("mail", mail)
        startActivity(intent)
    }

    //Eventos onclick
    fun login(v:View){
        loginUser()
    }

    fun register(v:View){
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }
}