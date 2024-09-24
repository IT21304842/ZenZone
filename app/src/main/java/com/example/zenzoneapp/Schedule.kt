package com.example.zenzoneapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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

        val fragmentAdapter = FragmentAdapter(childFragmentManager) // Use childFragmentManager
        fragmentAdapter.addFragment(UserSchedule(), "Yours")
        fragmentAdapter.addFragment(RecommendationSchedule(), "Recommended")
        fragmentAdapter.addFragment(HistorySchedule(), "History")

        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

        return binding.root // Return the binding root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the current month in the TextView
        updateMonthView()

        binding.prevMonthButton.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateMonthView()
        }

        binding.nextMonthButton.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateMonthView()
        }

        // Set up FloatingActionButton click listener to show popup
        binding.fab.setOnClickListener {
            showAddSchedulePopup()
        }
    }

    private fun updateMonthView() {
        // Update current month TextView
        val currentMonthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
        binding.currentMonthTextView.text = currentMonthName

        // Set calendar to first day of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // To make it zero-based (Sunday = 0)

        // Get number of days in the current month
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Create empty TextViews for padding the start of the grid (for days before the first of the month)
        val gridLayout = binding.monthGridLayout
        gridLayout.removeAllViews()

        for (i in 0 until firstDayOfWeek) {
            val emptyTextView = TextView(context)
            emptyTextView.text = ""
            gridLayout.addView(emptyTextView)
        }

        for (day in 1..daysInMonth) {
            val dayTextView = TextView(context)
            dayTextView.text = day.toString()
            dayTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            dayTextView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dayTextView.setPadding(16, 16, 16, 16)
            dayTextView.textSize = 16f

            dayTextView.setOnClickListener {
                val selectedDate = calendar.clone() as Calendar
                selectedDate.set(Calendar.DAY_OF_MONTH, day)
                showScheduleForSelectedDate(selectedDate)
            }

            gridLayout.addView(dayTextView)
        }

    }

    private fun showScheduleForSelectedDate(date: Calendar) {
        // Display schedules for the selected date or prompt to add a new schedule.
        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.time)
        Toast.makeText(context, "Selected Date: $dateString", Toast.LENGTH_SHORT).show()
        // Optionally, launch a dialog or navigate to another fragment with the schedule details.
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

            if (activityName.isNotEmpty() && userId != null) {
                val activityData = ActivityData(activityName, description, specialNotes, userId)

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
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }

    data class ActivityData(
        val activityName: String,
        val description: String,
        val specialNotes: String,
        val userId: String
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
