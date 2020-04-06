package com.example.playback

class SpotifyPersonalData{
    var data: HashMap<String,String> = hashMapOf("recordId" to "", "userId" to "",
        "artistName" to "", "popularityScore" to "","songName" to "","albumName" to "","songGenre" to "",
        "listenLocationLatitude" to "",  "listenLocationLongitude" to "" )


    constructor(incomingData: Map<String,String>){
        incomingData.map { (key, value) ->
            if(this.data.containsKey(key)) {
                this.data.put(key,value)
            }
        }
    }
}

