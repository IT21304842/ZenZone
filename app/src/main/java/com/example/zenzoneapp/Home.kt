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

    private val cardItems = mutableListOf<ActivityData>() // This will hold the fetched activities
    private lateinit var adapter: DailyActivitiesAdapter

    // Change appointmentItems to hold appointments from the database
    private val appointmentItems = mutableListOf<Appointment>() // Changed to mutable list
    private lateinit var appointmentRecyclerView: RecyclerView

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

        // Set up RecyclerView for activities
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = DailyActivitiesAdapter(cardItems) { updatedItem ->
            updateActivityStatus(updatedItem) // Update activity status and comment
        }
        recyclerView.adapter = adapter

        // Initialize the appointment recycler view
        appointmentRecyclerView = view.findViewById(R.id.AppointmentRecyclerView)

        // Set up the appointment RecyclerView
        appointmentRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("activities")

        // Fetch activities and appointments for today's date
        fetchTodayActivities()
        fetchTodayAppointments() // Call the new method

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
        Log.d("HomeFragment", "Fetching activities for today's date: $todayDate")

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
                            Log.d("HomeFragment", "Activity found: ${activity.date}, User: ${activity.userId}")
                        }
                    }
                } else {
                    Log.d("HomeFragment", "No activities found for today's date.")
                }

                // Notify the adapter that data has changed
                adapter.notifyDataSetChanged()
                Log.d("HomeFragment", "Adapter notified. Total activities: ${cardItems.size}")

                // Update the progress bar after fetching the activities
                updateProgressBar()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeFragment", "Error fetching activities: ${error.message}")
            }
        })
    }

    private fun fetchTodayAppointments() {
        val todayDate = getTodayDateforAppointment()
        Log.d("HomeFragment", "Fetching appointments for today's date: $todayDate")

        // Reference to the therapy_sessions table
        val appointmentReference = FirebaseDatabase.getInstance().getReference("therapy_sessions")
        val query = appointmentReference.orderByChild("date").equalTo(todayDate)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                appointmentItems.clear() // Clear old appointment data

                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val appointment = data.getValue(Appointment::class.java)
                        if (appointment != null && appointment.userId == auth.currentUser?.uid) {
                            appointmentItems.add(appointment) // Add appointment to the list
                            Log.d("HomeFragment", "Appointment found: ${appointment.date}, User: ${appointment.userId}")
                        }
                    }
                } else {
                    Log.d("HomeFragment", "No appointments found for today's date.")
                }

                // Update the appointment RecyclerView with new data
                val appointmentAdapter = AppointmentNoticesAdapter(appointmentItems)
                appointmentRecyclerView.adapter = appointmentAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeFragment", "Error fetching appointments: ${error.message}")
            }
        })
    }

    private fun updateProgressBar() {
        // Calculate the total number of activities and completed activities
        val totalActivities = cardItems.size
        val completedActivities = cardItems.count { it.status == "completed" }

        // Update the progress bar
        if (totalActivities > 0) {
            val progressPercentage = (completedActivities * 100) / totalActivities
            progressBar.progress = progressPercentage
            progressText.text = "$completedActivities / $totalActivities completed"
        } else {
            progressBar.progress = 0
            progressText.text = "No activities for today."
        }
    }

    private fun getTodayDateforAppointment(): String {
        val calendar = Calendar.getInstance()

        // Get year, month, and day
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-indexed
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Format the date to match the format in your database (day/month/year)
        return "$day/$month/$year"
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


    private fun updateActivityStatus(updatedItem: ActivityData) {
        // Update the activity in the database using the unique activityId
        databaseReference.child(updatedItem.activityId).setValue(updatedItem)
            .addOnSuccessListener {
                Log.d("HomeFragment", "Activity updated successfully: ${updatedItem.activityName}")
            }
            .addOnFailureListener { error ->
                Log.e("HomeFragment", "Error updating activity: ${error.message}")
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
                dateTextViews[i].setTextColor(resources.getColor(R.color.white))
            } else {
                dateTextViews[i].setBackgroundResource(0)
                dateTextViews[i].setTextColor(resources.getColor(R.color.black))
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1) // Move to the next day
        }
    }
}

// Remove the AppointmentItem class and keep only Appointment class.
