package com.example.zenzoneapp

data class Appointment(
    val date: String,
    val time: String,
    val location: String,
    val therapist: String,
    var notes: String
)
