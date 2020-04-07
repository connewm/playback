package com.example.playback.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.playback.R


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
//import com.anychart.sample.R;

import java.util.ArrayList;
import java.util.List;

class MapFragment : Fragment() {

    private lateinit var mapViewModel: MapViewModel
    private  val TAG = "MapFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.v(TAG,"called onCreateView")
        val root = inflater.inflate(R.layout.fragment_maps, container, false)

        /*mapViewModel =
            ViewModelProviders.of(this).get(MapViewModel::class.java)
        val textView: TextView = root.findViewById(R.id.map_page)
        mapViewModel.text.observe(this, Observer {
            textView.text = it
        })
        */

        var pie : Pie = AnyChart.pie()
        var data = ArrayList<DataEntry>()

        data.add(  ValueDataEntry("John", 10000))
        data.add(ValueDataEntry("Jake", 12000))
        data.add( ValueDataEntry("Peter", 18000))

        pie.data(data);

       var anyChartView:  AnyChartView  = root.findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie)




        return root
    }
}

