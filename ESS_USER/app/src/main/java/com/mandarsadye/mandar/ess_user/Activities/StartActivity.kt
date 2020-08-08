package com.mandarsadye.mandar.ess_user.Activities

import android.Manifest.permission.CAMERA
import android.support.v4.app.FragmentManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.icu.text.UnicodeSetSpanner
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.*
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mandarsadye.mandar.ess_user.CustomClasses.OverAllStaticConstants
import com.mandarsadye.mandar.ess_user.DataServices.UserInformation
import com.mandarsadye.mandar.ess_user.Fragments.SearchPackage.SearchPanel
import com.mandarsadye.mandar.ess_user.R
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.app_bar_start.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.mandarsadye.mandar.ess_user.DataServices.BusNumberData
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.WindowManager
import android.webkit.PermissionRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.zxing.Result
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.camera.CameraSettings
import com.mandarsadye.mandar.ess_user.CustomClasses.FetchUrl
import com.mandarsadye.mandar.ess_user.CustomClasses.FetchUrlDistance
import com.mandarsadye.mandar.ess_user.CustomClasses.distance_time
import com.mandarsadye.mandar.ess_user.Fragments.Chat.ChatMain
import com.mandarsadye.mandar.ess_user.Fragments.Location.LocationDisplay
import com.mandarsadye.mandar.ess_user.Fragments.Payment.PaymentMain
import com.mandarsadye.mandar.ess_user.Fragments.SearchPackage.Loading
import com.mandarsadye.mandar.ess_user.Fragments.SignUpPackage.ChooseEnter
import com.mandarsadye.mandar.ess_user.Fragments.SignUpPackage.SignIn
import com.mandarsadye.mandar.ess_user.Fragments.SignUpPackage.logIn
import com.mandarsadye.mandar.ess_user.Model.BusNumbers


class StartActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener , OnMapReadyCallback {
    var firebaseAuth: FirebaseAuth? = null
    var userInformation: UserInformation? = null

    var manager: FragmentManager? = null
    var fm: android.support.v4.app.FragmentManager? = null
    var user: FirebaseUser? = null
    var db: FirebaseDatabase? = null
    var dbRef: DatabaseReference? = null
    var mContext: Context? = null
    var mActivity: StartActivity? = null
    var mApplicationContext: Context? = null

    var mSuppActionBar: ActionBar? = null
    var isReverse: Boolean = false

    var locationListener: mLocationListener? = null
    var locationmanager: LocationManager? = null
    var locationActivityReference: View? = null
    var locationFrag: LocationDisplay? = null
    var mMap: GoogleMap? = null
    var markers = ArrayList<Marker>()

    var qrView: DecoratedBarcodeView? = null


    var mLastKnownLocation: LatLng? = null
    var mBusStop1Location : LatLng? = null
    var mBusStop2Location : LatLng? = null
    var mBusLocation : LatLng? = null

    var mNearBus1 : String? = null
    var mNearBus2 : String? = null


    private val REQUEST_CAMERA = 1
    private val camId = Camera.CameraInfo.CAMERA_FACING_BACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        setSupportActionBar(toolbar)
        mSuppActionBar = supportActionBar

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mContext = this
        mActivity = this
        mApplicationContext = applicationContext

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        dbRef = db!!.reference
        if (firebaseAuth!!.currentUser == null && !OverAllStaticConstants.isAnonymous) {
            startActivity(Intent(this, SigninSignUp::class.java))
            finish()
        }

        user = firebaseAuth!!.currentUser


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val pinMenuItem = nav_view.menu.findItem(R.id.nav_logout)
        if (OverAllStaticConstants.isAnonymous)
            pinMenuItem.title = "SignIn / SignUp"
        else
            pinMenuItem.title = "Log out"

