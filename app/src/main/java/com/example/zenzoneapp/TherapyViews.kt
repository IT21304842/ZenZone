package com.example.zenzoneapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.db.williamchart.view.BarChartView

class TherapyViews : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var cardAdapter: AppointmentAdapter
    private lateinit var barChart: BarChartView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_therapy_views)

        // Handle window insets for immersive experience
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize ViewPager2 and Adapter
        viewPager = findViewById(R.id.viewPager)

        // Sample appointment data
        val cardItems = listOf(
            Appointment("2024-09-30", "10:00 AM", "Location A", "Therapist A", ""),
            Appointment("2024-10-01", "11:00 AM", "Location B", "Therapist B", ""),
            Appointment("2024-10-02", "12:00 PM", "Location C", "Therapist C", "")
        )

        cardAdapter = AppointmentAdapter(cardItems)
        viewPager.adapter = cardAdapter

        // Initialize the bar chart
        barChart = findViewById(R.id.barChart)
        setupBarChart()
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
    }
}
