package com.example.zenzoneapp

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class DailyActivitiesAdapter(
    private val items: List<ActivityData>,
    private val onItemClick: (Int) -> Unit, // A lambda function to handle item clicks
    private val onUpdateStatus: (ActivityData) -> Unit // A lambda function to update status
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
        holder.nameTextView.text = item.activityName
        holder.descriptionTextView.text = item.description

        // Set a click listener on the card
        holder.cardView.setOnClickListener {
            onItemClick(position) // Call the click listener for the card
        }

        // Set click listener for the moreVertIcon
        holder.itemView.findViewById<ImageView>(R.id.moreVertIcon).setOnClickListener {
            // Trigger dialog here
            showActivityDialog(holder.itemView.context, item)
        }
    }

    private fun showActivityDialog(context: Context, item: ActivityData) {
        // Create a dialog for Activity Details
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.activity_details_popup)

        // Set activity name and description in the dialog
        val activityNameTextView = dialog.findViewById<TextView>(R.id.activityName)
        val activityDesTextView = dialog.findViewById<TextView>(R.id.activityDes)
        activityNameTextView.text = item.activityName
        activityDesTextView.text = item.description

        // Close button functionality
        val closeButton = dialog.findViewById<ImageView>(R.id.txtclose)
        closeButton.setOnClickListener {
            dialog.dismiss() // Close the dialog
        }

        // Complete button functionality
        val completeButton = dialog.findViewById<Button>(R.id.addButton) // "Complete" button
        completeButton.setOnClickListener {
            dialog.dismiss() // Dismiss the "Activity Details" pop-up
            showRateActivitiesDialog(context, item) // Open "Rate Daily Activities" pop-up
        }

        // Show the "Activity Details" dialog
        dialog.show()
    }

    private fun showRateActivitiesDialog(context: Context, item: ActivityData) {
        // Create a dialog for Rate Daily Activities
        val rateDialog = Dialog(context)
        rateDialog.setContentView(R.layout.rate_daily_activities_popup) // Assuming this is your layout file name

        // Get the comment input field
        val commentEditText = rateDialog.findViewById<EditText>(R.id.commentEditText)

        // Close button functionality for Rate Daily Activities
        val closeButton = rateDialog.findViewById<ImageView>(R.id.txtclose)
        closeButton.setOnClickListener {
            rateDialog.dismiss() // Close the "Rate Daily Activities" pop-up
        }

        // Submit button functionality
        val submitButton = rateDialog.findViewById<Button>(R.id.addButton) // "Submit" button
        submitButton.setOnClickListener {
            val comment = commentEditText.text.toString()
            if (comment.isNotBlank()) {
                item.comment = comment // Update the comment in the item
                item.status = "completed" // Update the status to completed
                item.reaction = "sad"
                onUpdateStatus(item) // Notify the fragment to update the database
            }
            rateDialog.dismiss() // Close the dialog
        }

        // Show the "Rate Daily Activities" dialog
        rateDialog.show()
    }

    override fun getItemCount() = items.size
}
