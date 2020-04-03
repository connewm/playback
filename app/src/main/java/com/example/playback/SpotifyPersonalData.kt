package com.example.playback

class SpotifyPersonalData{
    var recordId: Int = 0
    var userId: Int = 0
    var artistName: String? = null
    var popularityScore: Int = 0

    constructor(recordId: Int, userId: Int, artistName: String, popularityScore: Int){
        this.recordId = recordId
        this.userId = userId
        this.artistName = artistName
        this.popularityScore = popularityScore
    }
}

