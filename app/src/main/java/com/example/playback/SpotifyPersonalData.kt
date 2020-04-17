package com.example.playback

class SpotifyPersonalData{

    var recordId: Int = 0
    var userId: Int = 0
    var artistName: String = ""
    var popularityScore: Int = 0
    var songName: String = ""
    var albumName: String = ""
    var songGenre: String = ""


    var listenLocationLatitude = 0.00
    var listenLocationLongitude = 0.0

    constructor(recordId: Int, userId: Int, artistName: String, popularityScore: Int, songName: String, albumName: String, songGenre: String, listenLocationLatitude: Double, listenLocationLongitude: Double){

        this.recordId = recordId
        this.userId = userId
        this.artistName = artistName
        this.popularityScore = popularityScore
        this.songName = songName
        this.albumName = albumName
        this.songGenre = songGenre

        // these values are for pinpointing the location of a listened to song (where it was listened to most recently)
        this.listenLocationLatitude = listenLocationLatitude
        this.listenLocationLongitude = listenLocationLongitude
    }

}

