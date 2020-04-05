package com.example.playback.ui.database_test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.playback.DBActivity
import com.example.playback.DBManager
import com.example.playback.R
import com.example.playback.SpotifyPersonalData
import com.example.playback.ui.personal.PersonalViewModel

// do your work here shen, and you'll have to add this fragment to the main activity

class DBTestFragment : Fragment(), View.OnClickListener
{
    private lateinit var dbTestViewModel: DBTestViewModel
    private val TAG = "PersonalFragment"

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View =
            inflater.inflate(R.layout.fragment_personal, container, false)
        val addButton = v.findViewById<Button>(R.id.button3)
        addButton.setOnClickListener(this)
        val fetchButton = v.findViewById<Button>(R.id.button2)
        fetchButton.setOnClickListener(this)
        val removeButton = v.findViewById<Button>(R.id.button)
        removeButton.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if(v.id == R.id.button3) {
                startActivity(
                    Intent(activity!!.applicationContext, DBActivity::class.java)
                )
            }
        }
    }
}