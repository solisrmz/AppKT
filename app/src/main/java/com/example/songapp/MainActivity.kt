package com.example.songapp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialogo.view.*

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
        var db : Query = FirebaseDatabase.getInstance().getReference("task")
        taskItemList = mutableListOf<Task>()
        adapter = RememberAdpater(this, taskItemList!!)
        listViewItems!!.adapter = adapter
        db.addListenerForSingleValueEvent(itemListener)
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
            //Declare and Initialise the Task
            database = FirebaseDatabase.getInstance()
            databaseReference = database.reference.child("task")
            val task = Task.create()
            //Set Task Description and isDone Status
            task.taskDesc = dialog.remember.text.toString()
            task.taskAut = user.email

            val newTask= databaseReference.child("task").push()
            task.objectId = newTask.key
            newTask.setValue(task)
            mAlertDialog.dismiss()
            Toast.makeText(this, "Se ha guardado tu recordatorio", Toast.LENGTH_SHORT).show()
        }
    }
}