package com.example.playback.ui.home

import android.content.Context
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
import com.example.playback.SpotifyConnector
import com.example.playback.SpotifyPersonalData
import java.lang.Exception
import kotlin.math.log

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val TAG = "HomeFragment"

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //setup operations
        Log.v(TAG, "called onCreateView")
        val root = inflater.inflate(R.layout.fragment_home, container, false)



        viewManager = LinearLayoutManager(this.activity)

        var sc = SpotifyConnector()
        var recentlyPLayed = sc.getRecentlyPlayed()
        viewAdapter = SongAdapter(recentlyPLayed)
        //Log.v(TAG,"the data as an array: " + dataForRecylerView.toString().toList().toTypedArray())
        //Log.v(TAG,"the data as an array: " + dataSet)


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


    fun qurieSpotifyForAlbumCovers() {

    }
}