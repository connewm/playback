package com.example.playback.ui.database_test

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.playback.DBManager
import com.example.playback.R
import com.example.playback.SpotifyPersonalData
import com.example.playback.ui.personal.PersonalViewModel
import kotlinx.android.synthetic.main.fragment_database.*
import kotlinx.android.synthetic.main.fragment_personal.*

class DBTestFragment : Fragment(), View.OnClickListener
{
    private lateinit var dbTestViewModel: DBTestViewModel
    private val TAG = "DBTestFragment"

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbTestViewModel = ViewModelProviders.of(this).get(DBTestViewModel::class.java)
        val root =
            inflater.inflate(R.layout.fragment_database, container, false)

        val addButton = root.findViewById<Button>(R.id.Add_Button)
        addButton.setOnClickListener(this)
        val fetchButton = root.findViewById<Button>(R.id.Fetch_Button)
        fetchButton.setOnClickListener(this)
        val removeButton = root.findViewById<Button>(R.id.Delete_Button)
        removeButton.setOnClickListener(this)

        val record_id: TextView = root.findViewById(R.id.recordID)
        val user_id: TextView = root.findViewById(R.id.userID)
        val artist_name: TextView = root.findViewById(R.id.artistName)
        val popularity_score: TextView = root.findViewById(R.id.popularityScore)

        dbTestViewModel.recordID_text.observe(this, Observer {
            record_id.text = it
        })

        dbTestViewModel.userID_text.observe(this, Observer {
            user_id.text = it
        })

        dbTestViewModel.artistName_text.observe(this, Observer {
            artist_name.text = it
        })

        dbTestViewModel.popularityScore_text.observe(this, Observer {
            popularity_score.text = it
        })

        return root
    }

    override fun onClick(v: View?) {
        dbTestViewModel = ViewModelProviders.of(this).get(DBTestViewModel::class.java)
        if (v != null) {
            when(v.id)
            {
                R.id.Add_Button -> {
                    dbTestViewModel.recordID_text.value = "Adding record ID"
                    dbTestViewModel.userID_text.value = "Adding user ID"
                    dbTestViewModel.artistName_text.value = "Adding artist name"
                    dbTestViewModel.popularityScore_text.value = "Adding popularity score"
                }
                R.id.Fetch_Button -> {
                    dbTestViewModel.recordID_text.value = "Looking for record ID"
                    dbTestViewModel.userID_text.value = "Looking for user ID"
                    dbTestViewModel.artistName_text.value = "Looking for artist name"
                    dbTestViewModel.popularityScore_text.value = "Looking for popularity score"
                }
                R.id.Delete_Button -> {
                    dbTestViewModel.recordID_text.value = "Deleting record ID"
                    dbTestViewModel.userID_text.value = "Deleting user ID"
                    dbTestViewModel.artistName_text.value = "Deleting artist name"
                    dbTestViewModel.popularityScore_text.value = "Deleting popularity score"
                }

            }
        }
    }

}