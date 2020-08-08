package com.mandarsadye.mandar.ess_user.Fragments.Location


import android.os.Bundle
import android.support.v4.app.Fragment
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.Message
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.CustomClasses.distance_time
import com.mandarsadye.mandar.ess_user.DataServices.BusNumberData
import com.mandarsadye.mandar.ess_user.Fragments.Chat.ChatHistory
import com.mandarsadye.mandar.ess_user.Fragments.Chat.LoadingChats
import com.mandarsadye.mandar.ess_user.Model.BusNumbers

import com.mandarsadye.mandar.ess_user.R



class LocationDisplay : Fragment() ,LocationListener{

    var mActivity : StartActivity? = null
    var mContext : Context? = null
    var bus : BusNumbers? = null
    var thisFragment : LocationDisplay? = null
    var v :View? = null

    //views to be updated :
    var name1 : TextView? = null
    var timeToReachStop1 : TextView? = null
    var timeForBusToReachStop1 : TextView? = null
    var crowding1 : TextView? = null
    var walkingDistance1 : TextView? = null

    var name2 : TextView? = null
    var timeToReachStop2 : TextView? = null
    var timeForBusToReachStop2 : TextView? = null
    var crowding2 : TextView? = null
    var walkingDistance2 : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as StartActivity
        mContext = activity
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_location_display, container, false)

        v!!.findViewById<TextView>(R.id.LocationDisplayBusNumberField).text = "BUS NUMBER : " + bus!!.Number!!.replace("_"," ")

        mActivity!!.locationActivityReference = v!!
        mActivity!!.locationFrag = this
        v!!.findViewById<ConstraintLayout>(R.id.CRlayout1).visibility = View.GONE
        v!!.findViewById<ConstraintLayout>(R.id.CRlayout2).visibility = View.VISIBLE
