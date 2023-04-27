package com.example.fitnesstracker.ui.activity_log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.LoggedActivity
import com.example.fitnesstracker.MyRecyclerAdapter
import com.example.fitnesstracker.databinding.FragmentActivityLogBinding


class ActivityLogFragment : Fragment() {

    private var _binding: FragmentActivityLogBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ActivityLogViewModel::class.java)

        _binding = FragmentActivityLogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById<View>(com.example.fitnesstracker.R.id.recycler_view) as RecyclerView

        // viewAdapter for the data
        //Uncomment this and the genActivities func to gen test data
        recyclerView.adapter = MyRecyclerAdapter(genActivities(20))



        recyclerView.layoutManager = LinearLayoutManager(activity)

        // Divider
        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    // Generate Data (Used for testing purposes)
    private fun genActivities(size: Int) : ArrayList<LoggedActivity>{
        val activities = ArrayList<LoggedActivity>()
        for (i in 1..size) {
            val person = LoggedActivity("Date: Test $i", "Time Elapsed: Test $i", "Distance: Test $i")
            activities.add(person)
        }
        return activities
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}