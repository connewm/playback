package com.example.playback.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.playback.R

class MapFragment : Fragment() {

    private lateinit var mapViewModel: MapViewModel
    private  val TAG = "MapFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.v(TAG,"called onCreateView")
        mapViewModel =
            ViewModelProviders.of(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_maps, container, false)
        val textView: TextView = root.findViewById(R.id.map_page)
        mapViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}