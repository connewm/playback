package com.example.playback.ui.database_test

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import java.lang.Exception

class DBTestFragment : Fragment(), View.OnClickListener
{
    private lateinit var dbTestViewModel: DBTestViewModel
    lateinit var db : DBManager
    private val TAG = "DBTestFragment"

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbTestViewModel = ViewModelProviders.of(this).get(DBTestViewModel::class.java)
        val root =
            inflater.inflate(R.layout.fragment_database, container, false)

        val addButton = root.findViewById<Button>(R.id.add_data_button)
        addButton.setOnClickListener(this)
        val fetchButton = root.findViewById<Button>(R.id.find_data_button)
        fetchButton.setOnClickListener(this)
        val removeButton = root.findViewById<Button>(R.id.delete_data_button)
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
                R.id.add_data_button -> {
                    dbTestViewModel.recordID_text.value = "Adding record ID"
                    dbTestViewModel.userID_text.value = "Adding user ID"
                    dbTestViewModel.artistName_text.value = "Adding artist name"
                    dbTestViewModel.popularityScore_text.value = "Adding popularity score"
                    try {
                        Log.w("asdf", "db creation succeeded")
                        db = DBManager(this.context as Context)
                    } catch(e: Exception)
                    {
                        Log.w("asdf", "db creation failed")
                    }
                    val record_id = 0
                    val user_id = 0
                    val artistName: String = (v.findViewById(R.id.artistName) as EditText).text.toString()
                    val popScore: Int = Integer.parseInt((v.findViewById(R.id.popularityScore) as EditText).text.toString())
                    val songName: String = (v.findViewById(R.id.songName) as EditText).text.toString()
                    val albumName: String = (v.findViewById(R.id.albumName) as EditText).text.toString()
                    val songGenre: String = (v.findViewById(R.id.songGenre) as EditText).text.toString()
                    val songLatitude: Double = String.toDouble((v.findViewById(R.id.songLatitude) as EditText).text.toString())

                }
                R.id.find_data_button -> {
                    dbTestViewModel.recordID_text.value = "Looking for record ID"
                    dbTestViewModel.userID_text.value = "Looking for user ID"
                    dbTestViewModel.artistName_text.value = "Looking for artist name"
                    dbTestViewModel.popularityScore_text.value = "Looking for popularity score"
                }
                R.id.delete_data_button -> {
                    dbTestViewModel.recordID_text.value = "Deleting record ID"
                    dbTestViewModel.userID_text.value = "Deleting user ID"
                    dbTestViewModel.artistName_text.value = "Deleting artist name"
                    dbTestViewModel.popularityScore_text.value = "Deleting popularity score"
                }

            }
        }
    }

}