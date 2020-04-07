package com.example.playback

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.playback.ui.database_test.DBTestFragment
import com.example.playback.ui.database_test.DBTestViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_database.*
import kotlinx.android.synthetic.main.fragment_personal.*
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    lateinit var db : DBManager

    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "called onCreate")

        /**
         * Before creating the view for the main activity we want to read from the provided JSON
         * file and store that data in the database for the user.
         * This process will mimic what our app will do every time the Main Activity is created
         * Which is call the Spotify API for the users top songs/artists/etc. and either create
         * a record for them, or update the current record so the user can view their personal
         * Spotify data in the Personal page
         *
         * Unfortunately we don't have the Spotify API 100% working yet, but the JSONs returned by
         * API calls will look exactly like the one stored in the res/raw directory now.
         */

        setContentView(R.layout.activity_main)

        //db = DBManager(this.applicationContext)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_maps,
                R.id.navigation_personal
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    // These 3 functions are not implemented yet because they depends on
    // which fragment will do these operations
    fun newData(v:View){

    }

    fun lookupData(v:View){

    }

    fun deleteData(v:View){

    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "called onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "called onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "called onResume")
    }

    override fun onPause() {
        Log.v(TAG, "calling onPause")
        super.onPause()
        Log.v(TAG, "called onPause")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(TAG, "called onRestart")
    }

    override fun onDestroy() {
        Log.v(TAG, "calling onDestroy")
        super.onDestroy()
    }


}