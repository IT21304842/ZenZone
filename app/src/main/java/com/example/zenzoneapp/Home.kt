package com.example.zenzoneapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zenzoneapp.R

class Home : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var recyclerView: RecyclerView
    private var currentStage = 1 // To track the current stage
    private var lastClickedPosition = -1 // To track the last clicked card position
    private val cardItems = listOf(
        CardItem("Activity 1", "Description of Activity 1"),
        CardItem("Activity 2", "Description of Activity 2"),
        CardItem("Activity 3", "Description of Activity 3"),
        // Add more card items as needed
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize views
        progressBar = view.findViewById(R.id.progressBar)
        progressText = view.findViewById(R.id.progressText)
        recyclerView = view.findViewById(R.id.recyclerView)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = DailyActivitiesAdapter(cardItems) { position ->
            onCardClick(position)
        }
        recyclerView.adapter = adapter

        return view
    }

    private fun onCardClick(position: Int) {
        // Check if the clicked card is the same as the last clicked card
        if (position == lastClickedPosition) {
            return // Do nothing if the same card is clicked again
        }

        // Update the progress based on the clicked card's position
        if (position == currentStage) {
            updateProgress()
        } else if (position > currentStage) {
            currentStage = position
            updateProgress()
        }

        // Update the last clicked position
        lastClickedPosition = position
    }

    private fun updateProgress() {
        // Update the progress based on the current stage
        when (currentStage) {
            1 -> {
                progressBar.progress = 25
                progressText.text = "Stage 1"
            }
            2 -> {
                progressBar.progress = 50
                progressText.text = "Stage 2"
            }
            3 -> {
                progressBar.progress = 75
                progressText.text = "Stage 3"
            }
            4 -> {
                progressBar.progress = 100
                progressText.text = "Completed"
            }
        }
    }
}
