package com.mandarsadye.mandar.ess_user.Fragments.SearchPackage


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.DataServices.BusNumberData
import com.mandarsadye.mandar.ess_user.Model.BusNumbers

import com.mandarsadye.mandar.ess_user.R

import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.*
import com.mandarsadye.mandar.ess_user.CustomClasses.LoadingDialogClass
import com.mandarsadye.mandar.ess_user.CustomClasses.OverAllStaticConstants
import com.mandarsadye.mandar.ess_user.DataServices.UserInformation


/**
 * A simple [Fragment] subclass.
 */
class Loading : Fragment() {

    var activityj : StartActivity? = null
    var reference : DatabaseReference? = null
    var isDone : BooVariable = BooVariable()
    var vsuper : String? = null
    var value : List<String>? = null
    var Viewi : View? = null
    var newDialog : LoadingDialogClass? = null
    var thisFragment : Loading? = null

    var handler = Handler(){
        when (it.what.toInt()){
            1 -> {
                downloadMessages()
            }
            2 -> {
                val data = activityj!!.mApplicationContext!!.getSharedPreferences("DataSavedCopy", 0)
                val editor = data.edit()
                editor.putInt("isData",1)
                editor.putString("BusNumbers",vsuper)
                for (v in BusNumberData.ourInstance.list){
                    editor.putString(v.Number + "FROM",v.FROM)
                    editor.putString(v.Number + "TO",v.TO)
                    editor.putFloat(v.Number + "BusCapacity",v.BusCapacity)
                    editor.putInt(v.Number + "BusLevel",v.BusLevel)
                    editor.putString(v.Number + "latitude",v.latitude)
                    editor.putString(v.Number + "longitude",v.longitude)
                    editor.putString(v.Number + "Halts",v.Halts)
                    editor.putInt(v.Number + "CurrentRevenue",v.CurrentRevenue)
                    editor.putInt(v.Number + "Delay",v.Delay)
                    editor.putInt(v.Number + "CurrentPassangers",v.CurrentPassangers)
                    editor.putString(v.Number + "Halt_Lat",v.Halt_Lat)
                    editor.putString(v.Number + "Halt_Long",v.Halt_Long)
                    editor.putString(v.Number + "Distance_Time_Matrix",v.Distance_Time_Matrix)
                    editor.putString(v.Number + "data_reference",v.data_reference)
                }
                editor.apply()
                Toast.makeText(context,"Entered Critical Section",Toast.LENGTH_LONG).show()
                (activity as AppCompatActivity).supportActionBar!!.show()
                loadUserData()
            }
            0,10,20,30,40,50,60,70,80,90,100 -> {
                Viewi!!.findViewById<TextView>(R.id.PB2Tv).text = it.what.toString() + "%"
            }
        }
        return@Handler true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityj = activity as StartActivity
        reference = activityj!!.dbRef!!.child("BESTS").child("BEST NUMBERS")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       Viewi = inflater.inflate(R.layout.fragment_loading, container, false)
        (activity as AppCompatActivity).supportActionBar!!.hide()

        var stat = activityj!!.isReverse
        activityj!!.isReverse = false

        thisFragment = this
        val data = activityj!!.mApplicationContext!!.getSharedPreferences("DataSavedCopy", 0)
        var status1 = data.getInt("isData",-20) == -20

        Viewi!!.findViewById<TextView>(R.id.PB1Tv).text = "Please Wait"
        Viewi!!.findViewById<TextView>(R.id.PB2Tv).text = "Please Wait"

        if (status1 || stat){
            nullResouce(Viewi!!)
        }
        else{
            loadData(Viewi!!)
        }
        return Viewi
    }

    fun nullResouce(Viewi:View){
        Viewi.findViewById<ConstraintLayout>(R.id.ErrorLayout).visibility = View.VISIBLE
        Viewi.findViewById<ConstraintLayout>(R.id.DownlaadingLayout).visibility = View.GONE
        Viewi.findViewById<ConstraintLayout>(R.id.LoadingLayout).visibility = View.GONE
        downloadData()
    }

    fun loadData(Viewi:View){
        Viewi.findViewById<ConstraintLayout>(R.id.ErrorLayout).visibility = View.GONE
        Viewi.findViewById<ConstraintLayout>(R.id.DownlaadingLayout).visibility = View.GONE
        Viewi.findViewById<ConstraintLayout>(R.id.LoadingLayout).visibility = View.VISIBLE
        val data = activityj!!.mApplicationContext!!.getSharedPreferences("DataSavedCopy", 0)
        vsuper = data.getString("BusNumbers",null)
        if (vsuper == null){
            nullResouce(Viewi)
            return
        }
        BusNumberData.ourInstance.list = ArrayList()
        value = vsuper!!.trim().split(" ")
        for (v in value!!){
            BusNumberData.ourInstance.list.add(BusNumbers(v,data.getString(v+"FROM",""),data.getString(v+"TO",""),
                    data.getFloat(v+"BusCapacity",100f),data.getInt(v+"BusLevel",1),data.getInt(v+"CurrentPassangers",0),
                    data.getInt(v+"CurrentRevenue",0),data.getInt(v+"Delay",0),data.getString(v+"Halts",""),
                    data.getString(v+"latitude",""),data.getString(v+"longitude","")
                    ,data.getString(v+"Distance_Time_Matrix",""),data.getString(v+"Halt_Lat",""),data.getString(v+"Halt_Long","")))
        }
        (activity as AppCompatActivity).supportActionBar!!.show()
        try{
            loadUserData()
        }catch (t:Throwable){}

    }

