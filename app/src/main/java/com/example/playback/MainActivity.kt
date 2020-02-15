package com.example.playback

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v(TAG,"called onCreate")

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG,"called onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG,"called onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG,"called onResume")
    }

    override fun onPause() {
        Log.v(TAG,"calling onPause")
        super.onPause()
        Log.v(TAG,"called onPause")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(TAG,"called onRestart")
    }

    override fun onDestroy() {
        Log.v(TAG,"calling onDestroy")
        super.onDestroy()
    }


}
