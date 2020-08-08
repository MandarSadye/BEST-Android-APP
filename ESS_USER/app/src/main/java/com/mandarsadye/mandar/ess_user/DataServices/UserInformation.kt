package com.mandarsadye.mandar.ess_user.DataServices

/**
 * Created by mandar on 20-03-2018.
 */
class UserInformation{
    var nickName : String = ""
    var timeOfStart : String = ""
    var timeLastSeen : String = ""
    var Favorites : String = ""
    var currentLatitude : Float = 0f
    var currentLongitude : Float = 0f


    constructor(name : String , timeStart : String) {
        this.nickName = name
        this.timeOfStart = timeStart
        this.timeLastSeen = timeStart
        this.Favorites = ""
        this.currentLatitude = 0f
        this.currentLongitude = 0f
    }

    constructor(){}
}