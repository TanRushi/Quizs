package com.example.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quiz.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var signupBinding: ActivitySignUpBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signupBinding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = signupBinding.root
        setContentView(view)

        signupBinding.buttonSignUp.setOnClickListener {
            val email = signupBinding.loginMail.text.toString()
            val password = signupBinding.LoginPassword.text.toString()

            if (isValidInput(email, password)) {
                signUpWithFirebase(email, password)
            }
        }
    }

    private fun isValidInput(email: String, password: String): Boolean {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(applicationContext, "Invalid email address", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(applicationContext, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun signUpWithFirebase(email: String, password: String) {
        signupBinding.progressBar.visibility = View.VISIBLE
        signupBinding.buttonSignUp.isClickable = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            signupBinding.progressBar.visibility = View.INVISIBLE
            signupBinding.buttonSignUp.isClickable = true

            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Your account has been created", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                val errorMessage = task.exception?.localizedMessage ?: "Sign-up failed"
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
