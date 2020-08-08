package com.mandarsadye.mandar.ess_user.DataServices

import com.mandarsadye.mandar.ess_user.Model.Chats

/**
 * Created by mandar on 28-03-2018.
 */
class ChatData {

    var list : ArrayList<Chats> = ArrayList()



    companion object {
        @JvmStatic
        var ourInstance : ChatData = ChatData()
    }

    fun getChat() : ArrayList<Chats>{
        //pretend data for internet
        return list
    }

    fun addChat(addant : Chats) {
        list.add(addant)
    }

}