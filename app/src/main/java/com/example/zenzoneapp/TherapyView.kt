package com.example.zenzoneapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.db.williamchart.view.BarChartView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TherapyView : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var cardAdapter: AppointmentAdapter
    private lateinit var barChart: BarChartView

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

        // Get therapy name from arguments
        val therapyName = arguments?.getString("therapyName")

        // Set up ViewPager and BarChart
        setupViewPager()
        setupBarChart()

        // Set therapy name in TextView
        view.findViewById<TextView>(R.id.textView6).text = therapyName

        // Set up back button click listener
        view.findViewById<ImageView>(R.id.back_button).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // FloatingActionButton click listener
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            // Show the therapy session dialog
            TherapySessionDialog.newInstance().show(childFragmentManager, "TherapySessionDialog")
        }
    }


    private fun setupViewPager() {
        // Sample appointment data
        val cardItems = listOf(
            Appointment("2024-09-30", "10:00 AM", "Location A", "Therapist A", ""),
            Appointment("2024-10-01", "11:00 AM", "Location B", "Therapist B", ""),
            Appointment("2024-10-02", "12:00 PM", "Location C", "Therapist C", "")
        )

        cardAdapter = AppointmentAdapter(cardItems)
        viewPager.adapter = cardAdapter
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
