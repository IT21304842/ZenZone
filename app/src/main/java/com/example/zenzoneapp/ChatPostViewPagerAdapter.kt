package com.example.zenzoneapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ChatPostViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        // Number of tabs (Yours, Recent)
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        // Return the correct fragment based on the tab position
        return when (position) {
            0 -> UserChatPosts()  // First tab - Yours
            1 -> RecommendationSchedule()  // Second tab - Recent
            else -> UserChatPosts()
        }
    }
}
