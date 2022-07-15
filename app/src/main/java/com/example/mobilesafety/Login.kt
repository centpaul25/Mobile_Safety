package com.example.mobilesafety

import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Log
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.VISIBLE
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.core.view.View
import com.google.firebase.ktx.Firebase


class Login : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var register: TextView
    private lateinit var linear: LinearLayout
    private lateinit var paulAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        paulAuth = FirebaseAuth.getInstance()

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        register = findViewById(R.id.register)
        linear = findViewById(R.id.linear)


        register.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        linear.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        login.setOnClickListener {

            val email = email.text.toString()
            val password = password.text.toString()

            login(email, password)
        }

    }

    private fun login(email: String, password: String) {

        if (email == "" || password == "") {
            Toast.makeText(this@Login, "Email or Password is empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(
                this@Login,
                "Password must be more than 6 characters",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        paulAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    Toast.makeText(this@Login, "Login Successfully", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@Login, HomePage::class.java)
                    startActivity(intent)

                } else {

                    Toast.makeText(this@Login, "The user does not exist", Toast.LENGTH_SHORT).show()

                }
            }
    }

}


