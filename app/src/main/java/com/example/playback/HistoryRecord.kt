package com.example.playback

// data class that specifies aggregated return values to the filter functionality
class HistoryRecord {

    /**
     * we will be reusing this object for all listening patterns:
     * e.g. if you want to filter by artist listens, the listens member will be filled in with
     * artist listens for the given time period
     *
     * the filterBy member would then be set to "Artist" letting the user know which filter is being used
     * the filterBy member is set depending on which method is called, e.g. in this case getDailyArtist
     *
     * the timeframe member also specifies the timeframe being used to filter, e.g. in this case timeframe = "Daily"
     */
    var songName: String = ""
    var artist: String = ""
    var album: String = ""
    var listens: Int = 0
    var filterBy: String = ""

    constructor(songName: String, artist: String, album: String, listens: Int, filterBy: String)
    {
        this.songName = songName
        this.artist = artist
        this.album = album
        this.listens = listens
        this.filterBy = filterBy
    }

    companion object {
        val TIME_DAILY = 1
        val TIME_WEEKLY = 2
        val TIME_MONTHLY = 3
        val TIME_ALL = 4
    }
}