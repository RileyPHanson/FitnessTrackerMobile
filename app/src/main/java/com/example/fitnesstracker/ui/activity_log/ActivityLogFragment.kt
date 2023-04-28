package com.example.fitnesstracker.ui.activity_log

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.LoggedActivity
import com.example.fitnesstracker.MyRecyclerAdapter
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.FragmentActivityLogBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Session
import java.util.concurrent.TimeUnit


class ActivityLogFragment : Fragment() {
    private lateinit var thiscontext: Context

    private var _binding: FragmentActivityLogBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var session: Session? = null
    private var inSession: Boolean = false

    private val fitnessOption = FitnessOptions.builder()
        .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_WRITE)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[ActivityLogViewModel::class.java]

        _binding = FragmentActivityLogBinding.inflate(inflater, container, false)

        val sessionButton: Button = binding.sessionButton
        sessionButton.setOnClickListener {
            if (session == null && !inSession) {
                startSession()
            } else {
                endSession()
            }
        }

        return binding.root
    }
    private fun startSession() {
        // Check if user is authenticated for Google Play Services
        val account = GoogleSignIn.getAccountForExtension(requireContext(), fitnessOption)
        if (!GoogleSignIn.hasPermissions(account, fitnessOption)) {
            GoogleSignIn.requestPermissions(
                this,
                1,
                account,
                fitnessOption
            )
            return
        }
        inSession = true
        Toast.makeText(context, "Activity session started!", Toast.LENGTH_SHORT).show()

        // Create new session and start recording data

    }


    private fun endSession() {
        inSession = false
        Toast.makeText(context, "Activity session started!", Toast.LENGTH_SHORT).show()
    }



    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById<View>(com.example.fitnesstracker.R.id.recycler_view) as RecyclerView

        // Build the recyclerView from the database
        recyclerView.adapter = MyRecyclerAdapter(createFromDb())

        recyclerView.layoutManager = LinearLayoutManager(activity)
        // Divider
        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Check if location and activity recognition permissions are granted

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACTIVITY_RECOGNITION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
           return
        }

    }

    private fun createFromDb() : ArrayList<LoggedActivity>{
        val dbHelper = ActivityDbHelper(thiscontext)
        val activities = ArrayList<LoggedActivity>()
        val cursor = dbHelper.viewAllData

        while (cursor.moveToNext()){
            val activity = LoggedActivity("Date: ${cursor.getString(1)}", " Time Elapsed: ${cursor.getString(2)}", "Distance: ${cursor.getString(3)}")
            activities.add(activity)
        }

        return activities
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
