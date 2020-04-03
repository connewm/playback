package com.example.playback.ui.personal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.playback.DBActivity
import com.example.playback.R


class PersonalFragment : Fragment() , View.OnClickListener
{
    private lateinit var personalViewModel: PersonalViewModel
    private val TAG = "PersonalFragment"

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View =
            inflater.inflate(R.layout.activity_database, container, false)
        val addButton = v.findViewById<Button>(R.id.button3)
        addButton.setOnClickListener(this)
        val fetchButton = v.findViewById<Button>(R.id.button2)
        fetchButton.setOnClickListener(this)
        val removeButton = v.findViewById<Button>(R.id.button)
        removeButton.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if(v.id == R.id.button3) {
                startActivity(
                    Intent(activity!!.applicationContext, DBActivity::class.java)
                )
            }
        }
    }


}