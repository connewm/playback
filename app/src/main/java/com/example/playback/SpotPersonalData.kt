package com.example.playback

import java.util.ArrayList
// Data file for Spotify returned JSON object

// eventually want to add associated genre (val ass_genre: Array<String>)
data class SpotPersonalData (val record_id: Int, val user_id: Int, val artist_name: String, val popularity_score: Int)

