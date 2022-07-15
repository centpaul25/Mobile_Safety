package com.example.mobilesafety

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var address: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var rb_male: RadioButton
    private lateinit var rb_female: RadioButton
    private lateinit var existingUser: TextView
    private lateinit var btnSignup: Button
    private lateinit var country: EditText
    private lateinit var stateProvince: EditText
    private lateinit var postalCode: EditText
    private lateinit var paulAuth: FirebaseAuth
    private lateinit var myDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        paulAuth = FirebaseAuth.getInstance()

        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        address = findViewById(R.id.address)
        phoneNumber = findViewById(R.id.phoneNumber)
        existingUser = findViewById(R.id.existingUser)
        btnSignup = findViewById(R.id.btnSignup)
        rb_male = findViewById(R.id.rb_male)
        rb_female = findViewById(R.id.rb_female)
        postalCode = findViewById(R.id.postalCode)
        stateProvince = findViewById(R.id.stateProvince)
        country = findViewById(R.id.country)

        existingUser.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)

            rb_female.setOnCheckedChangeListener { radioGroup, i ->
            }

            rb_male.setOnCheckedChangeListener { radioGroup, i ->
            }
        }
        btnSignup.setOnClickListener {
            val name = name.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()
            val confirmPassword = confirmPassword.text.toString()
            val address = address.text.toString()
            val phoneNumber = phoneNumber.text.toString()
            val rb_female = rb_female.text.toString()
            val rb_male = rb_male.text.toString()
            val country = country.text.toString()
            val existingUser = existingUser.text.toString()
            val postalCode = postalCode.text.toString()
            val stateProvince = stateProvince.text.toString()

            signUp(name, email, existingUser, password, postalCode, confirmPassword, address, country, stateProvince, phoneNumber, rb_male, rb_female)
        }
    }

    private fun signUp(
        name: String, email: String, password: String,
        confirmPassword: String, address: String,
        phoneNumber: String, stateProvince: String, postalCode: String,
        rb_male: String, rb_female: String, rb_female1: String, country: String,
    ) {

        paulAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    addUserToDatabase(email, name, paulAuth.currentUser?.uid!!)

                    Toast.makeText(this@SignUp, "Sign Up Successful, log In", Toast.LENGTH_SHORT)
                        .show()

                    val intent = Intent(this@SignUp, Login::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        this@SignUp, "Error occurred, check all fields and try again!", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addUserToDatabase(name: String, email: String, uid: String) {

        myDbRef = FirebaseDatabase.getInstance().getReference()

        myDbRef.child("user").child(uid).setValue(usersInfo(name, email, uid))
    }
}
