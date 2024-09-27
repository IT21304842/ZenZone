package com.example.zenzoneapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserChatPosts : Fragment() {

    private lateinit var postAdapter: UserPostChatsAdapter
    private lateinit var postList: List<PostItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_chat_posts, container, false)

        // Initialize postList with your data
        postList = listOf(
            PostItem("Activity 1", "This is the content of post 1."),
            PostItem("Activity 2", "Content for post 2."),
            PostItem("Activity 3", "Details for post 3.")
        )

        // Set up RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        postAdapter = UserPostChatsAdapter(postList)
        recyclerView.adapter = postAdapter

        // Set up SearchView
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Perform search when query is submitted
                query?.let { postAdapter.filter(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Update the search results as the query changes
                newText?.let { postAdapter.filter(it) }
                return true
            }
        })

        return view
    }

}
