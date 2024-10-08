package com.example.zenzoneapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TherapyListAdapter(
    private val therapyList: List<TherapyItem>,
    private val onItemClick: (TherapyItem) -> Unit
) : RecyclerView.Adapter<TherapyListAdapter.TherapyViewHolder>() {

    class TherapyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgTitle: ImageView = itemView.findViewById(R.id.imgTitle)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TherapyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.therapy_list_item, parent, false)
        return TherapyViewHolder(view)
    }

    override fun onBindViewHolder(holder: TherapyViewHolder, position: Int) {
        val therapy = therapyList[position]
        holder.tvName.text = therapy.acronym

        // Load the image from the URL using Glide
        Glide.with(holder.itemView.context)
            .load(therapy.imageUrl)
            .into(holder.imgTitle)

        holder.itemView.setOnClickListener {
            // Navigate to TherapyViewFragment
            val fragment = TherapyView.newInstance(therapy.name)
            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return therapyList.size
    }
}

