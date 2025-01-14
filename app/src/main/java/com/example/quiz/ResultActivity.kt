package com.example.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quiz.databinding.ActivityQuizBinding
import com.example.quiz.databinding.ActivityResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ResultActivity : AppCompatActivity() {
    lateinit var resultBinding: ActivityResultBinding

    val database= FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("scores")

    val auth  = FirebaseAuth.getInstance()
    val user =auth.currentUser

    var userCorrect = ""
    var userWrong = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultBinding= ActivityResultBinding.inflate(layoutInflater)
        val view = resultBinding.root
        setContentView(view)

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                user?.let {
                    val userUID = it.uid

                 userCorrect = snapshot.child(userUID).child("correct").value.toString()
                    userWrong = snapshot.child(userUID).child("wrong").value.toString()

                    resultBinding.correctScore.text= userCorrect
                    resultBinding.wrongscore.text= userWrong

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        resultBinding.playAgain.setOnClickListener{
            val  intent = Intent(this,Quiz::class.java)
            startActivity(intent)
            finish()
        }

        resultBinding.next.setOnClickListener {

            val  intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}