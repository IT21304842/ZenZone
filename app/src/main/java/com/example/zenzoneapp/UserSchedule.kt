import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zenzoneapp.R
import com.example.zenzoneapp.UserSchduleAdapter
import com.example.zenzoneapp.UserScheduleData

class UserSchedule : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_user_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find RecyclerView in the inflated fragment layout
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        // Create dummy data
        val activityList = listOf(
            UserScheduleData("Yoga", "Morning yoga session"),
            UserScheduleData("Reading", "Read 30 minutes of a book"),
            UserScheduleData("Exercise", "Workout for 1 hour"),
            UserScheduleData("Meditation", "10 minutes meditation"),
            UserScheduleData("Study", "Complete the math assignment"),
            UserScheduleData("Cooking", "Cook a healthy meal")
        )

        // Set the adapter and layout manager for the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = UserSchduleAdapter(activityList)
    }
}
