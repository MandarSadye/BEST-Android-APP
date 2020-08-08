package com.mandarsadye.mandar.ess_user.Adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.Holders.SearchViewHolder
import com.mandarsadye.mandar.ess_user.Model.BusNumbers
import com.mandarsadye.mandar.ess_user.R

/**
 * Created by mandar on 24-03-2018.
 */
class SearchAdapter(searchInp : ArrayList<BusNumbers> , context: Context , activity: StartActivity) : RecyclerView.Adapter<SearchViewHolder>() {

    var searches : ArrayList<BusNumbers>? = searchInp
    var context : Context = context
    var activity = activity

    fun swapData(data : ArrayList<BusNumbers>? ){
        searches = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SearchViewHolder?, position: Int) {
        var bus : BusNumbers = searches!!.get(position)
        holder!!.updateUI(bus)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchViewHolder {
        var cardBuses : View = LayoutInflater.from(parent!!.context).inflate(R.layout.card_bus_numbers,parent,false)
        return SearchViewHolder(cardBuses,activity)
    }

    override fun getItemCount(): Int {
        return searches!!.size
    }
}