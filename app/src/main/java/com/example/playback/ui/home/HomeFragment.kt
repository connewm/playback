package com.example.playback.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playback.DBManager
import com.example.playback.R
import com.example.playback.SpotifyPersonalData

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private  val TAG = "HomeFragment"

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.v(TAG,"called onCreateView")


        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })




        var dataForRecylerView = arrayOf("TEST1","TEST2","TEST3","TEST4","TEST5","TEST6","TEST7","TEST8","TEST9","TEST10","TEST11","TEST12","TEST13")
        viewManager = LinearLayoutManager(this.activity)
        viewAdapter = SongAdapter(dataForRecylerView)

        recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }



        return root
    }

}