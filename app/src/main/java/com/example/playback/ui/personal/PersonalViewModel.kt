package com.example.playback.ui.personal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonalViewModel : ViewModel()
{
    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to your Personal stats"
    }
    val text: LiveData<String> = _text
}