    fun downloadData(){
        if (thisFragment!=null) {
            newDialog = LoadingDialogClass(activityj!!.mContext!!, thisFragment!!,activityj)

        }else{
            Toast.makeText(activityj,"It is null",Toast.LENGTH_LONG).show()
        }
        if (newDialog!=null) {
            newDialog!!.show()

        }else{
            Toast.makeText(activityj,"It is null dialog",Toast.LENGTH_LONG).show()
        }
    }

    fun downloadMessages(){
        var r = Runnable {
            BusNumberData.ourInstance.list = ArrayList()
            var ii = 0
            var jj = -10
            var aa : Float = 100 / value!!.size.toFloat()
            try {
                for (i in value!!) {
                    activityj!!.dbRef!!.child("BESTS").child(i).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError?) {
                        }

                        override fun onDataChange(p0: DataSnapshot?) {
                            ii++
                            var v = p0!!.getValue(BusNumbers::class.java)
                            v!!.Number = i
                            BusNumberData.ourInstance.list.add(v)
                            //Toast.makeText(context,j++.toString() + "done",Toast.LENGTH_LONG).show()
                            if (value!!.size == BusNumberData.ourInstance.list.size)
                                handler.sendEmptyMessage(2)
                            if ((ii*aa).toInt()%50 == 0 && (ii*aa).toInt()!=jj){
                                handler.sendEmptyMessage((ii*aa).toInt())
                                jj = (ii*aa).toInt()
                            }
                        }
                    })
                }
            }catch (e:Throwable){
            }
        }



        var downloadVariable = Thread(r)
        downloadVariable!!.isDaemon = true
        downloadVariable!!.start()
    }

    fun downloadDataCont(){

        newDialog!!.dismiss()
        Viewi!!.findViewById<ConstraintLayout>(R.id.ErrorLayout).visibility = View.GONE
        Viewi!!.findViewById<ConstraintLayout>(R.id.DownlaadingLayout).visibility = View.VISIBLE
        Viewi!!.findViewById<ConstraintLayout>(R.id.LoadingLayout).visibility = View.GONE



        reference!!.addListenerForSingleValueEvent(object  : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                vsuper = dataSnapshot.getValue(String::class.java).toString()
                value = vsuper!!.split(" ")
                if (value != null)
                    handler.sendEmptyMessage(1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activityj,error.toString(), Toast.LENGTH_LONG).show()            }
        })

    }

    fun loadUserData(){
        if (OverAllStaticConstants.checkIntenet()){
            FirebaseDatabase.getInstance().reference.child("USERS").child(activityj!!.user!!.uid).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    Toast.makeText(activityj,"Cancelled",Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    OverAllStaticConstants.userInformation = p0!!.getValue(UserInformation::class.java)
                    val data = activityj!!.mApplicationContext!!.getSharedPreferences("UserSavedCopy", 0)
                    val editor = data.edit()
                    editor.putString("nickName",OverAllStaticConstants.userInformation!!.nickName)
                    editor.putString("timeOfStart",OverAllStaticConstants.userInformation!!.timeOfStart)
                    editor.putString("timeLastSeen",OverAllStaticConstants.userInformation!!.timeLastSeen)
                    editor.putString("Favorites",OverAllStaticConstants.userInformation!!.Favorites)
                    editor.putFloat("currentLatitude",OverAllStaticConstants.userInformation!!.currentLatitude)
                    editor.putFloat("currentLongitude",OverAllStaticConstants.userInformation!!.currentLongitude)
                    editor.apply()
                    activityj!!.changeFragmentstart("LoadingDone")
                }
            })


        }
        else{
            val data = activityj!!.mApplicationContext!!.getSharedPreferences("UserSavedCopy", 0)
            OverAllStaticConstants.userInformation = UserInformation()
            OverAllStaticConstants.userInformation!!.nickName = data.getString("nickName",null)
            OverAllStaticConstants.userInformation!!.timeOfStart = data.getString("timeOfStart",null)
            OverAllStaticConstants.userInformation!!.timeLastSeen = data.getString("timeLastSeen",null)
            OverAllStaticConstants.userInformation!!.Favorites = data.getString("Favorites",null)
            OverAllStaticConstants.userInformation!!.currentLatitude = data.getFloat("currentLatitude",0f)
            OverAllStaticConstants.userInformation!!.currentLongitude = data.getFloat("currentLongitude",0f)
            activityj!!.changeFragmentstart("LoadingDone")
        }
    }


}// Required empty public constructor

class BooVariable {
    var isBoo = false
        set(boo) {
            field = boo
            if (listener != null) listener!!.onChange()
        }
    var listener: ChangeListener? = null

    interface ChangeListener {
        fun onChange()
    }
}
