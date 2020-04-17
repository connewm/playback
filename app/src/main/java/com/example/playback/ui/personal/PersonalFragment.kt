package com.example.playback.ui.personal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.playback.R
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.playback.DBManager
import com.example.playback.SpotifyPersonalData
import java.lang.Exception
import java.util.ArrayList


class PersonalFragment : Fragment(), View.OnClickListener
{
    private lateinit var personalViewModel: PersonalViewModel
    private val TAG = "PersonalFragment"

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_personal, container, false)
        // expand all of the buttons and add on click listener

        val mDailyButton: Button = root.findViewById(R.id.daily_button)
        mDailyButton.setOnClickListener(this)
        val mWeeklyButton: Button = root.findViewById(R.id.weekly_button)
        mWeeklyButton.setOnClickListener(this)
        val mMonthlyButton: Button = root.findViewById(R.id.monthly_button)
        mMonthlyButton.setOnClickListener(this)
        val mAllTimeButton: Button = root.findViewById(R.id.all_time_button)
        mAllTimeButton.setOnClickListener(this)

        // get other filter buttons
        val mSongButton: Button = root.findViewById(R.id.song_button)
        mSongButton.setOnClickListener(this)
        val mArtistButton: Button = root.findViewById(R.id.artist_button)
        mArtistButton.setOnClickListener(this)
        val mAlbumButton: Button = root.findViewById(R.id.album_button)
        mAlbumButton.setOnClickListener(this)

        // update the time text if needed
        val time_tv: TextView = root.findViewById(R.id.time_text_personal)
        val pref_tv: TextView = root.findViewById(R.id.pref_text_personal)

        personalViewModel.time_filter_text.observe(this, Observer {
            time_tv.text = it
        })

        personalViewModel.pref_filter_text.observe(this, Observer {
            pref_tv.text = it
        })
/**
        try {

            var db = DBManager(this.context as Context)
            Log.w("asdf", "db creation succeeded")


            var pie : Pie = AnyChart.pie()
            var data = ArrayList<DataEntry>()

            data.add(ValueDataEntry("mac Miller", 20))
            data.add(ValueDataEntry("Kanye West", 15))
            data.add(ValueDataEntry("Tame Impala", 11))
            data.add(ValueDataEntry("San Cisco", 9))
            data.add(ValueDataEntry("Black Keys", 32))

            pie.data(data);

            pie.background().fill("#404040")
            var anyChartView: AnyChartView = root.findViewById(R.id.any_chart_view)
            anyChartView.setChart(pie)

        } catch(e: Exception)
        {
            Log.w("asdf", "db creation failed")
        }
        */


        return root
    }


    override fun onClick(v: View)
    {
        // insanity test to see if we can have persistance when selecting buttons
        var currentlySelected: String = ""
        personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
        // first want to find out if its a time filter or a pref filter that has been selected
        if (v.id == R.id.daily_button || v.id == R.id.weekly_button || v.id == R.id.monthly_button || v.id == R.id.all_time_button)
        {
            when(v.id)
            {
                R.id.daily_button -> {
                    personalViewModel.time_filter_text.value = "Daily Selected"
                }
                R.id.monthly_button -> {
                    personalViewModel.time_filter_text.value = "Monthly Selected"
                }
                R.id.weekly_button -> {
                    personalViewModel.time_filter_text.value = "Weekly Selected"
                }
                R.id.all_time_button -> {
                    personalViewModel.time_filter_text.value = "All Time Selected"
                }

            }
        } else
        {
            when(v.id)
            {
                R.id.song_button -> {
                    personalViewModel.pref_filter_text.value = "Song Selected"
                }
                R.id.artist_button -> {
                    personalViewModel.pref_filter_text.value = "Artist Selected"
                }
                R.id.album_button -> {
                    personalViewModel.pref_filter_text.value = "Album Selected"
                }
            }
        }

    }



}