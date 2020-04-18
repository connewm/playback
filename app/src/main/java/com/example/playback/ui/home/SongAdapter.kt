package com.example.playback.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playback.R
import com.example.playback.SpotifyConnector
import com.example.playback.SpotifyPersonalData
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.main_page_text_view.view.*

class SongAdapter (private val myDataset: List<SpotifyPersonalData>) :
    RecyclerView.Adapter<SongAdapter.MyViewHolder>() {

    val TAG = "SONG ADAPTER "
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.songNameTextView
        val image = itemView.albumArt
        //val listens = itemView.amountListenTextView
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): SongAdapter.MyViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_page_text_view, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        var song = myDataset[position]
        var sc = SpotifyConnector()

        Log.d(TAG, "GET ALBUM ART FOR SONG" +song.songName)
        var b = null //sc.getAlbumArt(song.imageUri!!)


        if (b != null){
            holder.itemView.albumArt.setImageBitmap(b)
            Log.d(TAG, "GOT album art for song " + song.songName)
        }else{
            Log.d(TAG, "NO ALBUM ART FOR SONG " +song.songName)
            Log.d(TAG, "NO ALBUM ART FOR SONG " +song.imageUri)
        }
        holder.itemView.songNameTextView.text = song.songName + " By: " + song.artistName

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}
