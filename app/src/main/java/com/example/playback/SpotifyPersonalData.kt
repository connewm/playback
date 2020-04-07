package com.example.playback

class SpotifyPersonalData{
    var recordId: Int = 0
    var userId: Int = 0
    var artistName: String? = null
    var popularityScore: Int = 0
    var songName: String? = null
    var albumName: String? = null
    var songGenre: String? = null

<<<<<<< HEAD
    constructor(recordId: Int, userId: Int, artistName: String, popularityScore: Int, songName: String, albumName: String, songGenre: String){
=======
    var listenLocationLatitude = 0.0
    var listenLocationLongitude = 0.0

    constructor(recordId: Int, userId: Int, artistName: String, popularityScore: Int, songName: String, albumName: String, songGenre: String, listenLocationLatitude: Double, listenLocationLongitude: Double){
>>>>>>> 51d5a2b16e8c9d55e490dd94ae86d348a0758f3c
        this.recordId = recordId
        this.userId = userId
        this.artistName = artistName
        this.popularityScore = popularityScore
        this.songName = songName
        this.albumName = albumName
        this.songGenre = songGenre
<<<<<<< HEAD
=======

        // these values are for pinpointing the location of a listened to song
        this.listenLocationLatitude = listenLocationLatitude
        this.listenLocationLongitude = listenLocationLongitude
>>>>>>> 51d5a2b16e8c9d55e490dd94ae86d348a0758f3c
    }
}

