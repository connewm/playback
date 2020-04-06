package com.example.playback

class SpotifyPersonalData{

    var recordId: Int = 0
    var userId: Int = 0
    var artistName: String? = null
    var popularityScore: Int = 0
    var songName: String? = null
    var albumName: String? = null
    var songGenre: String? = null
    constructor(recordId: Int, userId: Int, artistName: String, popularityScore: Int, songName: String, albumName: String, songGenre: String){

        this.recordId = recordId
        this.userId = userId
        this.artistName = artistName
        this.popularityScore = popularityScore
        this.songName = songName
        this.albumName = albumName
        this.songGenre = songGenre
    }

}

