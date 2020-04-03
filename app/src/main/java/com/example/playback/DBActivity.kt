package com.example.playback

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_database.*

class DBActivity : AppCompatActivity() {

    val TAG = "DBActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

    }
	
	fun newData() {
        val dbManager = DBManager(this, null, null, 1)
        val recordId = Integer.parseInt(recordID.toString())
        val userId = Integer.parseInt(userID.toString())
        val artistname = artistName.toString()
        val popularityscore = Integer.parseInt(popularityScore.toString())

        val data = SpotifyPersonalData(recordId, userId, artistname, popularityscore)
        dbManager.addData(data)
        recordID.setText("")
        userID.setText("")
        artistName.setText("")
        popularityScore.setText("")
    }

    fun lookupData(){
        val dbManager = DBManager(this, null, null, 1)
        val data = dbManager.findData(Integer.parseInt(userID.toString()))
        if(data != null){
            recordID.setText(data.recordId.toString())
            artistName.setText(data.artistName.toString())
            popularityScore.setText(data.popularityScore.toString())
        }else{
            userID.setText("No Match Found")
        }
    }

    fun removeData(){
        val dbManager = DBManager(this, null, null, 1)
        val data = dbManager.deleteData(Integer.parseInt(userID.toString()))
        if(data){
            userID.setText("Record deleted")
            recordID.setText("")
            artistName.setText("")
            popularityScore.setText("")
        }else{
            userID.setText("No Match Found")
        }
    }

}