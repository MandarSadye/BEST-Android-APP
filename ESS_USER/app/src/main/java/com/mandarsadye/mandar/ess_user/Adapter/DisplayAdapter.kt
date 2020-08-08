package com.mandarsadye.mandar.ess_user.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.Fragments.Chat.ChatHistory
import com.mandarsadye.mandar.ess_user.Holders.ChatViewHolder
import com.mandarsadye.mandar.ess_user.Holders.DisplayViewHolder
import com.mandarsadye.mandar.ess_user.Model.BusNumbers
import com.mandarsadye.mandar.ess_user.Model.Chats
import com.mandarsadye.mandar.ess_user.Model.DisplayMatrix
import com.mandarsadye.mandar.ess_user.R

class DisplayAdapter(dispayData : ArrayList<DisplayMatrix>? , context : StartActivity) : RecyclerView.Adapter<DisplayViewHolder>() {

    var dispayData : ArrayList<DisplayMatrix>? = dispayData
    var context : StartActivity = context

    fun swapData(data : ArrayList<DisplayMatrix>? ){
        dispayData = data
        notifyDataSetChanged()
    }



    override fun onBindViewHolder(holder:    DisplayViewHolder?, position: Int) {
        var stop : DisplayMatrix = dispayData!![position]
        holder!!.updateUI(stop,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DisplayViewHolder {
        val DisplayMatrixCard : View = LayoutInflater.from(parent!!.context).inflate(R.layout.card_stops,parent,false)
        return DisplayViewHolder(DisplayMatrixCard,context)
    }

    override fun getItemCount(): Int {
        return dispayData!!.size
    }
}