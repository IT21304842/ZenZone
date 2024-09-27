package com.example.zenzoneapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserPostChatsAdapter(private var postList: List<PostItem>) : RecyclerView.Adapter<UserPostChatsAdapter.PostViewHolder>() {

    var filteredPostList = postList.toMutableList()

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDes: TextView = itemView.findViewById(R.id.tvDes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_chat_list_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = filteredPostList[position]
        holder.tvName.text = post.activityName
        holder.tvDes.text = post.postContent
    }

    override fun getItemCount(): Int {
        return filteredPostList.size
    }

    // Function to filter the posts
    fun filter(query: String) {
        filteredPostList = if (query.isEmpty()) {
            postList.toMutableList()
        } else {
            postList.filter {
                it.postContent.contains(query, true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}

data class PostItem(
    val activityName: String,
    val postContent: String
)

