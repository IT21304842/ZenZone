package com.example.zenzoneapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.Calendar
import java.util.Locale

class Home : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Get references to the TextViews
        val currentMonthTextView: TextView = view.findViewById(R.id.currentMonthTextView)
        val date1: TextView = view.findViewById(R.id.date1)
        val date2: TextView = view.findViewById(R.id.date2)
        val date3: TextView = view.findViewById(R.id.date3)
        val date4: TextView = view.findViewById(R.id.date4)
        val date5: TextView = view.findViewById(R.id.date5)
        val date6: TextView = view.findViewById(R.id.date6)
        val date7: TextView = view.findViewById(R.id.date7)
        val todayCircle: View = view.findViewById(R.id.todayCircle)

        // Get the current calendar instance
        val calendar = Calendar.getInstance()

        // Set the current month name
        val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        currentMonthTextView.text = monthName

        // Get the current date
        val currentDate = calendar.get(Calendar.DAY_OF_MONTH)

        // Set the dates for the current week
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1 = Sunday, 2 = Monday, ...
        val dayOffset = firstDayOfWeek - Calendar.SUNDAY // Adjust to start from Sunday
        calendar.add(Calendar.DAY_OF_MONTH, -dayOffset) // Move to the Sunday of the current week

        // Populate the TextViews for the week dates
        date1.text = (calendar.get(Calendar.DAY_OF_MONTH)).toString()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        date2.text = (calendar.get(Calendar.DAY_OF_MONTH)).toString()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        date3.text = (calendar.get(Calendar.DAY_OF_MONTH)).toString()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        date4.text = (calendar.get(Calendar.DAY_OF_MONTH)).toString()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        date5.text = (calendar.get(Calendar.DAY_OF_MONTH)).toString()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        date6.text = (calendar.get(Calendar.DAY_OF_MONTH)).toString()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        date7.text = (calendar.get(Calendar.DAY_OF_MONTH)).toString()

        // Set the visibility and position of todayCircle
        if (currentDate >= 1 && currentDate <= 7) {
            todayCircle.visibility = View.VISIBLE

            // Get the position of today's date TextView
            val todayTextViewId = when (currentDate) {
                1 -> R.id.date1
                2 -> R.id.date2
                3 -> R.id.date3
                4 -> R.id.date4
                5 -> R.id.date5
                6 -> R.id.date6
                7 -> R.id.date7
                else -> R.id.date1 // default to avoid error
            }

            // Get the TextView for today's date
            val todayTextView: TextView = view.findViewById(todayTextViewId)

            // Get the layout parameters of the TextView
            val layoutParams = todayCircle.layoutParams as ViewGroup.MarginLayoutParams

            // Position the circle around the TextView
            layoutParams.setMargins(
                todayTextView.left,
                todayTextView.top,
                todayTextView.right,
                todayTextView.bottom
            )

            todayCircle.layoutParams = layoutParams
        } else {
            todayCircle.visibility = View.GONE // Hide if the current date is not within range
        }

        return view
    }
}
