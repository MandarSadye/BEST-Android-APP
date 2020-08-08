package com.mandarsadye.mandar.ess_user.Holders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.Model.BusNumbers
import com.mandarsadye.mandar.ess_user.R
import kotlinx.android.synthetic.main.card_bus_numbers.view.*

/**
 * Created by mandar on 24-03-2018.
 */

class SearchViewHolder(itemView : View?,context: StartActivity) : RecyclerView.ViewHolder(itemView) {
    var Title : TextView? = null
    var FROM : TextView? = null
    var TO : TextView? = null
    var button : Button? = null
    var context = context

    var isClicked = false
    init {
        Title = itemView!!.findViewById(R.id.cardSearchText)
        FROM = itemView!!.findViewById(R.id.CardSearchFrom)
        TO = itemView!!.findViewById(R.id.CardSearchTo)
        itemView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if (isClicked ) {
                    v!!.findViewById<LinearLayout>(R.id.SearchLinearLayout).layoutParams.height = 0
                    Title!!.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
                }
                else {
                    v!!.findViewById<LinearLayout>(R.id.SearchLinearLayout).layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                    Title!!.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                }
                v!!.findViewById<LinearLayout>(R.id.SearchLinearLayout).requestLayout()
                isClicked = !isClicked

            }
        })
        button = itemView!!.findViewById(R.id.DetailsButton)

    }

    fun updateUI(bus : BusNumbers){
        Title!!.text = bus.Number!!.replace("_"," ")
        FROM!!.text = bus.FROM!!.replace("_"," ")
        TO!!.text = bus.TO!!.replace("_"," ")
        button!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                context.showDetailsInFragment(bus)
            }
        })
    }
}