package com.tugure.servicetugurereport.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.tugure.servicetugurereport.MainActivity
import com.tugure.servicetugurereport.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val requestBody = FormBody.Builder()
                    .add("id", email)
                    .add("password", password)
                    .build()

                val request = Request.Builder()
                    .url("https://your-server-url.com/getCustomToken")
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Authentication failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val responseBody = response.body?.string()
                            val json = JSONObject(responseBody)
                            val customToken = json.getString("customToken")

                            auth.signInWithCustomToken(customToken)
                                .addOnCompleteListener(this@LoginActivity) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, navigate to MainActivity
                                        runOnUiThread {
                                            Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                            startActivity(intent)
                                            finish() // Optional: close LoginActivity so user can't go back to it
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        runOnUiThread {
                                            Toast.makeText(this@LoginActivity, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                        } else {
                            runOnUiThread {
                                Toast.makeText(this@LoginActivity, "Authentication failed: ${response.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            } else {
                Toast.makeText(this, "Please enter ID and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}