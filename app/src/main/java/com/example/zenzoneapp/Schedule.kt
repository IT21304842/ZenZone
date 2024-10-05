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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Schedule : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var userSchedule: UserSchedule

    private val calendar = Calendar.getInstance()
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private var selectedDate: String? = null
    private var selectedDateTextView: TextView? = null // To keep track of the selected date


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        userSchedule = UserSchedule() // Initialize the fragment

        // Add the fragment to the adapter
        val fragmentAdapter = FragmentAdapter(childFragmentManager)
        fragmentAdapter.addFragment(userSchedule, "Yours")
        fragmentAdapter.addFragment(RecommendationSchedule(), "Recommended")
        fragmentAdapter.addFragment(HistorySchedule(), "History")

        binding.viewPager.adapter = fragmentAdapter
        binding.tablayout.setupWithViewPager(binding.viewPager)

        val datesContainer = binding.datesContainer
        updateMonthView(datesContainer)

        binding.fab.setOnClickListener {
            showAddSchedulePopup()
        }

        // Set up button listeners
        binding.prevMonthButton.setOnClickListener {
            calendar.add(Calendar.MONTH, -1) // Go to the previous month
            updateMonthView(datesContainer) // Update the month view
        }

        binding.nextMonthButton.setOnClickListener {
            calendar.add(Calendar.MONTH, 1) // Go to the next month
            updateMonthView(datesContainer) // Update the month view
        }

        return binding.root
    }

    private fun updateMonthView(datesContainer: LinearLayout) {
        val currentMonthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
        binding.currentMonthTextView.text = currentMonthName
        datesContainer.removeAllViews()
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        populateCalendarDates(firstDayOfWeek, daysInMonth, datesContainer)
    }

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

            val dateFrameLayout = FrameLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val dateTextView = TextView(requireContext()).apply {
                text = date.toString()
                gravity = Gravity.CENTER
                textSize = 18f
                setPadding(8, 8, 8, 8)

                if (date == today) {
                    background = resources.getDrawable(R.drawable.circle_current_date, null)
                    setTextColor(Color.WHITE)
                }

                setOnClickListener {
                    handleDateClick(this)
                }
            }

            dateFrameLayout.addView(dateTextView)
            linearLayout?.addView(dateFrameLayout)
        }
    }

    private fun handleDateClick(clickedDateTextView: TextView) {
        // Clear the previous selection
        selectedDateTextView?.apply {
            background = null
            setTextColor(Color.BLACK)
        }

        // Set the new selection
        clickedDateTextView.background = resources.getDrawable(R.drawable.clicked_date_background, null)
        selectedDateTextView = clickedDateTextView

        // Update selected date
        val day = clickedDateTextView.text.toString().toInt() // Keep this as is
        val month = calendar.get(Calendar.MONTH) + 1 // Keep this as is
        val year = calendar.get(Calendar.YEAR) // Keep this as is
        selectedDate = String.format("%04d-%d-%d", year, month, day) // Change "%02d" to "%d" for day

        // Reload user schedule for the selected date
        userSchedule.updateSelectedDate(selectedDate!!)
    }

    private fun reloadUserSchedule() {
        val userId = auth.currentUser?.uid
        if (userId != null && selectedDate != null) {
            val userScheduleFragment = UserSchedule()
            userScheduleFragment.loadScheduleData(userId, selectedDate!!)
            // Refresh the displayed fragment, if needed
            // Replace the fragment or update the adapter here
        } else {
            Toast.makeText(context, "User not logged in or date not selected", Toast.LENGTH_SHORT).show()
        }
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

            when {
                activityName.isEmpty() -> {
                    Toast.makeText(context, "Please fill in the Activity Name.", Toast.LENGTH_SHORT).show()
                    activityNameEditText.requestFocus() // Highlight the field
                }
                description.isEmpty() -> {
                    Toast.makeText(context, "Please fill in the Description.", Toast.LENGTH_SHORT).show()
                    descriptionEditText.requestFocus() // Highlight the field
                }
                specialNotes.isEmpty() -> {
                    Toast.makeText(context, "Please fill in the Special Notes.", Toast.LENGTH_SHORT).show()
                    specialNotesEditText.requestFocus() // Highlight the field
                }
                selectedDate == null -> {
                    Toast.makeText(context, "Please select a date.", Toast.LENGTH_SHORT).show()
                }
                userId == null -> {
                    Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Create a new activity reference using push() to generate a unique ID
                    val newActivityRef = database.child("activities").push()

                    // Create an ActivityData object with the generated ID
                    val activityData = ActivityData(
                        activityId = newActivityRef.key ?: "",
                        activityName = activityName,
                        description = description,
                        specialNotes = specialNotes,
                        userId = userId,
                        date = selectedDate!!,
                        status = "pending",
                        reaction = "happy",
                        comment = ""
                    )

                    // Save the activity data to the database
                    newActivityRef.setValue(activityData)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Activity added!", Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                            reloadUserSchedule() // Reload schedule after adding new activity
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to add activity!", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class ActivityData(
    val activityId: String = "",
    val activityName: String = "",
    val description: String = "",
    val specialNotes: String = "",
    val userId: String = "",
    val date: String = "",
    var status: String = "",
    var comment: String = "",
    var reaction: String = ""
)
