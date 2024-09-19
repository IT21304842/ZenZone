package com.example.zenzoneapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zenzoneapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Setting up the Spinner for Relation to Child
        val relationOptions = resources.getStringArray(R.array.relation_options)
        val adapter = ArrayAdapter(this, R.layout.spinner, relationOptions)
        binding.spinnerRelation.adapter = adapter

        binding.textView.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {

            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            val name = binding.etChildName.text.toString()
            val age = binding.etChildAge.text.toString()
            val Pname = binding.etParentName.text.toString()
            val Phone = binding.etParentPhone.text.toString()
            val Relation = binding.spinnerRelation.selectedItem.toString()  // Get selected item from Spinner

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && name.isNotEmpty() && age.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("SignUp", "User created successfully")

                                // Get the user ID
                                val userId = firebaseAuth.currentUser?.uid

                                // Create a new user object with additional info
                                val user = User(name, age, Pname, Phone, Relation)

                                // Save user data to Firebase Realtime Database
                                userId?.let {
                                    database.child("users").child(it).setValue(user)
                                        .addOnCompleteListener { dbTask ->
                                            if (dbTask.isSuccessful) {
                                                Log.d("SignUp", "User data saved to database")
                                                // Navigate to SignIn Activity
//                                                val intent = Intent(this, LogIn::class.java)
                                                startActivity(intent)
                                            } else {
                                                Log.e("SignUp", "Failed to save user data", dbTask.exception)
                                                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                }
                            } else {
                                Log.e("SignUp", "Failed to create user", task.exception)
                                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

// Data class for User
data class User(val ChildName: String, val age: String, val ParentName: String, val Phone: String, val Relation: String)
