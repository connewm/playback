package com.example.playback.ui.social

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SocialViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to your social page"
    }
    val text: LiveData<String> = _text
}