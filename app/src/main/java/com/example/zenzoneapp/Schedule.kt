package com.example.zenzoneapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import androidx.viewpager.widget.ViewPager
import com.example.zenzoneapp.databinding.FragmentScheduleBinding
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*

class Schedule : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)

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

        // Fill the GridLayout with day numbers
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
            gridLayout.addView(dayTextView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