        manager = supportFragmentManager
        var fragment: Fragment? = manager!!.findFragmentById(R.id.StartUpReplace)
        if (fragment == null) {
            fragment = Loading()
            manager!!.beginTransaction().replace(R.id.StartUpReplace, fragment).commit()
        }
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        locationListener = mLocationListener(this)
        locationmanager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        if(!((ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)||
                        (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.INTERNET)== PackageManager.PERMISSION_GRANTED)))return
        locationmanager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0f, locationListener)


    }

    fun permissions() {
        if (checkPermission()) {
            Toast.makeText(applicationContext, "Permission already granted!", Toast.LENGTH_LONG).show()
        } else {
            requestPermission()
        }
        scan()
    }

    fun scan() {

    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext, CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(CAMERA), REQUEST_CAMERA)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA -> if (grantResults.size > 0) {

                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted) {
                    Toast.makeText(applicationContext, "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(CAMERA)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(arrayOf(CAMERA),
                                                    REQUEST_CAMERA)
                                        }
                                    })
                            return
                        }
                    }
                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        android.support.v7.app.AlertDialog.Builder(this@StartActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }


    fun changeFragmentstart(key: String) {
        when (key) {
            "LoadingDone" -> {
                var fragment = SearchPanel()
                manager!!.beginTransaction().replace(R.id.StartUpReplace, fragment).addToBackStack(null).commit()
            }


        }
    }

    fun showDetailsInFragment(bus: BusNumbers) {
        val fragment = LocationDisplay.getInstance(bus)
        manager!!.beginTransaction().replace(R.id.StartUpReplace, fragment).addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.start, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.Search -> {
                var fragment = Loading()
                manager!!.beginTransaction().replace(R.id.StartUpReplace, fragment).commit()
            }
            R.id.Chat -> {
                var fragment = ChatMain()
                manager!!.beginTransaction().replace(R.id.StartUpReplace, fragment).commit()
            }
            R.id.ReLoadData -> {
                isReverse = true
                var fragment = Loading()
                manager!!.beginTransaction().replace(R.id.StartUpReplace, fragment).commit()
            }
            R.id.nav_manage -> {
                var fragment = LocationDisplay()
                manager!!.beginTransaction().replace(R.id.StartUpReplace, fragment).commit()
            }

            R.id.nav_payment -> {
                var fragment = PaymentMain()
                manager!!.beginTransaction().replace(R.id.StartUpReplace, fragment).commit()
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
            R.id.nav_logout -> {
                if (firebaseAuth != null) {
                    firebaseAuth!!.signOut()
                }
                val settings = applicationContext.getSharedPreferences("settings", 0)
                val editor = settings.edit()
                editor.putInt("isAnonymous", 0)
                editor.apply()
                OverAllStaticConstants.isAnonymous = false
                startActivity(Intent(this, SigninSignUp::class.java))
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
    }


    fun returnDistance_time(origin: LatLng, dest: LatLng, frag: LocationDisplay) {
        val urlDistance = getUrl(origin, dest, 1)
        Log.e("MyString", urlDistance)
        val FetchUrlDist = FetchUrlDistance(mActivity!!)

        FetchUrlDist.execute(urlDistance)
    }

    var handle = Handler {
        when (it.what) {
            1,2,3,4 -> {
//                Log.e("value returned" , )
                val a: distance_time = it.data.getParcelable("MyDistanceTime")
                Log.e("value_returned", a.distance.toString())
                Log.e("value_returned", a.time.toString())
                val kk = Bundle()
                kk.putParcelable("MyDistanceTime", a as Parcelable)
                val message = Message.obtain()
                message.data = kk
                message.what = it.what
                try {
                    locationFrag!!.handle.sendMessage(message)
                } catch (e: Throwable) {
                    Log.e("Error:Distance:Fragment", e.toString())
                }

            }
        }
        return@Handler true
    }

    fun updateDisplayLocationOnMyLocation(location: LatLng?) {
        mLastKnownLocation = location
        updateNearStops(location!!)
        if (mMap == null || locationActivityReference == null || locationFrag == null) return
        if (mLastKnownLocation!=null && mBusLocation!=null && mBusStop1Location!= null && mBusStop2Location !=null) {
            changeLocationMap(mLastKnownLocation!!, mBusLocation!!, mBusStop1Location!!, mBusStop2Location!!)
        }
    }

    fun updateDisplayLocationOnBusLocation(location: LatLng?) {
        mBusLocation = location
        if (mMap == null || locationActivityReference == null || locationFrag == null) return
        if (mLastKnownLocation!=null && mBusLocation!=null && mBusStop1Location!= null && mBusStop2Location !=null) {
            changeLocationMap(mLastKnownLocation!!, mBusLocation!!, mBusStop1Location!!, mBusStop2Location!!)
        }
    }

    fun updateNearStops(location: LatLng){
        var station1 : String? = mNearBus1
        var station2 : String? = mNearBus2
        var i1 = Double.MAX_VALUE - 1
        var i2 = Double.MAX_VALUE
        val p = locationFrag!!.bus
        val q = p!!.Halts.split(" ").size
        val r =  p!!.Halt_Lat.split(" ")
        val s =  p!!.Halt_Long.split(" ")
        val t = p!!.Halts.split(" ")
        var u = 0
        var v = 0
        for (i in 0 until q-1){
            val a = distance(location.latitude,location.longitude,r[i].trim().toDouble(),s[i].trim().toDouble())
            if (a < i1){
                station2 = station1
                station1 = t[i]
                i2 = i1
                i1 = a
                v = u
                u = i

            }
            else if (a < i2){
                station2 = t[i]
                i2 = a
                v = i
            }
        }
        mNearBus1 = station1
        mNearBus2 = station2
        mBusStop1Location = LatLng(r[u].trim().toDouble(),s[u].trim().toDouble())
        mBusStop2Location = LatLng(r[v].trim().toDouble(),s[v].trim().toDouble())
        Log.e("mBusStop1Location",mBusStop1Location.toString())
        Log.e("mBusStop2Location",mBusStop2Location.toString())
        Log.e("mBusStop1Location",t[u].toString())
        Log.e("mBusStop2Location",t[v].toString())
    }

    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {

        fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        fun rad2deg(rad: Double): Double {
            return rad * 180.0 / Math.PI
        }


        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta)))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60.0 * 1.1515
        return dist
    }

    fun changeLocationMap(myLocation: LatLng, busLocation: LatLng, busStop1: LatLng, busStop2: LatLng) {
        try {

            markers = ArrayList()
            var view = locationActivityReference!!
            var frag = locationFrag!!
            var bus = frag.bus

            val myPositionLatLng = LatLng(myLocation!!.latitude, myLocation!!.longitude)
            val busPositionLatLng = LatLng(busLocation!!.latitude, busLocation!!.longitude)
            val stop1PositionLatLng = LatLng(busStop1!!.latitude, busStop1!!.longitude)
            val stop2PositionLatLng = LatLng(busStop2!!.latitude, busStop2!!.longitude)

            mMap!!.clear()

            val urlStop1PositionLatLng = getUrl(myPositionLatLng, stop1PositionLatLng)
            val FetchUrlStop1PositionLatLng = FetchUrl(mActivity!!,1)
            FetchUrlStop1PositionLatLng.execute(urlStop1PositionLatLng)

            val urlstop2PositionLatLng = getUrl(myPositionLatLng, stop2PositionLatLng)
            val FetchUrlstop2PositionLatLng = FetchUrl(mActivity!!,2)
            FetchUrlstop2PositionLatLng.execute(urlstop2PositionLatLng)

            val urlDistancestop1PositionLatLng = getUrl(myPositionLatLng, stop1PositionLatLng , 1)
            val FetchUrlDiststop1PositionLatLng = FetchUrlDistance(mActivity!!,1)
            FetchUrlDiststop1PositionLatLng.execute(urlDistancestop1PositionLatLng)

            val urlDistancestop2PositionLatLng = getUrl(myPositionLatLng, stop2PositionLatLng , 1)
            val FetchUrlDiststop2PositionLatLng = FetchUrlDistance(mActivity!!,2)
            FetchUrlDiststop2PositionLatLng.execute(urlDistancestop2PositionLatLng)

            val urlDistancestop3PositionLatLng = getUrl(busPositionLatLng, stop2PositionLatLng , 1)
            val FetchUrlDiststop3PositionLatLng = FetchUrlDistance(mActivity!!,3)
            FetchUrlDiststop3PositionLatLng.execute(urlDistancestop3PositionLatLng)

            val urlDistancestop4PositionLatLng = getUrl(busPositionLatLng, stop1PositionLatLng , 1)
            val FetchUrlDiststop4PositionLatLng = FetchUrlDistance(mActivity!!,4)
            FetchUrlDiststop4PositionLatLng.execute(urlDistancestop4PositionLatLng)

            var optionsmyPositionLatLng = MarkerOptions()
            optionsmyPositionLatLng.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

            val optionsbusPositionLatLngg = MarkerOptions()
            optionsbusPositionLatLngg.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))

            val optionsmystop1PositionLatLng = MarkerOptions()
            optionsmystop1PositionLatLng.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

            val optionsstop2PositionLatLng = MarkerOptions()
            optionsstop2PositionLatLng.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))

            view.findViewById<ConstraintLayout>(R.id.CRlayout1).visibility = View.VISIBLE
            view.findViewById<ConstraintLayout>(R.id.CRlayout2).visibility = View.GONE

            markers.add(mMap!!.addMarker(optionsmyPositionLatLng.position(myPositionLatLng)))
            markers.add(mMap!!.addMarker(optionsbusPositionLatLngg.position(busPositionLatLng)))
            markers.add(mMap!!.addMarker(optionsmystop1PositionLatLng.position(stop1PositionLatLng)))
            markers.add(mMap!!.addMarker(optionsstop2PositionLatLng.position(stop2PositionLatLng)))

            var builder = LatLngBounds.Builder()
            for (marker in markers) {
                builder.include(marker.position)
            }
            var bounds = builder.build()

            var padding = 50     // offset from edges of the map in pixels
            var cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)

            mMap!!.moveCamera(cu)


            mMap!!.mapType = GoogleMap.MAP_TYPE_HYBRID

        } catch (e: Throwable) {
            Toast.makeText(mActivity,"Excetion Occured",Toast.LENGTH_LONG).show()
        }

    }

    fun getUrl(origin: LatLng, dest: LatLng, status: Int = 0): String {

        // Origin of route
        var str_origin = "origin=" + origin.latitude + "," + origin.longitude
        if (status == 1)
            str_origin = "origins=" + origin.latitude + "," + origin.longitude

        // Destination of route
        var str_dest = "destination=" + dest.latitude + "," + dest.longitude
        if (status == 1)
            str_dest = "destinations=" + dest.latitude + "," + dest.longitude


        // Sensor enabled
        val sensor = "sensor=false"

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor"

        // Output format
        val output = "json"

        // Building the url to the web service

        if (status == 1) {
            var mmmm = "https://maps.googleapis.com/maps/api/distancematrix/json?${str_origin}&${str_dest}&key=AIzaSyCrR8Vc2ETimxf9IbdjSXaboZq7jg6Lz5Q"
            Log.e("myLink", mmmm)
            return mmmm

        }
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"

    }

}

class mLocationListener(activity: StartActivity) : LocationListener{
    var activity = activity
    override fun onLocationChanged(location: Location?) {

        try {
            activity.updateDisplayLocationOnMyLocation(location = LatLng(location!!.latitude, location.longitude))
        } catch (e: Throwable) {
            Log.e("mLocationListener", e.toString())
        }
    }


    override fun onProviderDisabled(provider: String?) {
        null
    }

    override fun onProviderEnabled(provider: String?) {
        null
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        null
    }

    class distance_time(distance: Int,time : Int){
        val distance = distance
        val time = time
    }

}
