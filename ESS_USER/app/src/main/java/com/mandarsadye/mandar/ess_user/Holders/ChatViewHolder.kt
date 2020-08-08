package com.mandarsadye.mandar.ess_user.Holders

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.mandarsadye.mandar.ess_user.CustomClasses.OverAllStaticConstants
import com.mandarsadye.mandar.ess_user.Model.BusNumbers
import com.mandarsadye.mandar.ess_user.Model.Chats
import com.mandarsadye.mandar.ess_user.R
import android.util.DisplayMetrics
import android.widget.*
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import android.R.attr.gravity
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import com.mandarsadye.mandar.ess_user.DataServices.ChatData
import java.text.FieldPosition


/**
 * Created by mandar on 28-03-2018.
 */
class ChatViewHolder(itemView : View?,context: StartActivity) : RecyclerView.ViewHolder(itemView)  {
    val mContext = context
    var Sender : TextView? = null
    var Message : TextView? = null
    var Time : TextView? = null
    var Card_View : LinearLayout? = null
    var SuperLL : LinearLayout? = null

    init {
        Sender = itemView!!.findViewById(R.id.sender_name)
        Message = itemView!!.findViewById(R.id.sender_text)
        Time = itemView!!.findViewById(R.id.sender_time)
        Card_View = itemView!!.findViewById(R.id.LinearCard)
        SuperLL = itemView!!.findViewById(R.id.SuperLinearLayout)
    }

    fun updateUI(message : Chats,position: Int){
        Sender!!.text = "#" + message.Sender
        Message!!.text = message.Message
        Time!!.text = "@" + message.Time

        var a = Sender!!.text
        if (position>0) {
            if (ChatData.ourInstance.list[position - 1].Sender.trim() == message.Sender.trim())
                Sender!!.visibility = View.GONE
            else
                Sender!!.visibility = View.VISIBLE
        }
        else
            Sender!!.visibility = View.VISIBLE


        val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        params.weight = 1.0f
        if (message.Sender.trim() == OverAllStaticConstants.userInformation!!.nickName.trim()) {
            params.gravity = Gravity.RIGHT
            params.marginStart = 100
            Card_View!!.gravity = Gravity.LEFT
            Card_View!!.background = ContextCompat.getDrawable(mContext, R.drawable.ic_gradient_chat_my_background)
        }
        else {
            params.gravity = Gravity.LEFT
            params.marginEnd = 100
            Card_View!!.gravity = Gravity.RIGHT
            Card_View!!.background = ContextCompat.getDrawable(mContext, R.drawable.ic_gradient_chat_other_background)
        }
        Card_View!!.setLayoutParams(params)
        val scale = mContext.getResources().getDisplayMetrics().density
        val dpAsPixels0 = (4 * scale + 0.5f).toInt()
        val dpAsPixels1 = (0 * scale + 0.5f).toInt()
        if (position - 1 < 0) {
            SuperLL!!.setPadding(dpAsPixels0, dpAsPixels1, dpAsPixels0, dpAsPixels1)
        }
        else if (ChatData.ourInstance.list[position-1].Sender.trim() == message.Sender.trim()) {
//            Sender!!.height = 0
//            Sender!!.visibility = View.GONE
            SuperLL!!.setPadding(dpAsPixels0, dpAsPixels1, dpAsPixels0, dpAsPixels1)
        }
        else {
            SuperLL!!.setPadding(dpAsPixels0, dpAsPixels0, dpAsPixels0, dpAsPixels1)
        }
    }

}