package com.example.playback.ui.database_test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DBTestViewModel : ViewModel()
{

    var recordID_text = MutableLiveData<String>().apply {
        value = "No record ID"
    }

    var userID_text = MutableLiveData<String>().apply {
        value = "No user ID"
    }
    var artistName_text = MutableLiveData<String>().apply {
        value = "No artist name"
    }
    var popularityScore_text = MutableLiveData<String>().apply {
        value = "No popularity score"
    }

    var songName_text = MutableLiveData<String>().apply {
        value = "No song name"
    }

    var albumName_text = MutableLiveData<String>().apply {
        value = "No album name"
    }

    var songGenre_text = MutableLiveData<String>().apply {
        value = "No song genre"
    }

    var songLat_text = MutableLiveData<Double>().apply {
        value = 0.0
    }

    var songLong_text = MutableLiveData<Double>().apply {
        value = 0.0
    }
}