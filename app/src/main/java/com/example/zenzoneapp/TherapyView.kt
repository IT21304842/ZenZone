package com.example.zenzoneapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.db.williamchart.view.BarChartView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class TherapyView : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var cardAdapter: AppointmentAdapter
    private lateinit var barChart: BarChartView

    // Firebase Database Reference
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_therapy_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Views
        viewPager = view.findViewById(R.id.viewPager)
        barChart = view.findViewById(R.id.barChart)

        // Set up an empty adapter initially to avoid layout warnings
        cardAdapter = AppointmentAdapter(emptyList())
        viewPager.adapter = cardAdapter

        // Initialize Views
        viewPager = view.findViewById(R.id.viewPager)
        barChart = view.findViewById(R.id.barChart)

        setupBarChart()

        // Get therapy name from arguments
        val therapyName = arguments?.getString("therapyName")

        // Set therapy name in TextView
        view.findViewById<TextView>(R.id.textView6).text = therapyName

        // Set up back button click listener
        view.findViewById<ImageView>(R.id.back_button).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Fetch upcoming therapy sessions for the logged-in user
        fetchUpcomingAppointments()

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            // Show the therapy session dialog with therapy name
            TherapySessionDialog.newInstance(therapyName ?: "").show(childFragmentManager, "TherapySessionDialog")
        }

    }

    private fun fetchUpcomingAppointments() {
        // Initialize the Firebase database reference
        database = Firebase.database.reference.child("therapy_sessions")

        // Get the logged-in user's ID
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid  // This will be the logged-in user's ID
        val therapyName = arguments?.getString("therapyName") // Retrieve therapy name from arguments

        if (userId != null) {
            // Fetch upcoming therapy sessions for this user
            database.orderByChild("userId").equalTo(userId).get().addOnSuccessListener { snapshot ->
                val cardItems = mutableListOf<Appointment>()
                snapshot.children.forEach { childSnapshot ->
                    val appointment = childSnapshot.getValue<Appointment>()
                    if (appointment != null && appointment.status == "upcoming" && appointment.therapyName == therapyName) { // Filter by therapy name
                        cardItems.add(appointment)
                    }
                }

                // Update the adapter with fetched appointments
                cardAdapter = AppointmentAdapter(cardItems)
                viewPager.adapter = cardAdapter

                // Notify the adapter of data change
                cardAdapter.notifyDataSetChanged()
            }.addOnFailureListener { exception ->
                // Handle possible errors
                Log.e("TherapyView", "Error fetching appointments: ${exception.message}")
            }
        } else {
            Log.e("TherapyView", "Error: No user is logged in")
        }
    }


    private fun setupBarChart() {
        // Set up the bar chart data
        barChart.animation.duration = animationDuration
        barChart.animate(barSet)
    }

    companion object {
        private val barSet = listOf(
            "JAN" to 4F,
            "FEB" to 7F,
            "MAR" to 2F,
            "MAY" to 2.3F,
            "APR" to 5F,
            "JUN" to 4F
        )

        private const val animationDuration = 1000L

        fun newInstance(therapyName: String): TherapyView {
            val fragment = TherapyView()
            val args = Bundle()
            args.putString("therapyName", therapyName)
            fragment.arguments = args
            return fragment
        }
    }
}

