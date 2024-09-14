package com.tugure.servicetugurereport

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DashboardAdminActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_admin)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        val editTextId = findViewById<EditText>(R.id.editTextId)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextRole = findViewById<EditText>(R.id.editTextRole)
        val buttonAddUser = findViewById<Button>(R.id.buttonAddUser)

        buttonAddUser.setOnClickListener {
            val id = editTextId.text.toString()
            val password = editTextPassword.text.toString()
            val role = editTextRole.text.toString()

            if (id.isNotEmpty() && password.isNotEmpty() && role.isNotEmpty()) {
                val user = mapOf(
                    "password" to password,
                    "role" to role
                )
                database.child("users").child(id).setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to add user: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please enter ID, password, and role", Toast.LENGTH_SHORT).show()
            }
        }
    }
}