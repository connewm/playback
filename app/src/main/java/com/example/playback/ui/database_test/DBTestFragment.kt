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
import com.example.playback.HistoryRecord
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



        // create db
        try {

            db = DBManager(this.context as Context)
            Log.w("asdf", "db creation succeeded")
        } catch(e: Exception)
        {
            Log.w("asdf", "db creation failed")
        }


        val song = root.findViewById<Button>(R.id.pull_song)
        song.setOnClickListener {
            val arr = db.getSongs(HistoryRecord.TIME_DAILY)
            val ll_parent: LinearLayout = root.findViewById(R.id.ll_parent)

            for (obj in arr)
            {
                // code to create textview programmtically
                val dynamic_view: TextView = TextView(this.context as Context)
                dynamic_view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                dynamic_view.text = "Song: ${obj.songName}, Artist: ${obj.artist}, Listens: ${obj.listens}"
                ll_parent.addView(dynamic_view)
            }





        }


        val artist = root.findViewById<Button>(R.id.pull_artist)
        artist.setOnClickListener{


            val arr = db.getArtists(HistoryRecord.TIME_DAILY)
            val ll_parent: LinearLayout = root.findViewById(R.id.ll_parent)

            for (obj in arr)
            {
                // code to create textview programmtically
                val dynamic_view: TextView = TextView(this.context as Context)
                dynamic_view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                dynamic_view.text = "Artist: ${obj.artist}, Listens: ${obj.listens}"
                ll_parent.addView(dynamic_view)
            }
        }

        val album = root.findViewById<Button>(R.id.pull_album)
        album.setOnClickListener{

            val arr = db.getAlbums(HistoryRecord.TIME_DAILY)
            val ll_parent: LinearLayout = root.findViewById(R.id.ll_parent)

            for (obj in arr)
            {
                // code to create textview programmtically
                val dynamic_view: TextView = TextView(this.context as Context)
                dynamic_view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                dynamic_view.text = "Album: ${obj.album}, Artist: ${obj.songName}, Listens: ${obj.listens}"
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