package com.example.songapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.ViewAnimator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.properties.Delegates

class Register : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    private var name by Delegates.notNull<String>()
    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        initialise()
    }

    private fun initialise() {
        //llamamos nuestras vista
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        /*Creamos una instancia para guardar los datos del usuario en nuestra base  de datos*/
        database = FirebaseDatabase.getInstance()
        /*Creamos una instancia para crear nuestra autenticación y guardar el usuario*/
        auth = FirebaseAuth.getInstance()
        /*reference nosotros leemos o escribimos en una ubicación específica la base de datos*/
        databaseReference = database.reference.child("Users")
    }

    private fun createAccount() {
        //Obtenemos los datos de nuestras cajas de texto
        name = etName.text.toString()
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        //Verificamos que los campos estén llenos
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            //vamos a dar de alta el usuario con el correo y la contraseña
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                //Si está en este método quiere decir que salio bien

                    /*Vamos a obtener el id del usuario con que accedio con currentUser*/
                    val user: FirebaseUser = auth.currentUser!!
                    //enviamos email de verificación a la cuenta del usuario
                    verifyEmail(user);
                    /*Damos de alta la información del usuario enviamos el la referencia para guardarlo en la base de datos  de preferencia enviamos el id para que no se repita*/
                    val currentUserDb = databaseReference.child(user.uid)
                    //Agregamos el nombre y el apellido dentro de user/id/
                    currentUserDb.child("name").setValue(name)
                    currentUserDb.child("email").setValue(email)
                    //Por último nos vamos a la vista home
                    home(email)

                }.addOnFailureListener{
                // si el registro falla se mostrara este mensaje
                    Toast.makeText(this, "Error en la autenticación.",
                        Toast.LENGTH_SHORT).show()
                }

        } else {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener(this) {
            //Verificamos que la tarea se realizó correctamente
                    task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,
                        "Email " + user.getEmail(),
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this,
                        "Error al verificar el correo ",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun home(mail:String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("mail", mail)
        startActivity(intent)
    }

    fun singUp(v: View){
        createAccount()
    }
}