package com.example.zenzoneapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class DailyActivitiesAdapter(
    private val items: List<CardItem>,
    private val onItemClick: (Int) -> Unit // A lambda function to handle item clicks
) : RecyclerView.Adapter<DailyActivitiesAdapter.CardViewHolder>() {

    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvName)
        val descriptionTextView: TextView = view.findViewById(R.id.tvDes)
        val cardView: CardView = view.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_activities_list_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.name
        holder.descriptionTextView.text = item.description

        // Set a click listener on the card
        holder.cardView.setOnClickListener {
            onItemClick(position) // Call the click listener
        }
    }

    override fun getItemCount() = items.size
}

// A data class to represent card items
data class CardItem(val name: String, val description: String)

