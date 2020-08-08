package com.mandarsadye.mandar.ess_user.CustomClasses

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.DataServices.ChatData
import com.mandarsadye.mandar.ess_user.R


class CustomScrollListener(context: StartActivity,username : String) : RecyclerView.OnScrollListener() {
    var context = context
    var username = username

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            when (newState) {
//                RecyclerView.SCROLL_STATE_IDLE -> println("The RecyclerView is not scrolling")
//                RecyclerView.SCROLL_STATE_DRAGGING -> println("Scrolling now")
//                RecyclerView.SCROLL_STATE_SETTLING -> println("Scroll Settling")
            }

        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            val p : LinearLayoutManager = recyclerView!!.layoutManager as LinearLayoutManager
            val q = p.findFirstVisibleItemPosition()
            val r = ChatData.ourInstance.list[q]
            if (username == r.Sender.trim()){
                context.mSuppActionBar!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context,R.color.mychat)))
            }
            else {
                context.mSuppActionBar!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context,R.color.otherchat)))
            }

        }
}