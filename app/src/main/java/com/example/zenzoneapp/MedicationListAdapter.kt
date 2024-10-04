import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zenzoneapp.Medication
import com.example.zenzoneapp.R

class MedicationListAdapter(private val medicationList: List<Medication>) :
    RecyclerView.Adapter<MedicationListAdapter.MedicationViewHolder>() {

    inner class MedicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicationName: TextView = itemView.findViewById(R.id.medication)
        val medicationDose: TextView = itemView.findViewById(R.id.dose)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medication_list_item, parent, false)
        return MedicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        val medication = medicationList[position]
        holder.medicationName.text = medication.name
        holder.medicationDose.text = medication.dose
    }

    override fun getItemCount(): Int {
        return medicationList.size
    }


}
