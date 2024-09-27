package com.example.zenzoneapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2

class Connect : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_connect, container, false)

        // Initialize ViewPager2
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = ChatPostViewPagerAdapter(requireActivity())

        // Initialize TabLayout
        val tabLayout = view.findViewById<com.google.android.material.tabs.TabLayout>(R.id.tablayout)

        // Link TabLayout and ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Yours"
                1 -> "Recent"
                else -> ""
            }
        }.attach()

        return view
    }
}