//        Toast.makeText(mContext,bus!!.Halts,Toast.LENGTH_LONG).show()

        var mapFragment : SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        if (mapFragment == null){
            var fm = fragmentManager
            var ft = fm.beginTransaction()
            mapFragment = SupportMapFragment()
            ft.replace(R.id.map,mapFragment)
        }
        mapFragment!!.getMapAsync(mActivity)

        mActivity!!.fm =  mActivity!!.manager!!
        var fragment = displayStops.getInstance(bus)
        mActivity!!.fm!!.beginTransaction().replace(R.id.displayReplacement,fragment).commit()

        mActivity!!.dbRef!!.child("BESTS")!!.child(bus!!.Number).child("Delay").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                v!!.findViewById<TextView>(R.id.locationDisplayDelay).text = p0!!.getValue(Int::class.java).toString()
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })

        name1 = v!!.findViewById(R.id.station1Name)
        timeToReachStop1 = v!!.findViewById(R.id.station1stopTime)
        timeForBusToReachStop1 = v!!.findViewById(R.id.station1timeforbusStop)
        crowding1 = v!!.findViewById(R.id.station1crowding)
        walkingDistance1 = v!!.findViewById(R.id.station1walking)

        name2 = v!!.findViewById(R.id.station2Name)
        timeToReachStop2 = v!!.findViewById(R.id.station2stopTime)
        timeForBusToReachStop2 = v!!.findViewById(R.id.station2timeForBusStop)
        crowding2 = v!!.findViewById(R.id.station2crowding)
        walkingDistance2 = v!!.findViewById(R.id.station2walking)

        v!!.findViewById<TextView>(R.id.capacity).text = bus!!.BusCapacity.toString()
        v!!.findViewById<TextView>(R.id.decks).text = bus!!.BusLevel.toString()
        mActivity!!.dbRef!!.child("BESTS")!!.child(bus!!.Number).child("CurrentPassangers").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                for (i in BusNumberData.ourInstance.list){
                    if(i.Number == bus!!.Number) {
                        i.CurrentPassangers = p0!!.getValue(Int::class.java)!!.toInt()
                        bus = i
                        v!!.findViewById<TextView>(R.id.currentFilledCapacity).text = bus!!.CurrentPassangers.toString()
                        break
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
        mActivity!!.dbRef!!.child("BESTS")!!.child(bus!!.Number).child("latitude").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                for (i in BusNumberData.ourInstance.list){
                    if(i.Number == bus!!.Number) {
                        var a = p0!!.getValue(String::class.java)!!.toDouble()
                        var c = 0
                        for (i in bus!!.latitude.trim().split(" ")){
                            if (i.toDouble() == a)break
                            c++
                        }
                        val bb = bus!!.Halt_Long.trim().split(" ")
                        Log.e("debug",c.toString() + "  " )
                        val b = bb[c].trim().toDouble()
                        mActivity!!.updateDisplayLocationOnBusLocation(LatLng(a,b))
                        bus = i
                        v!!.findViewById<TextView>(R.id.currentFilledCapacity).text = bus!!.CurrentPassangers.toString()
                        break
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
        return v!!
    }

    override fun onStop() {
        super.onStop()
        mActivity!!.locationActivityReference = null
        mActivity!!.locationFrag = null
    }


    override fun onLocationChanged(location: Location) {
        Toast.makeText(mContext,location.toString(),Toast.LENGTH_LONG).show()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun getInstance(bus : BusNumbers) : LocationDisplay{
            var frag = LocationDisplay()
            frag.bus = bus
            frag.thisFragment = frag
            return frag
        }
    }

    var handle = Handler{
        when(it.what){
            1 -> {
//                Log.e("value returned" , )
                val a : distance_time = it.data.getParcelable("MyDistanceTime")
                Log.e("value_returned_frag" , a.distance.toString())
                Log.e("value_returned_frag" , a.time.toString())
                var p = "m"
                var q = 1
                if(a.distance>1000){
                    p = "km"
                    q = 1000
                }

                walkingDistance1!!.text = "WalkingDistance to Bus Stop : " + (a.distance/q).toString() + " " + p

                p = "sec"
                q = 1
                if(a.time>60){
                    p = "min"
                    q = 60
                }
                timeToReachStop1!!.text = "Time Required to reach Bus Stop : " + (a.time/q).toString() + " " + p
            }
            2 -> {
//                Log.e("value returned" , )
                val a : distance_time = it.data.getParcelable("MyDistanceTime")
                Log.e("value_returned_frag" , a.distance.toString())
                Log.e("value_returned_frag" , a.time.toString())
                var p = "m"
                var q = 1
                if(a.distance>1000){
                    p = "km"
                    q = 1000
                }

                walkingDistance2!!.text = "WalkingDistance to Bus Stop : " + (a.distance/q).toString() + " " + p

                p = "sec"
                q = 1
                if(a.time>60){
                    p = "min"
                    q = 60
                }
                timeToReachStop2!!.text = "Time Required to reach Bus Stop : " + (a.time/q).toString() + " " + p


                name1!!.text = "Bus Stop : " +  mActivity!!.mNearBus1!!.replace("_"," ")
                name2!!.text = "Bus Stop : " +  mActivity!!.mNearBus2!!.replace("_"," ")
            }
            3 -> {
                val a : distance_time = it.data.getParcelable("MyDistanceTime")
                Log.e("value_returned_frag" , a.distance.toString())
                Log.e("value_returned_frag" , a.time.toString())
                var p = "sec"
                var q = 1
                if(a.time>60){
                    p = "min"
                    q = 60
                }
                timeForBusToReachStop2!!.text = "Time for Bus to Reach Bust Stop : " +  (a.time/q).toString() + " " + p
            }
            4 -> {
                val a : distance_time = it.data.getParcelable("MyDistanceTime")
                Log.e("value_returned_frag" , a.distance.toString())
                Log.e("value_returned_frag" , a.time.toString())
                var p = "sec"
                var q = 1
                if(a.time>60){
                    p = "min"
                    q = 60
                }
                timeForBusToReachStop1!!.text = "Time for Bus to Reach Bust Stop : " +  (a.time/q).toString() + " " + p
            }


        }

        mActivity!!.dbRef!!.child("BESTS")!!.child(bus!!.Number).child("data_reference").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                val a = p0!!.getValue(String::class.java)!!.trim().split(" ")
                var b = 0
                for (i in a){
                    b = b + i.toString().trim().toInt()
                }
                Log.e("myxyz",a.toString())
                Log.e("myxyz",b.toString())

                var kutosis = b/a.size
                var p = 0
                var q = 0
                var r = 0
                for (i in bus!!.Halts.trim().split(" ")){
                    if (i == mActivity!!.mNearBus1)p = r
                    if (i == mActivity!!.mNearBus2)q = r
                    r++
                }
                r = 0
                for (i in bus!!.Halt_Lat.trim().split(" ")){
                    if (i == bus!!.latitude)break
                    r++
                }
                var num1 = 0
                var s = 0
                for (i in a){
                    if (s == p)break
                    num1 = num1 + i.toString().trim().toInt()
                    s++
                }
                num1 = num1 / s

                crowding1!!.text = "Expected Crowd : " + (kutosis*(s-r)*150/600 + bus!!.CurrentPassangers).toString()

                num1 = 0
                s = 0
                for (i in a){
                    if (s == q)break
                    num1 = num1 + i.toString().trim().toInt()
                    s++
                }
                num1 = num1 / s

                crowding2!!.text = "Expected Crowd : " + (kutosis*(s-r)*150/600 + bus!!.CurrentPassangers).toString()


                Log.e("myxyz",p.toString()+ "    " + q.toString() + "    " + r.toString()+ "    " + num1.toString()+ "    " + (s-r).toString() )
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })

        return@Handler true
    }



}

