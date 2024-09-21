package com.example.zenzoneapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.database.*
//
//class Quiz : AppCompatActivity() {
//
//    private lateinit var database: DatabaseReference
//    private lateinit var areaTextView: TextView
//    private lateinit var questionTextView: TextView
//    private lateinit var answerButtons: List<Button>
//    private lateinit var nextButton: Button
//
//    private var currentAreaIndex: Int = 1
//    private var currentQuestionIndex: Int = 1
//    private var maxQuestions: Int = 0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_quiz)
//
//        areaTextView = findViewById(R.id.areaTextView)
//        questionTextView = findViewById(R.id.questionTextView)
//        answerButtons = listOf(
//            findViewById(R.id.answerA),
//            findViewById(R.id.answerB),
//            findViewById(R.id.answerC),
//            findViewById(R.id.answerD)
//        )
//        nextButton = findViewById(R.id.nextButton)
//
//        // Initialize Firebase
//        database = FirebaseDatabase.getInstance().getReference("areas")
//
//        // Load the first question
//        loadQuestion(currentAreaIndex, currentQuestionIndex)
//
//        // Handle Next Button Click
//        nextButton.setOnClickListener {
//            if (currentQuestionIndex < maxQuestions) {
//                currentQuestionIndex++
//                loadQuestion(currentAreaIndex, currentQuestionIndex)
//            } else {
//                // Move to next area
//                currentAreaIndex++
//                if (currentAreaIndex <= 6) {
//                    currentQuestionIndex = 1
//                    loadQuestion(currentAreaIndex, currentQuestionIndex)
//                } else {
//                    // End of quiz, go back to main activity
//                    val intent = Intent(this@Quiz, MainActivity::class.java)
//                    startActivity(intent)
//                    finish() // Make sure to call finish() to end this activity
//                }
//            }
//        }
//
//    }
//
//    private fun loadQuestion(areaId: Int, questionId: Int) {
//        val areaRef = FirebaseDatabase.getInstance().getReference("areas").child(areaId.toString())
//
//        areaRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Log the entire data snapshot to see what is retrieved from Firebase
//                Log.d("Quiz", "DataSnapshot: ${dataSnapshot.value}")
//
//                // Retrieve the area object
//                val area = dataSnapshot.getValue(Area::class.java)
//
//                // Log the area object to see if it's being created correctly
//                if (area != null) {
//                    Log.d("Quiz", "Area Retrieved: ${area.name}")
//                    Log.d("Quiz", "Questions Retrieved: ${area.questions}")
//                    displayAreaAndQuestion(area, questionId)
//                } else {
//                    Log.e("Quiz", "Area is null, check your data structure in Firebase")
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.e("Quiz", "Database error: ${databaseError.message}")
//            }
//        })
//
//    }
//
//    private fun displayAreaAndQuestion(area: Area, questionId: Int) {
//        areaTextView.text = area.name
//
//        val question = area.questions[questionId.toString()]
//        if (question != null) {
//            questionTextView.text = question.question
//            val answerKeys = listOf("A", "B", "C", "D")
//            for (i in answerButtons.indices) {
//                answerButtons[i].text = question.answers[answerKeys[i]]
//                answerButtons[i].setOnClickListener {
//                    // Handle answer selection, if necessary
//                }
//            }
//        }
//    }
//
//
//}

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Quiz : AppCompatActivity() {

    private lateinit var areaTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var answerAButton: Button
    private lateinit var answerBButton: Button
    private lateinit var answerCButton: Button
    private lateinit var answerDButton: Button
    private lateinit var nextButton: Button

    private val database = FirebaseDatabase.getInstance()
    private val questionsRef = database.getReference("areas")

    private var currentAreaIndex = 0
    private var currentQuestionIndex = 0
    private var areaList = mutableListOf<String>()
    private var questionList = mutableListOf<String>()

    private var currentQuestions: Map<String, Map<String, Any>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Initialize views
        areaTextView = findViewById(R.id.areaTextView)
        questionTextView = findViewById(R.id.questionTextView)
        answerAButton = findViewById(R.id.answerA)
        answerBButton = findViewById(R.id.answerB)
        answerCButton = findViewById(R.id.answerC)
        answerDButton = findViewById(R.id.answerD)
        nextButton = findViewById(R.id.nextButton)

        // Load quiz data from Firebase
        loadQuizData()

        // Set up click listeners for answer buttons
        answerAButton.setOnClickListener { selectAnswer("A") }
        answerBButton.setOnClickListener { selectAnswer("B") }
        answerCButton.setOnClickListener { selectAnswer("C") }
        answerDButton.setOnClickListener { selectAnswer("D") }

        // Set up click listener for the next button
        nextButton.setOnClickListener {
            currentQuestionIndex++ // Increment the question index
            showNextQuestion() // Show the next question
        }
    }

    private fun loadQuizData() {
        questionsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                areaList = snapshot.children.map { it.key.toString() }.toMutableList()
                showNextQuestion()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    private fun showNextQuestion() {
        // Check if there are no more areas left
        if (currentAreaIndex >= areaList.size) {
            // Quiz finished, navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val currentAreaKey = areaList[currentAreaIndex]
        val currentArea = questionsRef.child(currentAreaKey)

        currentArea.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(areaSnapshot: DataSnapshot) {
                val areaName = areaSnapshot.child("name").getValue(String::class.java)
                val questionsSnapshot = areaSnapshot.child("questions")

                // Update area name in UI
                areaTextView.text = areaName

                if (questionsSnapshot.exists()) {
                    val questionKeys = questionsSnapshot.children.map { it.key.toString() }
                    if (currentQuestionIndex < questionKeys.size) {
                        val questionKey = questionKeys[currentQuestionIndex]
                        val questionSnapshot = questionsSnapshot.child(questionKey)

                        val questionText = questionSnapshot.child("question").getValue(String::class.java)
                        val answersSnapshot = questionSnapshot.child("answers")

                        if (questionText != null && answersSnapshot.exists()) {
                            // Update UI with the current question and answers
                            questionTextView.text = questionText
                            answerAButton.text = answersSnapshot.child("A").getValue(String::class.java)
                            answerBButton.text = answersSnapshot.child("B").getValue(String::class.java)
                            answerCButton.text = answersSnapshot.child("C").getValue(String::class.java)
                            answerDButton.text = answersSnapshot.child("D").getValue(String::class.java)
                        } else {
                            // Handle missing data or unexpected structure
                            questionTextView.text = "Question or answers not found."
                            answerAButton.text = "N/A"
                            answerBButton.text = "N/A"
                            answerCButton.text = "N/A"
                            answerDButton.text = "N/A"
                        }
                    } else {
                        // Move to the next area if no more questions are left in the current area
                        currentQuestionIndex = 0
                        currentAreaIndex++
                        showNextQuestion()
                    }
                } else {
                    // If there are no questions in the current area
                    currentQuestionIndex = 0
                    currentAreaIndex++
                    showNextQuestion()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }




    private fun selectAnswer(answer: String) {
        // Here you can handle the selected answer if needed
    }
}
