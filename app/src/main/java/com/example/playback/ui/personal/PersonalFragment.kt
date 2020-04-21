package com.example.playback.ui.personal

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.playback.R
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginLeft
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.anychart.core.annotations.Line
import com.example.playback.DBManager
import com.example.playback.HistoryRecord
import com.example.playback.SpotifyPersonalData
import org.w3c.dom.Text
import java.lang.Exception
import java.util.ArrayList


class PersonalFragment : Fragment(), View.OnClickListener
{
    private lateinit var personalViewModel: PersonalViewModel
    private val TAG = "PersonalFragment"
    private var daily_clicked: Boolean = false
    private var weekly_clicked: Boolean = false
    private var monthly_clicked: Boolean = false
    private var all_time_clicked: Boolean = false
    private var song_clicked: Boolean = false
    private var artist_clicked: Boolean = false
    private var album_clicked: Boolean = false

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_personal, container, false)
        // expand all of the buttons and add on click listener

        val mDailyButton: Button = root.findViewById(R.id.daily_button)
        val mWeeklyButton: Button = root.findViewById(R.id.weekly_button)
        val mMonthlyButton: Button = root.findViewById(R.id.monthly_button)
        val mAllTimeButton: Button = root.findViewById(R.id.all_time_button)

        // get other filter buttons
        val mSongButton: Button = root.findViewById(R.id.song_button)
        val mArtistButton: Button = root.findViewById(R.id.artist_button)
        val mAlbumButton: Button = root.findViewById(R.id.album_button)

        var db = DBManager(this.context as Context)

        mSongButton.setOnClickListener {
            if (daily_clicked) {

                personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
                personalViewModel.time_filter_text.value = "Daily Selected"
                personalViewModel.pref_filter_text.value = "Song Selected"

                song_clicked = false
                artist_clicked = false
                album_clicked = false

                daily_clicked = false
                weekly_clicked = false
                monthly_clicked = false
                all_time_clicked = false

                val arr = db.getSongs(HistoryRecord.TIME_DAILY)
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                var total_listens_top_ten = 0
                for (obj in arr) {
                    total_listens_top_ten += obj.listens
                }

                for (obj in arr) {
                    val dynamic_ll = LinearLayout(this.context as Context)

                    var ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    ll_params.setMargins(0, 10, 0, 10)
                    dynamic_ll.layoutParams = ll_params
                    dynamic_ll.orientation = LinearLayout.HORIZONTAL

                    var bar_tv = TextView(this.context as Context)
                    var desc_tv = TextView(this.context as Context)

                    // calculate relative weight of shape based on num listens
                    var weight: Float = 0.0f
                    weight = (obj.listens.toFloat() / total_listens_top_ten.toFloat())
                    var percent_weight: Float = weight * 100

                    var bar_lp = LinearLayout.LayoutParams(0 ,100, weight)
                    bar_lp.setMargins(20, 0 ,0 ,0)
                    bar_tv.layoutParams = bar_lp
                    bar_tv.background = resources.getDrawable(R.drawable.rectangle)
                    bar_tv.gravity = Gravity.CENTER
                    bar_tv.setPadding(0, 5, 0, 5)
                    bar_tv.text = "${String.format("%.1f", percent_weight).toFloat()}%"

                    var dyn_lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f)
                    dyn_lp.setMargins(0, -5, 25, -5)
                    desc_tv.layoutParams = dyn_lp
                    desc_tv.gravity = Gravity.RIGHT
                    desc_tv.includeFontPadding = false
                    desc_tv.firstBaselineToTopHeight = 0
                    desc_tv.setPadding(0,0,0,0)
                    desc_tv.isSingleLine = false
                    desc_tv.text = "${obj.songName} By: ${obj.artist}"
                    dynamic_ll.addView(bar_tv)
                    dynamic_ll.addView(desc_tv)
                    wrapper.addView(dynamic_ll)
                }
            } else if (weekly_clicked) {
                personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
                personalViewModel.time_filter_text.value = "Weekly Selected"
                personalViewModel.pref_filter_text.value = "Song Selected"

                song_clicked = false
                artist_clicked = false
                album_clicked = false

                daily_clicked = false
                weekly_clicked = false
                monthly_clicked = false
                all_time_clicked = false

                val arr = db.getSongs(HistoryRecord.TIME_WEEKLY)
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                var total_listens_top_ten = 0
                for (obj in arr) {
                    total_listens_top_ten += obj.listens
                }

                for (obj in arr) {
                    val dynamic_ll = LinearLayout(this.context as Context)

                    var ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    ll_params.setMargins(0, 10, 0, 10)
                    dynamic_ll.layoutParams = ll_params
                    dynamic_ll.orientation = LinearLayout.HORIZONTAL

                    var bar_tv = TextView(this.context as Context)
                    var desc_tv = TextView(this.context as Context)

                    // calculate relative weight of shape based on num listens
                    var weight: Float = 0.0f
                    weight = (obj.listens.toFloat() / total_listens_top_ten.toFloat())
                    var percent_weight: Float = weight * 100

                    var bar_lp = LinearLayout.LayoutParams(0 ,100, weight)
                    bar_lp.setMargins(20, 0 ,0 ,0)
                    bar_tv.layoutParams = bar_lp
                    bar_tv.background = resources.getDrawable(R.drawable.rectangle)
                    bar_tv.gravity = Gravity.CENTER
                    bar_tv.setPadding(0, 5, 0, 5)
                    bar_tv.text = "${String.format("%.1f", percent_weight).toFloat()}%"

                    var dyn_lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f)
                    dyn_lp.setMargins(0, -5, 25, -5)
                    desc_tv.layoutParams = dyn_lp
                    desc_tv.gravity = Gravity.RIGHT
                    desc_tv.includeFontPadding = false
                    desc_tv.firstBaselineToTopHeight = 0
                    desc_tv.setPadding(0,0,0,0)
                    desc_tv.isSingleLine = false
                    desc_tv.text = "${obj.songName} By: ${obj.artist}"
                    dynamic_ll.addView(bar_tv)
                    dynamic_ll.addView(desc_tv)
                    wrapper.addView(dynamic_ll)
                }
            } else if (monthly_clicked) {
                personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
                personalViewModel.time_filter_text.value = "Monthly Selected"
                personalViewModel.pref_filter_text.value = "Song Selected"

                song_clicked = false
                artist_clicked = false
                album_clicked = false

                daily_clicked = false
                weekly_clicked = false
                monthly_clicked = false
                all_time_clicked = false

                val arr = db.getSongs(HistoryRecord.TIME_MONTHLY)
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                var total_listens_top_ten = 0
                for (obj in arr) {
                    total_listens_top_ten += obj.listens
                }

                for (obj in arr) {
                    val dynamic_ll = LinearLayout(this.context as Context)

                    var ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    ll_params.setMargins(0, 10, 0, 10)
                    dynamic_ll.layoutParams = ll_params
                    dynamic_ll.orientation = LinearLayout.HORIZONTAL

                    var bar_tv = TextView(this.context as Context)
                    var desc_tv = TextView(this.context as Context)

                    // calculate relative weight of shape based on num listens
                    var weight: Float = 0.0f
                    weight = (obj.listens.toFloat() / total_listens_top_ten.toFloat())
                    var percent_weight: Float = weight * 100

                    var bar_lp = LinearLayout.LayoutParams(0 ,100, weight)
                    bar_lp.setMargins(20, 0 ,0 ,0)
                    bar_tv.layoutParams = bar_lp
                    bar_tv.background = resources.getDrawable(R.drawable.rectangle)
                    bar_tv.gravity = Gravity.CENTER
                    bar_tv.setPadding(0, 5, 0, 5)
                    bar_tv.text = "${String.format("%.1f", percent_weight).toFloat()}%"

                    var dyn_lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f)
                    dyn_lp.setMargins(0, -5, 25, -5)
                    desc_tv.layoutParams = dyn_lp
                    desc_tv.gravity = Gravity.RIGHT
                    desc_tv.includeFontPadding = false
                    desc_tv.firstBaselineToTopHeight = 0
                    desc_tv.setPadding(0,0,0,0)
                    desc_tv.isSingleLine = false
                    desc_tv.text = "${obj.songName} By: ${obj.artist}"
                    dynamic_ll.addView(bar_tv)
                    dynamic_ll.addView(desc_tv)
                    wrapper.addView(dynamic_ll)
                }
            } else if (all_time_clicked) {
                personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
                personalViewModel.time_filter_text.value = "All Time Selected"
                personalViewModel.pref_filter_text.value = "Song Selected"

                song_clicked = false
                artist_clicked = false
                album_clicked = false

                daily_clicked = false
                weekly_clicked = false
                monthly_clicked = false
                all_time_clicked = false

                val arr = db.getSongs(HistoryRecord.TIME_ALL)
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                var total_listens_top_ten = 0
                for (obj in arr) {
                    total_listens_top_ten += obj.listens
                }

                for (obj in arr) {
                    val dynamic_ll = LinearLayout(this.context as Context)

                    var ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    ll_params.setMargins(0, 10, 0, 10)
                    dynamic_ll.layoutParams = ll_params
                    dynamic_ll.orientation = LinearLayout.HORIZONTAL

                    var bar_tv = TextView(this.context as Context)
                    var desc_tv = TextView(this.context as Context)

                    // calculate relative weight of shape based on num listens
                    var weight: Float = 0.0f
                    weight = (obj.listens.toFloat() / total_listens_top_ten.toFloat())
                    var percent_weight: Float = weight * 100

                    var bar_lp = LinearLayout.LayoutParams(0 ,100, weight)
                    bar_lp.setMargins(20, 0 ,0 ,0)
                    bar_tv.layoutParams = bar_lp
                    bar_tv.background = resources.getDrawable(R.drawable.rectangle)
                    bar_tv.gravity = Gravity.CENTER
                    bar_tv.setPadding(0, 5, 0, 5)
                    bar_tv.text = "${String.format("%.1f", percent_weight).toFloat()}%"

                    var dyn_lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f)
                    dyn_lp.setMargins(0, -5, 25, -5)
                    desc_tv.layoutParams = dyn_lp
                    desc_tv.gravity = Gravity.RIGHT
                    desc_tv.includeFontPadding = false
                    desc_tv.firstBaselineToTopHeight = 0
                    desc_tv.setPadding(0,0,0,0)
                    desc_tv.isSingleLine = false
                    desc_tv.text = "${obj.songName} By: ${obj.artist}"
                    dynamic_ll.addView(bar_tv)
                    dynamic_ll.addView(desc_tv)
                    wrapper.addView(dynamic_ll)
                }
            } else {
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                personalViewModel.time_filter_text.value = "Please select a time filter"
            }
        }

        mArtistButton.setOnClickListener {
            if (daily_clicked) {

                personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
                personalViewModel.time_filter_text.value = "Daily Selected"
                personalViewModel.pref_filter_text.value = "Artist Selected"

                song_clicked = false
                artist_clicked = false
                album_clicked = false

                daily_clicked = false
                weekly_clicked = false
                monthly_clicked = false
                all_time_clicked = false

                val arr = db.getArtists(HistoryRecord.TIME_DAILY)
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                var total_listens_top_ten = 0
                for (obj in arr) {
                    total_listens_top_ten += obj.listens
                }

                for (obj in arr) {
                    val dynamic_ll = LinearLayout(this.context as Context)

                    var ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    ll_params.setMargins(0, 10, 0, 10)
                    dynamic_ll.layoutParams = ll_params
                    dynamic_ll.orientation = LinearLayout.HORIZONTAL

                    var bar_tv = TextView(this.context as Context)
                    var desc_tv = TextView(this.context as Context)

                    // calculate relative weight of shape based on num listens
                    var weight: Float = 0.0f
                    weight = (obj.listens.toFloat() / total_listens_top_ten.toFloat())
                    var percent_weight: Float = weight * 100

                    var bar_lp = LinearLayout.LayoutParams(0 ,100, weight)
                    bar_lp.setMargins(20, 0 ,0 ,0)
                    bar_tv.layoutParams = bar_lp
                    bar_tv.background = resources.getDrawable(R.drawable.rectangle)
                    bar_tv.gravity = Gravity.CENTER
                    bar_tv.setPadding(0, 5, 0, 5)
                    bar_tv.text = "${String.format("%.1f", percent_weight).toFloat()}%"

                    var dyn_lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f)
                    dyn_lp.setMargins(0, -5, 25, -5)
                    desc_tv.layoutParams = dyn_lp
                    desc_tv.gravity = Gravity.RIGHT
                    desc_tv.includeFontPadding = false
                    desc_tv.firstBaselineToTopHeight = 0
                    desc_tv.setPadding(0,0,0,0)
                    desc_tv.isSingleLine = false
                    desc_tv.text = "${obj.artist}"
                    dynamic_ll.addView(bar_tv)
                    dynamic_ll.addView(desc_tv)
                    wrapper.addView(dynamic_ll)
                }
            } else if (weekly_clicked) {
                personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
                personalViewModel.time_filter_text.value = "Weekly Selected"
                personalViewModel.pref_filter_text.value = "Artist Selected"

                song_clicked = false
                artist_clicked = false
                album_clicked = false

                daily_clicked = false
                weekly_clicked = false
                monthly_clicked = false
                all_time_clicked = false

                val arr = db.getArtists(HistoryRecord.TIME_WEEKLY)
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                var total_listens_top_ten = 0
                for (obj in arr) {
                    total_listens_top_ten += obj.listens
                }

                for (obj in arr) {
                    val dynamic_ll = LinearLayout(this.context as Context)

                    var ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    ll_params.setMargins(0, 10, 0, 10)
                    dynamic_ll.layoutParams = ll_params
                    dynamic_ll.orientation = LinearLayout.HORIZONTAL

                    var bar_tv = TextView(this.context as Context)
                    var desc_tv = TextView(this.context as Context)

                    // calculate relative weight of shape based on num listens
                    var weight: Float = 0.0f
                    weight = (obj.listens.toFloat() / total_listens_top_ten.toFloat())
                    var percent_weight: Float = weight * 100

                    var bar_lp = LinearLayout.LayoutParams(0 ,100, weight)
                    bar_lp.setMargins(20, 0 ,0 ,0)
                    bar_tv.layoutParams = bar_lp
                    bar_tv.background = resources.getDrawable(R.drawable.rectangle)
                    bar_tv.gravity = Gravity.CENTER
                    bar_tv.setPadding(0, 5, 0, 5)
                    bar_tv.text = "${String.format("%.1f", percent_weight).toFloat()}%"

                    var dyn_lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f)
                    dyn_lp.setMargins(0, -5, 25, -5)
                    desc_tv.layoutParams = dyn_lp
                    desc_tv.gravity = Gravity.RIGHT
                    desc_tv.includeFontPadding = false
                    desc_tv.firstBaselineToTopHeight = 0
                    desc_tv.setPadding(0,0,0,0)
                    desc_tv.isSingleLine = false
                    desc_tv.text = "${obj.artist}"
                    dynamic_ll.addView(bar_tv)
                    dynamic_ll.addView(desc_tv)
                    wrapper.addView(dynamic_ll)
                }
            } else if (monthly_clicked) {
                personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
                personalViewModel.time_filter_text.value = "Monthly Selected"
                personalViewModel.pref_filter_text.value = "Artist Selected"

                song_clicked = false
                artist_clicked = false
                album_clicked = false

                daily_clicked = false
                weekly_clicked = false
                monthly_clicked = false
                all_time_clicked = false

                val arr = db.getArtists(HistoryRecord.TIME_MONTHLY)
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                var total_listens_top_ten = 0
                for (obj in arr) {
                    total_listens_top_ten += obj.listens
                }

                for (obj in arr) {
                    val dynamic_ll = LinearLayout(this.context as Context)

                    var ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    ll_params.setMargins(0, 10, 0, 10)
                    dynamic_ll.layoutParams = ll_params
                    dynamic_ll.orientation = LinearLayout.HORIZONTAL

                    var bar_tv = TextView(this.context as Context)
                    var desc_tv = TextView(this.context as Context)

                    // calculate relative weight of shape based on num listens
                    var weight: Float = 0.0f
                    weight = (obj.listens.toFloat() / total_listens_top_ten.toFloat())
                    var percent_weight: Float = weight * 100

                    var bar_lp = LinearLayout.LayoutParams(0 ,100, weight)
                    bar_lp.setMargins(20, 0 ,0 ,0)
                    bar_tv.layoutParams = bar_lp
                    bar_tv.background = resources.getDrawable(R.drawable.rectangle)
                    bar_tv.gravity = Gravity.CENTER
                    bar_tv.setPadding(0, 5, 0, 5)
                    bar_tv.text = "${String.format("%.1f", percent_weight).toFloat()}%"

                    var dyn_lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f)
                    dyn_lp.setMargins(0, -5, 25, -5)
                    desc_tv.layoutParams = dyn_lp
                    desc_tv.gravity = Gravity.RIGHT
                    desc_tv.includeFontPadding = false
                    desc_tv.firstBaselineToTopHeight = 0
                    desc_tv.setPadding(0,0,0,0)
                    desc_tv.isSingleLine = false
                    desc_tv.text = "${obj.artist}"
                    dynamic_ll.addView(bar_tv)
                    dynamic_ll.addView(desc_tv)
                    wrapper.addView(dynamic_ll)
                }
            } else if (all_time_clicked) {
                personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
                personalViewModel.time_filter_text.value = "All Time Selected"
                personalViewModel.pref_filter_text.value = "Artist Selected"

                song_clicked = false
                artist_clicked = false
                album_clicked = false

                daily_clicked = false
                weekly_clicked = false
                monthly_clicked = false
                all_time_clicked = false

                val arr = db.getArtists(HistoryRecord.TIME_ALL)
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                var total_listens_top_ten = 0
                for (obj in arr) {
                    total_listens_top_ten += obj.listens
                }

                for (obj in arr) {
                    val dynamic_ll = LinearLayout(this.context as Context)

                    var ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    ll_params.setMargins(0, 10, 0, 10)
                    dynamic_ll.layoutParams = ll_params
                    dynamic_ll.orientation = LinearLayout.HORIZONTAL

                    var bar_tv = TextView(this.context as Context)
                    var desc_tv = TextView(this.context as Context)

                    // calculate relative weight of shape based on num listens
                    var weight: Float = 0.0f
                    weight = (obj.listens.toFloat() / total_listens_top_ten.toFloat())
                    var percent_weight: Float = weight * 100

                    var bar_lp = LinearLayout.LayoutParams(0 ,100, weight)
                    bar_lp.setMargins(20, 0 ,0 ,0)
                    bar_tv.layoutParams = bar_lp
                    bar_tv.background = resources.getDrawable(R.drawable.rectangle)
                    bar_tv.gravity = Gravity.CENTER
                    bar_tv.setPadding(0, 5, 0, 5)
                    bar_tv.text = "${String.format("%.1f", percent_weight).toFloat()}%"

                    var dyn_lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f)
                    dyn_lp.setMargins(0, -5, 25, -5)
                    desc_tv.layoutParams = dyn_lp
                    desc_tv.gravity = Gravity.RIGHT
                    desc_tv.includeFontPadding = false
                    desc_tv.firstBaselineToTopHeight = 0
                    desc_tv.setPadding(0,0,0,0)
                    desc_tv.isSingleLine = false
                    desc_tv.text = "${obj.artist}"
                    dynamic_ll.addView(bar_tv)
                    dynamic_ll.addView(desc_tv)
                    wrapper.addView(dynamic_ll)
                }
            } else {
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                personalViewModel.time_filter_text.value = "Please select a time filter"
            }
        }

        mAlbumButton.setOnClickListener {
            if (daily_clicked) {

                personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
                personalViewModel.time_filter_text.value = "Daily Selected"
                personalViewModel.pref_filter_text.value = "Album Selected"

                song_clicked = false
                artist_clicked = false
                album_clicked = false

                daily_clicked = false
                weekly_clicked = false
                monthly_clicked = false
                all_time_clicked = false

                val arr = db.getAlbums(HistoryRecord.TIME_DAILY)
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                var total_listens_top_ten = 0
                for (obj in arr) {
                    total_listens_top_ten += obj.listens
                }

                for (obj in arr) {
                    val dynamic_ll = LinearLayout(this.context as Context)

                    var ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    ll_params.setMargins(0, 10, 0, 10)
                    dynamic_ll.layoutParams = ll_params
                    dynamic_ll.orientation = LinearLayout.HORIZONTAL

                    var bar_tv = TextView(this.context as Context)
                    var desc_tv = TextView(this.context as Context)

                    // calculate relative weight of shape based on num listens
                    var weight: Float = 0.0f
                    weight = (obj.listens.toFloat() / total_listens_top_ten.toFloat())
                    var percent_weight: Float = weight * 100

                    var bar_lp = LinearLayout.LayoutParams(0 ,100, weight)
                    bar_lp.setMargins(20, 0 ,0 ,0)
                    bar_tv.layoutParams = bar_lp
                    bar_tv.background = resources.getDrawable(R.drawable.rectangle)
                    bar_tv.gravity = Gravity.CENTER
                    bar_tv.setPadding(0, 5, 0, 5)
                    bar_tv.text = "${String.format("%.1f", percent_weight).toFloat()}%"

                    var dyn_lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f)
                    dyn_lp.setMargins(0, -5, 25, -5)
                    desc_tv.layoutParams = dyn_lp
                    desc_tv.gravity = Gravity.RIGHT
                    desc_tv.includeFontPadding = false
                    desc_tv.firstBaselineToTopHeight = 0
                    desc_tv.setPadding(0,0,0,0)
                    desc_tv.isSingleLine = false
                    desc_tv.text = "${obj.album} By: ${obj.artist}"
                    dynamic_ll.addView(bar_tv)
                    dynamic_ll.addView(desc_tv)
                    wrapper.addView(dynamic_ll)
                }
            } else if (weekly_clicked) {
                personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
                personalViewModel.time_filter_text.value = "Weekly Selected"
                personalViewModel.pref_filter_text.value = "Album Selected"

                song_clicked = false
                artist_clicked = false
                album_clicked = false

                daily_clicked = false
                weekly_clicked = false
                monthly_clicked = false
                all_time_clicked = false

                val arr = db.getAlbums(HistoryRecord.TIME_WEEKLY)
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                var total_listens_top_ten = 0
                for (obj in arr) {
                    total_listens_top_ten += obj.listens
                }

                for (obj in arr) {
                    val dynamic_ll = LinearLayout(this.context as Context)

                    var ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    ll_params.setMargins(0, 10, 0, 10)
                    dynamic_ll.layoutParams = ll_params
                    dynamic_ll.orientation = LinearLayout.HORIZONTAL

                    var bar_tv = TextView(this.context as Context)
                    var desc_tv = TextView(this.context as Context)

                    // calculate relative weight of shape based on num listens
                    var weight: Float = 0.0f
                    weight = (obj.listens.toFloat() / total_listens_top_ten.toFloat())
                    var percent_weight: Float = weight * 100

                    var bar_lp = LinearLayout.LayoutParams(0 ,100, weight)
                    bar_lp.setMargins(20, 0 ,0 ,0)
                    bar_tv.layoutParams = bar_lp
                    bar_tv.background = resources.getDrawable(R.drawable.rectangle)
                    bar_tv.gravity = Gravity.CENTER
                    bar_tv.setPadding(0, 5, 0, 5)
                    bar_tv.text = "${String.format("%.1f", percent_weight).toFloat()}%"

                    var dyn_lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f)
                    dyn_lp.setMargins(0, -5, 25, -5)
                    desc_tv.layoutParams = dyn_lp
                    desc_tv.gravity = Gravity.RIGHT
                    desc_tv.includeFontPadding = false
                    desc_tv.firstBaselineToTopHeight = 0
                    desc_tv.setPadding(0,0,0,0)
                    desc_tv.isSingleLine = false
                    desc_tv.text = "${obj.album} By: ${obj.artist}"
                    dynamic_ll.addView(bar_tv)
                    dynamic_ll.addView(desc_tv)
                    wrapper.addView(dynamic_ll)
                }
            } else if (monthly_clicked) {
                personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
                personalViewModel.time_filter_text.value = "Monthly Selected"
                personalViewModel.pref_filter_text.value = "Album Selected"

                song_clicked = false
                artist_clicked = false
                album_clicked = false

                daily_clicked = false
                weekly_clicked = false
                monthly_clicked = false
                all_time_clicked = false

                val arr = db.getAlbums(HistoryRecord.TIME_MONTHLY)
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                var total_listens_top_ten = 0
                for (obj in arr) {
                    total_listens_top_ten += obj.listens
                }

                for (obj in arr) {
                    val dynamic_ll = LinearLayout(this.context as Context)

                    var ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    ll_params.setMargins(0, 10, 0, 10)
                    dynamic_ll.layoutParams = ll_params
                    dynamic_ll.orientation = LinearLayout.HORIZONTAL

                    var bar_tv = TextView(this.context as Context)
                    var desc_tv = TextView(this.context as Context)

                    // calculate relative weight of shape based on num listens
                    var weight: Float = 0.0f
                    weight = (obj.listens.toFloat() / total_listens_top_ten.toFloat())
                    var percent_weight: Float = weight * 100

                    var bar_lp = LinearLayout.LayoutParams(0 ,100, weight)
                    bar_lp.setMargins(20, 0 ,0 ,0)
                    bar_tv.layoutParams = bar_lp
                    bar_tv.background = resources.getDrawable(R.drawable.rectangle)
                    bar_tv.gravity = Gravity.CENTER
                    bar_tv.setPadding(0, 5, 0, 5)
                    bar_tv.text = "${String.format("%.1f", percent_weight).toFloat()}%"

                    var dyn_lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f)
                    dyn_lp.setMargins(0, -5, 25, -5)
                    desc_tv.layoutParams = dyn_lp
                    desc_tv.gravity = Gravity.RIGHT
                    desc_tv.includeFontPadding = false
                    desc_tv.firstBaselineToTopHeight = 0
                    desc_tv.setPadding(0,0,0,0)
                    desc_tv.isSingleLine = false
                    desc_tv.text = "${obj.album} By: ${obj.artist}"
                    dynamic_ll.addView(bar_tv)
                    dynamic_ll.addView(desc_tv)
                    wrapper.addView(dynamic_ll)
                }
            } else if (all_time_clicked) {
                personalViewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
                personalViewModel.time_filter_text.value = "All Time Selected"
                personalViewModel.pref_filter_text.value = "Album Selected"

                song_clicked = false
                artist_clicked = false
                album_clicked = false

                daily_clicked = false
                weekly_clicked = false
                monthly_clicked = false
                all_time_clicked = false

                val arr = db.getAlbums(HistoryRecord.TIME_ALL)
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                var total_listens_top_ten = 0
                for (obj in arr) {
                    total_listens_top_ten += obj.listens
                }

                for (obj in arr) {
                    val dynamic_ll = LinearLayout(this.context as Context)

                    var ll_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    ll_params.setMargins(0, 10, 0, 10)
                    dynamic_ll.layoutParams = ll_params
                    dynamic_ll.orientation = LinearLayout.HORIZONTAL

                    var bar_tv = TextView(this.context as Context)
                    var desc_tv = TextView(this.context as Context)

                    // calculate relative weight of shape based on num listens
                    var weight: Float = 0.0f
                    weight = (obj.listens.toFloat() / total_listens_top_ten.toFloat())
                    var percent_weight: Float = weight * 100

                    var bar_lp = LinearLayout.LayoutParams(0 ,100, weight)
                    bar_lp.setMargins(20, 0 ,0 ,0)
                    bar_tv.layoutParams = bar_lp
                    bar_tv.background = resources.getDrawable(R.drawable.rectangle)
                    bar_tv.gravity = Gravity.CENTER
                    bar_tv.setPadding(0, 5, 0, 5)
                    bar_tv.text = "${String.format("%.1f", percent_weight).toFloat()}%"

                    var dyn_lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f)
                    dyn_lp.setMargins(0, -5, 25, -5)
                    desc_tv.layoutParams = dyn_lp
                    desc_tv.gravity = Gravity.RIGHT
                    desc_tv.includeFontPadding = false
                    desc_tv.firstBaselineToTopHeight = 0
                    desc_tv.setPadding(0,0,0,0)
                    desc_tv.isSingleLine = false
                    desc_tv.text = "${obj.album} By: ${obj.artist}"
                    dynamic_ll.addView(bar_tv)
                    dynamic_ll.addView(desc_tv)
                    wrapper.addView(dynamic_ll)
                }
            } else {
                val wrapper: LinearLayout = root.findViewById(R.id.listening_wrapper)
                wrapper.removeAllViews()

                personalViewModel.time_filter_text.value = "Please select a time filter"
            }
        }


        mDailyButton.setOnClickListener {
            daily_clicked = true
            weekly_clicked = false
            monthly_clicked = false
            all_time_clicked = false
        }

        mWeeklyButton.setOnClickListener {
            daily_clicked = false
            weekly_clicked = true
            monthly_clicked = false
            all_time_clicked = false
        }
        mMonthlyButton.setOnClickListener {
            daily_clicked = false
            weekly_clicked = false
            monthly_clicked = true
            all_time_clicked = false
        }
        mAllTimeButton.setOnClickListener {
            daily_clicked = false
            weekly_clicked = false
            monthly_clicked = false
            all_time_clicked = true
        }



        // update the time text if needed
        val time_tv: TextView = root.findViewById(R.id.time_text_personal)
        val pref_tv: TextView = root.findViewById(R.id.pref_text_personal)

        personalViewModel.time_filter_text.observe(this, Observer {
            time_tv.text = it
        })

        personalViewModel.pref_filter_text.observe(this, Observer {
            pref_tv.text = it
        })



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