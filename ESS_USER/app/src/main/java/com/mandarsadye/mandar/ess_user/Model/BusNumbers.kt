package com.mandarsadye.mandar.ess_user.Model

/**
 * Created by mandar on 24-03-2018.
 */
class BusNumbers {
    var Number : String? = "No number available"
    var FROM : String? = "No start point available"
    var TO : String? = "No end point available"
    var BusCapacity : Float = 0f
    var BusLevel : Int = 1
    var CurrentPassangers : Int = 0
    var CurrentRevenue : Int = 0
    var Delay : Int = 0
    var Halts : String = ""
    var latitude : String = ""
    var longitude : String = ""
    var Distance_Time_Matrix : String = ""
    var Halt_Lat : String = ""
    var Halt_Long : String = ""
    var data_reference : String = ""

    constructor(){}

    constructor(number : String){
        Number = number
    }
    constructor(FROM:String,TO:String){
        this.FROM = FROM
        this.TO = TO
    }
    constructor(number : String,FROM:String,TO:String,BusCapacity:Float,BusLevel:Int,CurrentPassangers : Int,CurrentRevenue : Int,
                Delay : Int = 0,Halts : String = "",latitude : String = "",longitude : String = "", Distance_Time_Matrix :String = ""
                , Halt_Lat :String = "", Halt_Long :String = ""){
        this.FROM = FROM
        this.TO = TO
        this.Number = number
        this.BusCapacity = BusCapacity
        this.BusLevel = BusLevel
        this.CurrentPassangers = CurrentPassangers
        this.CurrentRevenue = CurrentRevenue
        this.Delay = Delay
        this.Halts = Halts
        this.latitude = latitude
        this.longitude = longitude
        this.Distance_Time_Matrix = Distance_Time_Matrix
        this.Halt_Lat = Halt_Lat
        this.Halt_Long = Halt_Long
        }
}