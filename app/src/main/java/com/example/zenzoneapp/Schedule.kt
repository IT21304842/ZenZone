package com.example.zenzoneapp

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.zenzoneapp.databinding.FragmentScheduleBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class Schedule : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    // Variable to store the selected date
    private var selectedDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Access ViewPager and TabLayout using the binding
        val viewPager = binding.viewPager
        val tabLayout = binding.tablayout

        val fragmentAdapter = FragmentAdapter(childFragmentManager)
        fragmentAdapter.addFragment(UserSchedule(), "Yours")
        fragmentAdapter.addFragment(RecommendationSchedule(), "Recommended")
        fragmentAdapter.addFragment(HistorySchedule(), "History")

        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

        // Initialize datesContainer
        val datesContainer = binding.datesContainer
        updateMonthView(datesContainer)

        // Set up FloatingActionButton click listener to show popup
        binding.fab.setOnClickListener {
            showAddSchedulePopup()
        }

        return binding.root // Return the binding root
    }

    private fun updateMonthView(datesContainer: LinearLayout) {
        // Update current month TextView
        val currentMonthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
        binding.currentMonthTextView.text = currentMonthName

        // Clear previous dates
        datesContainer.removeAllViews()

        // Set calendar to first day of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        // Get the first day of the month and determine how many days are in this month
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Populate the calendar
        populateCalendarDates(firstDayOfWeek, daysInMonth, datesContainer)
    }

    private var selectedDateTextView: TextView? = null // To keep track of the selected date

    private fun populateCalendarDates(firstDayOfWeek: Int, daysInMonth: Int, datesContainer: LinearLayout) {
        val datesPerRow = 7
        var linearLayout: LinearLayout? = null

        val today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        for (i in 1 until firstDayOfWeek) {
            if (i % datesPerRow == 1) {
                linearLayout = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                }
                datesContainer.addView(linearLayout)
            }

            // Create empty TextView for leading spaces
            val emptyTextView = TextView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = Gravity.CENTER
                textSize = 18f
            }
            linearLayout?.addView(emptyTextView)
        }

        for (date in 1..daysInMonth) {
            if ((date + firstDayOfWeek - 1) % datesPerRow == 1) {
                linearLayout = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                }
                datesContainer.addView(linearLayout)
            }

            // Create a FrameLayout to hold the date TextView
            val dateFrameLayout = FrameLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            // Create a TextView for each date
            val dateTextView = TextView(requireContext()).apply {
                text = date.toString()
                gravity = Gravity.CENTER
                textSize = 18f
                setPadding(8, 8, 8, 8)

                // Set background for today's date
                if (date == today) {
                    background = resources.getDrawable(R.drawable.circle_current_date, null)
                    setTextColor(Color.WHITE) // Change text color for contrast
                }

                // Set click listener for the date
                setOnClickListener {
                    handleDateClick(this)
                }
            }

            dateFrameLayout.addView(dateTextView)
            linearLayout?.addView(dateFrameLayout) ?: run {}
        }
    }

    // Function to handle the date click
    private fun handleDateClick(clickedDateTextView: TextView) {
        // Reset the previously selected date's background (if any)
        selectedDateTextView?.apply {
            background = null // Reset background
            setTextColor(Color.BLACK) // Reset text color to default
        }

        // Highlight the clicked date
        clickedDateTextView.background = resources.getDrawable(R.drawable.clicked_date_background, null) // Change to your desired background

        // Update the selected date reference
        selectedDateTextView = clickedDateTextView

        // Store the selected date in the variable (formatted as needed)
        val day = clickedDateTextView.text.toString().toInt()
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-based
        val year = calendar.get(Calendar.YEAR)
        selectedDate = "$year-$month-$day" // Format as "YYYY-MM-DD"

    }

    private fun showAddSchedulePopup() {
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.schedule_new, null)

        val activityNameEditText = popupView.findViewById<EditText>(R.id.activityNameEditText)
        val descriptionEditText = popupView.findViewById<EditText>(R.id.descriptionEditText)
        val specialNotesEditText = popupView.findViewById<EditText>(R.id.specialNotesEditText)
        val addButton = popupView.findViewById<Button>(R.id.addButton)
        val closeTextView = popupView.findViewById<ImageView>(R.id.txtclose)

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(popupView)
            .create()

        closeTextView.setOnClickListener {
            alertDialog.dismiss()
        }

        addButton.setOnClickListener {
            val activityName = activityNameEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val specialNotes = specialNotesEditText.text.toString().trim()
            val userId = auth.currentUser?.uid

            // Ensure all fields are filled and date is selected
            if (activityName.isNotEmpty() && userId != null && selectedDate != null) {
                // Create an ActivityData instance including the selected date
                val activityData = ActivityData(activityName, description, specialNotes, userId, selectedDate!!)

                // Save to Firebase Realtime Database
                database.child("activities").push().setValue(activityData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Activity added!", Toast.LENGTH_SHORT).show()
                        alertDialog.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error adding activity", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Please fill all fields and select a date", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }



    data class ActivityData(
        val activityName: String,
        val description: String,
        val specialNotes: String,
        val userId: String,
        val date: String
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
