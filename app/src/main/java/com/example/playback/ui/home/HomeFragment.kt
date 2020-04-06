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

        //setup operations
        Log.v(TAG,"called onCreateView")
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        //create the viewModel which will pull the data needed from the database
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)


        //creates a text view then sets that textViews text, to the data from the model
        //creates the view based off the xml at R.id.text_home which is in the home fragment layout xml
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {  //get the data from the variable text from the viewModel, this data cn be changed live, such as in a recyceler view
            textView.text = it
        })



        Log.v(TAG,"obtaining spotify data")
        homeViewModel.data.observe(this, Observer {  //get the data from the variable text from the viewModel, this data cn be changed live, such as in a recyceler view

            var dataForRecylerView: Map<String,Int> = it
            Log.v(TAG,"the data is: " + dataForRecylerView.toString())

            viewManager = LinearLayoutManager(this.activity)
            viewAdapter = SongAdapter(dataForRecylerView.toList().toTypedArray())
            Log.v(TAG,"the data as an array: " + dataForRecylerView.toString().toList().toTypedArray())
            
            recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view).apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)

                // use a linear layout manager
                layoutManager = viewManager

                // specify an viewAdapter (see also next example)
                adapter = viewAdapter
            }
        })





        return root
    }

}