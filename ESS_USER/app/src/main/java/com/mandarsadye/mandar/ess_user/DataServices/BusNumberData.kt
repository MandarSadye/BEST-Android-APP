package com.mandarsadye.mandar.ess_user.DataServices

import com.mandarsadye.mandar.ess_user.Model.BusNumbers

/**
 * Created by mandar on 24-03-2018.
 */
class BusNumberData {
    var list : ArrayList<BusNumbers> = ArrayList()

    companion object {
        @JvmStatic
        var ourInstance : BusNumberData = BusNumberData()
    }


    fun getBusNumbers() : ArrayList<BusNumbers>{
        //pretend data for internet
        return list
    }

    fun addBusNumber(addant : BusNumbers) {
        list.add(addant)
    }

}