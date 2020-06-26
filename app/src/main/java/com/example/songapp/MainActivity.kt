package com.example.songapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialogo.view.*

@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    var taskItemList: MutableList<Task>? = null
    lateinit var adapter: RememberAdpater
    private var listViewItems: ListView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listViewItems = findViewById<ListView>(R.id.items_list)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        val mail = user.email

        btnMain.setOnClickListener(){
            newRemember()
        }
        databaseRef = FirebaseDatabase.getInstance().getReference("task${user.uid}")
        taskItemList = mutableListOf<Task>()
        adapter = RememberAdpater(this, taskItemList!!)
        listViewItems!!.adapter = adapter
        databaseRef.addListenerForSingleValueEvent(itemListener)
    }
    private var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            addDataToList(dataSnapshot)
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, log a message
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    private fun addDataToList(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()
        //Check if current database contains any collection
        if (items.hasNext()) {
            val toDoListindex = items.next()
            val itemsIterator = toDoListindex.children.iterator()

            //check if the collection has any to do items or not
            while (itemsIterator.hasNext()) {
                //get current item
                val currentItem = itemsIterator.next()
                val task = Task.create()
                //get current data in a map
                val map = currentItem.value as HashMap<String, Any>
                //key will return Firebase ID
                task.objectId = currentItem.key
                task.taskDesc = map["taskDesc"] as String?
                taskItemList!!.add(task);
            }
        }
        //alert adapter that has changed
        adapter.notifyDataSetChanged()
    }

    fun newRemember(){
        val dialog = LayoutInflater.from(this).inflate(R.layout.dialogo, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(dialog)
            .setTitle("Ingresar un nuevo recordatorio")
        val  mAlertDialog = mBuilder.show()
        dialog.cerrar.setOnClickListener(){
            val task = Task.create()
            //Set Task Description and isDone Status
            task.taskDesc = dialog.remember.text.toString()
            task.taskAut = user.uid
            database = FirebaseDatabase.getInstance()
            databaseReference = database.reference.child("task${user.uid}")
            val newTask= databaseReference.child("task").push()
            task.objectId = newTask.key
            newTask.setValue(task)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "Se ha guardado tu recordatorio", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.info-> {
                Toast.makeText(this, "Info de la app", Toast.LENGTH_SHORT).show()
            }
            R.id.logout->{
                auth.signOut()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}