package com.example.quiz

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.quiz.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.view.View
import java.util.Random

class Quiz : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding

    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("questions")

    var question = ""
    var ansA = ""
    var ansB = ""
    var ansC = ""
    var ansD = ""
    var correctAns = ""
    var questionCount = 0
    var quesNumber = 0
    var userAnswer = ""
    var useracorrect = 0
    var userWrong = 0



    lateinit var timer : CountDownTimer
    private val totalTime = 25000L
    var timerContinue = false
    var leftTime = totalTime

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val scoreRef =database.reference

    val  questions = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view =quizBinding.root
        setContentView(view)

        do {

       val number = kotlin.random.Random.nextInt(1,11)
            Log.d("number",number.toString())




            questions.add(number)


        }while (questions.size < 10)

        Log.d("numberOfQuestions",questions.toString())

        gameLogic()
        quizBinding.finish.setOnClickListener{

            sendScore()

        }

        quizBinding.next1.setOnClickListener {

            resetTimer()
            gameLogic()
        }

        quizBinding.A.setOnClickListener {

            pauseTimer()
            userAnswer =ansA

            if(correctAns==userAnswer){
                quizBinding.A.setTextColor(getColor(R.color.green))

                useracorrect++
                quizBinding.correct.text = useracorrect.toString()
            }else{
                quizBinding.A.setTextColor(getColor(R.color.red))
                userWrong++
                quizBinding.wrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }

        quizBinding.B.setOnClickListener {
            pauseTimer()
            userAnswer =ansB

            if (correctAns.trim().equals(userAnswer.trim(), ignoreCase = true)){
                quizBinding.B.setTextColor(getColor(R.color.green))
                useracorrect++
                quizBinding.correct.text = useracorrect.toString()
            }else{
                quizBinding.B.setTextColor(getColor(R.color.red))
                userWrong++
                quizBinding.wrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }

        quizBinding.C.setOnClickListener {
            pauseTimer()
            userAnswer =ansC

            if (correctAns.trim().equals(userAnswer.trim(), ignoreCase = true)){
                quizBinding.C.setTextColor(getColor(R.color.green))
                useracorrect++
                quizBinding.correct.text = useracorrect.toString()
            }else{
                quizBinding.C.setTextColor(getColor(R.color.red))
                userWrong++
                quizBinding.wrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }

        quizBinding.D.setOnClickListener {
            pauseTimer()
            userAnswer =ansD

            if (correctAns.trim().equals(userAnswer.trim(), ignoreCase = true)){
                quizBinding.D.setTextColor(getColor(R.color.green))
                useracorrect++
                quizBinding.correct.text = useracorrect.toString()
            }else{
                quizBinding.D.setTextColor(getColor(R.color.red))
                userWrong++
                quizBinding.wrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }



    }

    private fun gameLogic(){

        restoreOption()
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                questionCount = snapshot.childrenCount.toInt()

                if(quesNumber< questions.size) {

                    question = snapshot.child(questions.elementAt(quesNumber).toString()).child("que").value.toString()
                    ansA = snapshot.child(questions.elementAt(quesNumber).toString()).child("a").value.toString()
                    ansB = snapshot.child(questions.elementAt(quesNumber).toString()).child("b").value.toString()
                    ansC = snapshot.child(questions.elementAt(quesNumber).toString()).child("c").value.toString()
                    ansD = snapshot.child(questions.elementAt(quesNumber).toString()).child("d").value.toString()
                    correctAns = snapshot.child(questions.elementAt(quesNumber).toString()).child("ans").value.toString()


                    quizBinding.Question.text = question
                    quizBinding.A.text = ansA
                    quizBinding.B.text = ansB
                    quizBinding.C.text = ansC
                    quizBinding.D.text = ansD

                    quizBinding.progressBar3.visibility = android.view.View.INVISIBLE
                    quizBinding.linearLayoutInfo.visibility = android.view.View.VISIBLE
                    quizBinding.linearLayout2.visibility = android.view.View.VISIBLE
                    quizBinding.LinearLayoutButtons.visibility = android.view.View.VISIBLE

                    startTimer()

                }else{

                    val dialogMessage = AlertDialog.Builder(this@Quiz)
                    dialogMessage.setTitle("Quiz game")
                    dialogMessage.setMessage("Congratulations!!\n you anser all the questions.Do you want to see the result ")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogInterface ,i ->
                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Play again"){dialogInterface , i ->
                        val intent = Intent(this@Quiz,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialogMessage.create().show()

                }

                quesNumber++
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun findAnswer(){
        when (correctAns.trim().toUpperCase()) {
            "a" -> quizBinding.A.setTextColor(getColor(R.color.green))
            "b" -> quizBinding.B.setTextColor(getColor(R.color.green))
            "c" -> quizBinding.C.setTextColor(getColor(R.color.green))
            "d" -> quizBinding.D.setTextColor(getColor(R.color.green))
        }
    }


    fun disableClickable(){
        quizBinding.A.isClickable = false
        quizBinding.B.isClickable = false
        quizBinding.C.isClickable = false
        quizBinding.D.isClickable = false
    }


    fun restoreOption(){
        quizBinding.A.setTextColor(getColor(R.color.black))
        quizBinding.B.setTextColor(getColor(R.color.black))
        quizBinding.C.setTextColor(getColor(R.color.black))
        quizBinding.D.setTextColor(getColor(R.color.black))

        quizBinding.A.isClickable =true
        quizBinding.B.isClickable =true
        quizBinding.C.isClickable =true
        quizBinding.D.isClickable =true
    }



private fun startTimer() {
   timer =object : CountDownTimer(leftTime,1000){
       override fun onTick(milliUntilFinish: Long) {
           leftTime= milliUntilFinish
           updateCoundownText()

       }

       override fun onFinish() {
           disableClickable()
         resetTimer()
           updateCoundownText()
           quizBinding.Question.text = "Time Up"
           timerContinue = false
       }


   }.start()

}
    fun updateCoundownText(){
        val remaininggTimer : Int = (leftTime/1000).toInt()
        quizBinding.time.text = remaininggTimer.toString()

    }

    fun pauseTimer(){
        timer.cancel()
        timerContinue=false
    }

    fun resetTimer(){
        pauseTimer()
        leftTime=totalTime
        updateCoundownText()

    }

    fun sendScore(){

        user?.let {
            val userUid =it.uid
            scoreRef.child("scores").child(userUid).child("correct").setValue(useracorrect)
            scoreRef.child("scores").child(userUid).child("wrong").setValue(userWrong).addOnSuccessListener {


                val intent = Intent(this@Quiz,ResultActivity::class.java)
                startActivity(intent)
                finish()

            }
        }

    }
}