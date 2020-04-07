package com.example.playback.ui.database_test

import android.app.ActionBar
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
import android.widget.LinearLayout
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
import org.w3c.dom.Text
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

        val record_id: EditText = root.findViewById(R.id.recordID)
        val user_id: EditText = root.findViewById(R.id.userID)
        val artist_name: EditText = root.findViewById(R.id.artistName)
        val popularity_score: EditText = root.findViewById(R.id.popularityScore)
        val songName: EditText = root.findViewById(R.id.songName)
        val albumName: EditText = root.findViewById(R.id.albumName)
        val songGenre: EditText = root.findViewById(R.id.songGenre)

        val db_text_test: TextView = root.findViewById(R.id.test_db)

        // create db
        try {

            db = DBManager(this.context as Context)
            Log.w("asdf", "db creation succeeded")
        } catch(e: Exception)
        {
            Log.w("asdf", "db creation failed")
        }


        val addButton = root.findViewById<Button>(R.id.add_data_button)
        addButton.setOnClickListener {


            var response: Boolean = false
            try {
                db.writableDatabase
                response = db.addData(
                    SpotifyPersonalData(
                        Integer.parseInt(record_id.text.toString()),
                        Integer.parseInt(user_id.text.toString()),
                        artist_name.text.toString(),
                        Integer.parseInt(popularity_score.text.toString()),
                        songName.text.toString(),
                        albumName.text.toString(),
                        songGenre.text.toString(),
                        0.0,
                        0.0
                    )
                )

                Log.w("asdf", "in add try block")
            } catch(e: Exception) {
                Log.w("asdf", "${e.printStackTrace()}")
                Log.w("asdf", "add not successful")
            }
            if (response)
            {
                Log.w("asdf", "add was successful!!")
            } else {
                Log.w("asdf", "was not successful :(")
            }



        }


        val fetchButton = root.findViewById<Button>(R.id.find_data_button)
        fetchButton.setOnClickListener{
            record_id.visibility = View.GONE
            user_id.visibility = View.GONE
            popularity_score.visibility = View.GONE
            artist_name.visibility = View.GONE
            songName.visibility = View.GONE
            albumName.visibility = View.GONE
            songGenre.visibility = View.GONE

            val arr = db.findData(0)
            val ll_parent: LinearLayout = root.findViewById(R.id.ll_parent)
            for (obj in arr)
            {
                // code to create textview programmtically
                val dynamic_view: TextView = TextView(this.context as Context)
                dynamic_view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                dynamic_view.text = "UserID: ${obj.userId}, RecordID: ${obj.recordId}, Artist Name: ${obj.artistName}, Pop Score: ${obj.popularityScore}, Song Name: ${obj.songName}, Album Name: ${obj.albumName}"
                ll_parent.addView(dynamic_view)
            }
        }
        val removeButton = root.findViewById<Button>(R.id.delete_data_button)
        removeButton.setOnClickListener{
            db.deleteData(0)
        }

        return root
    }

    override fun onClick(v: View?) {
        dbTestViewModel = ViewModelProviders.of(this).get(DBTestViewModel::class.java)
        if (v != null) {
            when(v.id)
            {
                R.id.add_data_button -> {

                    try {

                        db = DBManager(this.context as Context)
                        Log.w("asdf", "db creation succeeded")
                    } catch(e: Exception)
                    {
                        Log.w("asdf", "db creation failed")
                    }
                    val record_id = 0
                    val user_id = 0
                    val artistName: String = dbTestViewModel.artistName_text.value.toString()
                    val popScore: Int = Integer.parseInt(dbTestViewModel.popularityScore_text.value.toString())
                    val songName: String = dbTestViewModel.songName_text.value.toString()
                    val albumName: String = dbTestViewModel.albumName_text.value.toString()
                    val songGenre: String = dbTestViewModel.songGenre_text.value.toString()
                    val songLatitude: Double = dbTestViewModel.songLat_text.value as Double
                    val songLongitude: Double = dbTestViewModel.songLong_text.value as Double

                    var response: Boolean = false
                    try {
                        response = db.addData(
                            SpotifyPersonalData(
                                record_id,
                                user_id,
                                artistName,
                                0,
                                songName,
                                albumName,
                                songGenre,
                                songLatitude,
                                songLongitude
                            )
                        )

                        Log.w("asdf", "in add try block")
                    } catch(e: Exception) {
                        Log.w("asdf", "add not successful")
                    }

                    if (response)
                    {
                        Log.w("asdf", "add was successful!!")
                    } else {
                        Log.w("asdf", "was not successful :(")
                    }
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