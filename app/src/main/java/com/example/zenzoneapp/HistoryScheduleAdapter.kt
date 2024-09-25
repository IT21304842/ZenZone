package com.example.zenzoneapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class HistoryScheduleAdapter(
    private val historyScheduleList: List<HistoryScheduleData>,
    private val onCommentClick: (HistoryScheduleData) -> Unit
) : RecyclerView.Adapter<HistoryScheduleAdapter.HistoryScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_schedule_list_item, parent, false)
        return HistoryScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryScheduleViewHolder, position: Int) {
        val historySchedule = historyScheduleList[position]
        holder.tvName.text = historySchedule.name
        holder.tvDescription.text = historySchedule.description

        // If there's a comment, set it
        if (historySchedule.comment.isNotEmpty()) {
            holder.tvComment.text = historySchedule.comment
        } else {
            holder.tvComment.text = "Add your comment..."
        }

        // Handle comment card click
        holder.commentCardView.setOnClickListener {
            onCommentClick(historySchedule)
        }
    }

    override fun getItemCount(): Int {
        return historyScheduleList.size
    }

    class HistoryScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDes)
        val tvComment: TextView = itemView.findViewById(R.id.commentTextView)
        val commentCardView: CardView = itemView.findViewById(R.id.commentCardView)
//        val commentImageView: ImageView = itemView.findViewById(R.id.commentimg)
    }
}
