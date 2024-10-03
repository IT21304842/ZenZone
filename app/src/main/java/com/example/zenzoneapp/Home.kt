package com.example.zenzoneapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class Home : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var recyclerView: RecyclerView
    private var currentStage = 1 // To track the current stage
    private var lastClickedPosition = -1 // To track the last clicked card position

    private val cardItems = mutableListOf<ActivityData>() // This will hold the fetched activities
    private lateinit var adapter: DailyActivitiesAdapter

    // Add a variable to track the total number of cards
    private val totalCards = cardItems.size

    private lateinit var appointmentRecyclerView: RecyclerView
    private val appointmentItems = listOf(
        AppointmentItem("Appointment 1", "10:00 AM", "Location A"),
        AppointmentItem("Appointment 2", "11:30 AM", "Location B"),
        AppointmentItem("Appointment 3", "1:00 PM", "Location C"),
    )

    private lateinit var currentMonthTextView: TextView
    private lateinit var dateTextViews: List<TextView>

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

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
        adapter = DailyActivitiesAdapter(cardItems) { position -> onCardClick(position) }
        recyclerView.adapter = adapter

        // Initialize the appointment recycler view
        appointmentRecyclerView = view.findViewById(R.id.AppointmentRecyclerView)

        // Set up the appointment RecyclerView
        appointmentRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val appointmentAdapter = AppointmentNoticesAdapter(appointmentItems)
        appointmentRecyclerView.adapter = appointmentAdapter

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("activities")

        // Fetch activities for today's date
        fetchTodayActivities()

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

    private fun fetchTodayActivities() {
        val todayDate = getTodayDate()
        Log.d("HomeFragment", "Fetching activities for today's date: $todayDate") // Log the date for debugging

        // Query activities where the date matches today's date
        val query = databaseReference.orderByChild("date").equalTo(todayDate)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cardItems.clear() // Clear the old data

                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val activity = data.getValue(ActivityData::class.java)
                        if (activity != null && activity.userId == auth.currentUser?.uid) {
                            cardItems.add(activity)
                            Log.d("HomeFragment", "Activity found: ${activity.date}, User: ${activity.userId}") // Log activity details
                        }
                    }
                } else {
                    Log.d("HomeFragment", "No activities found for today's date.") // Log if no data found
                }

                // Notify the adapter that data has changed
                adapter.notifyDataSetChanged()
                Log.d("HomeFragment", "Adapter notified. Total activities: ${cardItems.size}") // Log the size of the list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeFragment", "Error fetching activities: ${error.message}") // Log error in case of failure
            }
        })
    }

    private fun getTodayDate(): String {
        val calendar = Calendar.getInstance()

        // Get year, month, and day without leading zeros
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-indexed, so we add 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Format the date to match the format in your database (without leading zeros)
        return "$year-$month-$day"
    }

    private fun onCardClick(position: Int) {
        if (position == lastClickedPosition) {
            return // Do nothing if the same card is clicked again
        }

        if (position == currentStage - 1) {
            updateProgress()
        } else if (position > currentStage - 1) {
            currentStage = position + 1
            updateProgress()
        }

        lastClickedPosition = position
    }

    private fun updateProgress() {
        val progressPercentage = (currentStage.toFloat() / cardItems.size * 100).toInt()
        progressBar.progress = progressPercentage

        progressText.text = when (currentStage) {
            in 1 until cardItems.size -> "Activity $currentStage"
            cardItems.size -> "You Did Well Today!"
            else -> "Activity 1"
        }
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
}
