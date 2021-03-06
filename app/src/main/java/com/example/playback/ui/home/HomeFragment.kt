package com.example.playback.ui.home

import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.playback.DBManager
import com.example.playback.R
import com.example.playback.SpotifyPersonalData

class HomeFragment : Fragment() {
    lateinit var db : DBManager
    private lateinit var homeViewModel: HomeViewModel
    private  val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.v(TAG,"called onCreateView")

        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })

        var dataSet: ArrayList<SpotifyPersonalData> = db.showRecent()


        return root
    }


}