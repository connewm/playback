package com.example.playback.ui.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.playback.R

class PersonalFragment : Fragment()
{
    private lateinit var personalViewModel: PersonalViewModel

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_personal, container, false)
        val textView: TextView = root.findViewById(R.id.text_personal)
        personalViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}