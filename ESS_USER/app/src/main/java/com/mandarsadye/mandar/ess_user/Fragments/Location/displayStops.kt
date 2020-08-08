package com.mandarsadye.mandar.ess_user.Fragments.Location


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.Adapter.ChatAdapter
import com.mandarsadye.mandar.ess_user.Adapter.DisplayAdapter
import com.mandarsadye.mandar.ess_user.DataServices.BusNumberData
import com.mandarsadye.mandar.ess_user.DataServices.ChatData
import com.mandarsadye.mandar.ess_user.DataServices.DisplayData
import com.mandarsadye.mandar.ess_user.Model.BusNumbers
import com.mandarsadye.mandar.ess_user.Model.DisplayMatrix

import com.mandarsadye.mandar.ess_user.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 *
 */
class displayStops : Fragment() {
    var viewi : View? = null
    var mActivity : StartActivity? = null
    var mContext : Context? = null
    var adapter : DisplayAdapter? = null
    var reference : DatabaseReference? = null
    var recyclerview : RecyclerView? =null
    var layoutManager : LinearLayoutManager? = null
    var bus : BusNumbers? = null

    companion object {
        @JvmStatic
        fun getInstance(bus : BusNumbers?) : displayStops {
            val a = displayStops()
            a.bus = bus
            return a
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as StartActivity
        mContext = activity
        reference = mActivity!!.dbRef!!.child("BESTS")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewi = inflater.inflate(R.layout.fragment_display_stops, container, false)
        DisplayData.ourInstance.list = ArrayList()
        val a = bus!!.Distance_Time_Matrix.split(" ")
        Log.e("Distance_Time_Matrix" , a.size.toString())
        val b = bus!!.Halts.split(" ").size
        Log.e("number of Hals" , b.toString())
        var i = 0
        var status = true
        while (i<b){
//            val returned = getNextBus(a[6*i+2] + " " + a[6*i+3] + " " + a[6*i+4] + " " + a[6*i+5])
//            Log.e("returned_instance : " , returned.toString())
            fun Boolean.toInt() = if (this) 1 else 0
            var str1 = a[6*i+2] + " " + a[6*i+3] + " " + a[6*i+4] + " " + a[6*i+5]
            if (status){
                if(bus!!.latitude == bus!!.Halt_Lat.split(" ")[i])
                    status = false
                DisplayData.ourInstance.list.add(DisplayMatrix(a[6*i].replace("_"," "),a[6*i+1],str1,str1,"0"))
                i++
                continue
            }
            var str2 = (a[6*i+2].toFloat() + bus!!.Delay.toFloat()/100).toString() + " " + (a[6*i+3].toFloat() + bus!!.Delay.toFloat()/100).toString() + " " + (a[6*i+4].toFloat() + bus!!.Delay.toFloat()/100).toString() + " " + (a[6*i+5].toFloat() + bus!!.Delay.toFloat()/100).toString()
            DisplayData.ourInstance.list.add(DisplayMatrix(a[6*i].replace("_"," "),a[6*i+1],str1,str2.toString(),bus!!.Delay.toString()))
            i++
        }
        Log.e("DelayBus",bus!!.Delay.toString())

        recyclerview = viewi!!.findViewById(R.id.recyclerStops)
        recyclerview!!.setHasFixedSize(true)

        adapter  = DisplayAdapter(DisplayData.ourInstance.getChat(),mActivity!!)
        recyclerview!!.adapter = adapter

        layoutManager = object : LinearLayoutManager(context){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        layoutManager!!.orientation = LinearLayoutManager.VERTICAL
        recyclerview!!.layoutManager = layoutManager
        monitorViews()

        mActivity!!.updateDisplayLocationOnBusLocation(LatLng(bus!!.latitude.trim().toDouble(),bus!!.longitude.trim().toDouble()))
        return viewi!!
    }



    fun monitorViews(){
        reference!!.child(bus!!.Number).child("Delay").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                for (i in BusNumberData.ourInstance.list){
                    if(i.Number == bus!!.Number) {
                        i.Delay = p0!!.getValue(Int::class.java)!!.toInt()
                        bus = i
                        break
                    }
                }
                handle.sendEmptyMessage(1)
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
        reference!!.child(bus!!.Number).child("latitude").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                for (i in BusNumberData.ourInstance.list){
                    if(i.Number == bus!!.Number) {
                        i.latitude = p0!!.getValue(String::class.java)!!.toString()
                        bus = i
                        break
                    }
                }
                handle.sendEmptyMessage(1)
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
        reference!!.child(bus!!.Number).child("longitude").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                for (i in BusNumberData.ourInstance.list){
                    if(i.Number == bus!!.Number) {
                        i.longitude = p0!!.getValue(String::class.java)!!.toString()
                        bus = i
                        break
                    }
                }
                handle.sendEmptyMessage(1)
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })

    }

    fun RefreshList(){
        DisplayData.ourInstance.list = ArrayList()
        val a = bus!!.Distance_Time_Matrix.split(" ")
        Log.e("Distance_Time_Matrix" , a.size.toString())
        val b = bus!!.Halts.split(" ").size
        Log.e("number of Hals" , b.toString())
        var i = 0
        var status = true
        while (i<b){
            var str1 = a[6*i+2] + " " + a[6*i+3] + " " + a[6*i+4] + " " + a[6*i+5]
            if (status){
                if(bus!!.latitude == bus!!.Halt_Lat.split(" ")[i])
                    status = false
                DisplayData.ourInstance.list.add(DisplayMatrix(a[6*i].replace("_"," "),a[6*i+1],str1,str1,"0"))
                i++
                continue
            }
            var str2 = (a[6*i+2].toFloat() + bus!!.Delay.toFloat()/100).toString() + " " + (a[6*i+3].toFloat() + bus!!.Delay.toFloat()/100).toString() + " " + (a[6*i+4].toFloat() + bus!!.Delay.toFloat()/100).toString() + " " + (a[6*i+5].toFloat() + bus!!.Delay.toFloat()/100).toString()
            DisplayData.ourInstance.list.add(DisplayMatrix(a[6*i].replace("_"," "),a[6*i+1],str1,str2.toString(),bus!!.Delay.toString()))
            i++
        }
        adapter!!.swapData(DisplayData.ourInstance.list)
    }

    var handle = Handler{

        when(it.what){
            1-> {
                RefreshList()
            }
        }
        return@Handler true
    }


}
