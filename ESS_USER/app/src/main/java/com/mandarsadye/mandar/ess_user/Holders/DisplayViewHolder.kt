package com.mandarsadye.mandar.ess_user.Holders

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.CustomClasses.OverAllStaticConstants
import com.mandarsadye.mandar.ess_user.DataServices.ChatData
import com.mandarsadye.mandar.ess_user.Model.Chats
import com.mandarsadye.mandar.ess_user.Model.DisplayMatrix
import com.mandarsadye.mandar.ess_user.R
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.*

class DisplayViewHolder (itemView : View?, context: StartActivity) : RecyclerView.ViewHolder(itemView)  {
    val mContext = context
    var displayStop : TextView? = null
    var displayStartDistance : TextView? = null
    var displayScheduledTime : TextView? = null
    var displayExpectedTime : TextView? = null
    var displayDelay : TextView? = null

    var isClicked = false

    init {
        displayStop = itemView!!.findViewById(R.id.displayStop)
        displayStartDistance = itemView!!.findViewById(R.id.displayStartDistance)
        displayScheduledTime = itemView!!.findViewById(R.id.displayScheduledTime)
        displayExpectedTime = itemView!!.findViewById(R.id.displayExpectedTime)
        displayDelay = itemView!!.findViewById(R.id.displayDelay)
        itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (isClicked ) {
                    v!!.findViewById<LinearLayout>(R.id.ShrinkLayout).layoutParams.height = 0
                    v!!.findViewById<TextView>(R.id.alaisForStopName).layoutParams.height = 0
                    displayStop!!.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
                }
                else {
                    v!!.findViewById<LinearLayout>(R.id.ShrinkLayout).layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                    v!!.findViewById<TextView>(R.id.alaisForStopName).layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                    displayStop!!.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                }
                v!!.findViewById<LinearLayout>(R.id.ShrinkLayout).requestLayout()
                isClicked = !isClicked
            }
        })
    }

    fun updateUI(message : DisplayMatrix,position: Int){
        var k = getNextBus(message.timeAct)
        var j = 0
        for (i in k){
            if (i != 0)break
            j++
        }
        Log.e("Feedback_ViewHolder",j.toString())
        displayStop!!.text = (position+1).toString() + ". " + message.stopName
        displayStartDistance!!.text = message.distance
        displayScheduledTime!!.text = message.timeAct.split(" ")[j]
        displayExpectedTime!!.text = message.timeExp.split(" ")[j]
        displayDelay!!.text = message.delay
    }

    fun getNextBus(str : String):ArrayList<Int>{
        fun Boolean.toInt() = if (this) 1 else 0
        val p = str.trim().split(" ")
        val cal = Calendar.getInstance()
        Log.e("MyDat" , cal.getTime().toString())
        val sd1 = SimpleDateFormat("HH")
        val sd2 = SimpleDateFormat("mm")
        val formattedDate = sd1.format(cal.getTime()) + "." + sd2.format(cal.getTime())
        val k = formattedDate.toFloat()
        Log.e("MyDat" ,k.toString())
        Log.e("MyDat" ,"------------------********--------------------")
        var pminus = 0f
        var stat = 0
        var arr = ArrayList<Float>()
        for (i in p){
            var b = i.toFloat()
            if (b < pminus)stat = 12
            b += stat
            arr.add(b)
            pminus = b
        }
        var arrRet = ArrayList<Int>()
        var i = 0
        while (i < 4){
            arrRet.add(0)
            i++
        }
        var m = 0
        if (k<arr[0]){

            m =  3*5*7
        }
        else if (k<arr[1]){
            m = 2*5*7
        }
        else if (k<arr[2]){
            m =  3*2*7
        }
        else if (k<arr[3]){
            m = 3*5*2
        }
        Log.e("MyDat" ,"------------------********--------------------")

        arrRet[0] = (m%2 != 0).toInt()
        arrRet[1] = (m%3 != 0).toInt()
        arrRet[2] = (m%5 != 0).toInt()
        arrRet[3] = (m%7 != 0).toInt()
        Log.e("MyDat" ,m.toString())
        Log.e("MyDat" ,arr.toString())

        return arrRet
    }

}