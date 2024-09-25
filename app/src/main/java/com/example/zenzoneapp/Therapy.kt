package com.example.zenzoneapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

class Therapy : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var therapyAdapter: TherapyListAdapter
    private val therapyList = listOf(
        TherapyItem("Therapy 1", R.drawable.t1),
        TherapyItem("Therapy 2", R.drawable.t2),
        TherapyItem("Therapy 3", R.drawable.t3),
        TherapyItem("Therapy 4", R.drawable.t4),
        TherapyItem("Therapy 5", R.drawable.t5),
        TherapyItem("Therapy 6", R.drawable.t6)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_therapy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewTherapies)
        therapyAdapter = TherapyListAdapter(therapyList)

        // Change to GridLayoutManager for horizontal scrolling with 2 columns
        val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = therapyAdapter
    }


}
