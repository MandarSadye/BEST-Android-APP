package com.mandarsadye.mandar.ess_user.DataServices

import com.mandarsadye.mandar.ess_user.Model.Chats
import com.mandarsadye.mandar.ess_user.Model.DisplayMatrix

class DisplayData {

    var list : ArrayList<DisplayMatrix> = ArrayList()



    companion object {
        @JvmStatic
        var ourInstance : DisplayData = DisplayData()
    }

    fun getChat() : ArrayList<DisplayMatrix>{
        //pretend data for internet
        return list
    }

    fun addChat(addant : DisplayMatrix) {
        list.add(addant)
    }

}