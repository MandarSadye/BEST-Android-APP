package com.mandarsadye.mandar.ess_user.Model

import com.mandarsadye.mandar.ess_user.Fragments.Location.displayStops

class DisplayMatrix(name : String,dist:String,ta:String,te:String,delay:String) {
    var stopName : String = name
    var distance : String = dist
    var timeAct : String = ta
    var timeExp : String = te
    var delay : String = delay
}