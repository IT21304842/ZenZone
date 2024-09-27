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
import java.text.SimpleDateFormat
import java.util.*

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
    )

    private lateinit var appointmentRecyclerView: RecyclerView
    private val appointmentItems = listOf(
        AppointmentItem("Appointment 1", "10:00 AM", "Location A"),
        AppointmentItem("Appointment 2", "11:30 AM", "Location B"),
        AppointmentItem("Appointment 3", "1:00 PM", "Location C"),
    )

    private lateinit var currentMonthTextView: TextView
    private lateinit var dateTextViews: List<TextView>

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
        val adapter = DailyActivitiesAdapter(cardItems) { position -> onCardClick(position) }
        recyclerView.adapter = adapter

        // Initialize the appointment recycler view
        appointmentRecyclerView = view.findViewById(R.id.AppointmentRecyclerView)

        // Set up the appointment RecyclerView
        appointmentRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val appointmentAdapter = AppointmentNoticesAdapter(appointmentItems)
        appointmentRecyclerView.adapter = appointmentAdapter

        // Initialize current month and week dates
        currentMonthTextView = view.findViewById(R.id.currentMonthTextView)
        dateTextViews = listOf(
            view.findViewById(R.id.date1),
            view.findViewById(R.id.date2),
            view.findViewById(R.id.date3),
            view.findViewById(R.id.date4),
            view.findViewById(R.id.date5),
            view.findViewById(R.id.date6),
            view.findViewById(R.id.date7)
        )

        setCurrentMonthAndDates()

        return view
    }

    private fun setCurrentMonthAndDates() {
        // Get the current date
        val calendar = Calendar.getInstance()

        // Capture today's date before modifying the calendar
        val todayCalendar = Calendar.getInstance()
        val todayDate = todayCalendar.get(Calendar.DAY_OF_MONTH)

        // Get the current month
        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        val currentMonth = monthFormat.format(calendar.time)
        currentMonthTextView.text = currentMonth

        // Set the current week's dates
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 0 = Sunday, 1 = Monday, etc.
        calendar.add(Calendar.DAY_OF_MONTH, -firstDayOfWeek) // Go to the first day of the week

        for (i in 0 until 7) {
            val date = calendar.get(Calendar.DAY_OF_MONTH)
            dateTextViews[i].text = date.toString()

            // Highlight today's date
            if (date == todayDate) {
                dateTextViews[i].setBackgroundResource(R.drawable.circle_current_date)
                dateTextViews[i].setTextColor(resources.getColor(R.color.white)) // Optional: Change text color for visibility
            } else {
                dateTextViews[i].setBackgroundResource(0)
                dateTextViews[i].setTextColor(resources.getColor(R.color.black)) // Optional: Reset text color
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1) // Move to the next day
        }
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
