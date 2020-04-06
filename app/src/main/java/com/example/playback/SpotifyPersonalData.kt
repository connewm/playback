package com.example.playback

class SpotifyPersonalData{
    var data: HashMap<String,String> = hashMapOf("recordId" to "", "userId" to "",
        "artistName" to "", "popularityScore" to "","songName" to "","albumName" to "","songGenre" to "")
    /*
    var recordId: Int = 0
    var userId: Int = 0
    var artistName: String? = null
    var popularityScore: Int = 0
    var songName: String? = null
    var albumName: String? = null
    var songGenre: String? = null
    constructor(recordId: Int, userId: Int, artistName: String, popularityScore: Int, songName: String, albumName: String, songGenre: String){
        val string
        this.recordId = recordId
        this.userId = userId
        this.artistName = artistName
        this.popularityScore = popularityScore
        this.songName = songName
        this.albumName = albumName
        this.songGenre = songGenre
    }*/

    constructor(incomingData: Map<String,String>){
        incomingData.map { (key, value) ->
            if(this.data.containsKey(key)) {
                this.data.put(key,value)
            }
        }
    }
}

