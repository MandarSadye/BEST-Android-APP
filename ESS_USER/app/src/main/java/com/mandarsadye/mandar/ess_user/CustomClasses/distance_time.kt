package com.mandarsadye.mandar.ess_user.CustomClasses

import android.os.Parcel
import android.os.Parcelable

internal class distance_time:Parcelable {
    var distance = 0
    var time = 0
    constructor(distance:Int, time:Int) {
        this.distance = distance
        this.time = time
    }
    protected constructor(`in`:Parcel) {
        distance = `in`.readInt()
        time = `in`.readInt()
    }
    override fun describeContents():Int {
        return 0
    }
    override fun writeToParcel(dest:Parcel, flags:Int) {
        dest.writeInt(distance)
        dest.writeInt(time)
    }
    companion object {
        val CREATOR: Parcelable.Creator<distance_time> = object:Parcelable.Creator<distance_time> {
            override fun createFromParcel(`in`: Parcel):distance_time {
                return distance_time(`in`)
            }
            override fun newArray(size:Int):Array<distance_time?> {
                return arrayOfNulls(size)
            }
        }
    }
}