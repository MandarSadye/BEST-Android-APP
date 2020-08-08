package com.mandarsadye.mandar.ess_user.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.Fragments.Chat.ChatHistory
import com.mandarsadye.mandar.ess_user.Holders.ChatViewHolder
import com.mandarsadye.mandar.ess_user.Model.Chats
import com.mandarsadye.mandar.ess_user.R

/**
 * Created by mandar on 28-03-2018.
 */
class ChatAdapter(chatInp : ArrayList<Chats>? , context : StartActivity , mFrag : ChatHistory) : RecyclerView.Adapter<ChatViewHolder>() {

    var chat : ArrayList<Chats>? = chatInp
    var context : StartActivity = context
    var mFrag = mFrag

    fun swapData(data : ArrayList<Chats>? ){
        chat = ArrayList()
        chat = data
        notifyDataSetChanged()
        mFrag.handler.sendEmptyMessage(3)
    }



    override fun onBindViewHolder(holder:    ChatViewHolder?, position: Int) {
        var chat : Chats = chat!![position]
        holder!!.updateUI(chat,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ChatViewHolder {
        var cardChat : View = LayoutInflater.from(parent!!.context).inflate(R.layout.card_chat,parent,false)
        return ChatViewHolder(cardChat,context)
    }

    override fun getItemCount(): Int {
        return chat!!.size
    }
}