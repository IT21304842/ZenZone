package com.example.zenzoneapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppointmentAdapter(private val cardItems: List<Appointment>) :
    RecyclerView.Adapter<AppointmentAdapter.CardViewHolder>() {

    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appointmentDate: TextView = view.findViewById(R.id.AppointmentDate)
        val appointmentTime: TextView = view.findViewById(R.id.AppointmentTime)
        val appointmentLocation: TextView = view.findViewById(R.id.AppointmentLocation)
        val appointmentTherapist: TextView = view.findViewById(R.id.AppointmentTherapist)
        val appointmentNotes: EditText = view.findViewById(R.id.AppointmentNotes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.appointment_list_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val appointment = cardItems[position]
        holder.appointmentDate.text = appointment.date
        holder.appointmentTime.text = appointment.time
        holder.appointmentLocation.text = appointment.location
        holder.appointmentTherapist.text = appointment.therapist
        holder.appointmentNotes.setText(appointment.notes)
    }

    override fun getItemCount(): Int = cardItems.size
}
