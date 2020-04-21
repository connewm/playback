package com.example.playback.ui.personal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonalViewModel : ViewModel()
{
    var time_filter_text = MutableLiveData<String>().apply {
        value = ""
    }


    var pref_filter_text = MutableLiveData<String>().apply {
        value = ""
    }

}