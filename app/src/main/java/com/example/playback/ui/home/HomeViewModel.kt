package com.example.playback.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playback.DBManager
import java.security.AccessController.getContext

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    private val _data = MutableLiveData< Map<String,Int>>().apply {
        value =  getSpotifyData()
    }
    val text: LiveData<String> = _text
    var data: LiveData<Map<String,Int>> = _data


    //TODO change to something like get the song data from the database then querie spotify for the album covers or save them?
    fun getSpotifyData( ): Map<String,Int> {



        //var db = DBManager()

        var data = mutableMapOf<String,Int>()
        for (x in 0..30){
            val key = "Test" + x.toString()
            data[key] = 0
        }
        return data.toList().sortedBy { (_, value) -> value}.toMap()
    }
}
