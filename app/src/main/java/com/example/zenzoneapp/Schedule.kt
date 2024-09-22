package com.example.zenzoneapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.text.SimpleDateFormat
import java.util.*

class Schedule : Fragment() {

    private lateinit var currentMonthTextView: TextView
    private lateinit var nextButton: TextView
    private lateinit var previousButton: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private var currentCalendar = Calendar.getInstance()
    private lateinit var yearAdapter: YearAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)

        currentMonthTextView = view.findViewById(R.id.currentMonthTextView)
        nextButton = view.findViewById(R.id.nextButton)
        previousButton = view.findViewById(R.id.previousButton)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)

        // Set up RecyclerView
        calendarRecyclerView.layoutManager = GridLayoutManager(context, 7)
        yearAdapter = YearAdapter(currentCalendar)
        calendarRecyclerView.adapter = yearAdapter

        // Set current year and month
        updateCalendarView()

        // Handle navigation clicks
        nextButton.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            updateCalendarView()
        }

        previousButton.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            updateCalendarView()
        }

        return view
    }

    private fun updateCalendarView() {
        val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        currentMonthTextView.text = monthFormat.format(currentCalendar.time)
        yearAdapter.updateCalendar(currentCalendar)
    }
}
