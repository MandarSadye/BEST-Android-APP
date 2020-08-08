package com.mandarsadye.mandar.ess_user.CustomClasses

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import java.nio.file.Files.size
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import android.os.AsyncTask
import android.util.Log
import com.mandarsadye.mandar.ess_user.Activities.StartActivity


class ParserTask(activity: StartActivity,stopIndex:Int) : AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {

    val stopIndex = stopIndex
    val activity = activity
    // Parsing the data in non-ui thread
    override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>>? {

        val jObject: JSONObject
        var routes: List<List<HashMap<String, String>>>? = null

        try {
            jObject = JSONObject(jsonData[0])
            Log.e("ParserTask", jsonData[0])
            val parser = DataParser()
            Log.e("ParserTask", parser.toString())

            // Starts parsing data
            routes = parser.parse(jObject)
            Log.e("ParserTask", "Executing routes")
            Log.e("ParserTask", routes.toString())

        } catch (e: Exception) {
            Log.e("ParserTask", e.toString())
            e.printStackTrace()
        }

        return routes
    }

    // Executes in UI thread, after the parsing process
    override fun onPostExecute(result: List<List<HashMap<String, String>>>) {
        var points: ArrayList<LatLng>
        var lineOptions: PolylineOptions? = null

        // Traversing through all the routes
        for (i in result.indices) {
            points = ArrayList()
            lineOptions = PolylineOptions()

            // Fetching i-th route
            val path = result[i]

            // Fetching all the points in i-th route
            for (j in path.indices) {
                val point = path[j]

                val lat = java.lang.Double.parseDouble(point["lat"])
                val lng = java.lang.Double.parseDouble(point["lng"])
                val position = LatLng(lat, lng)

                points.add(position)
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points)
            lineOptions.width(2f)
            when (stopIndex){
                1->lineOptions.color(Color.MAGENTA)
                2->lineOptions.color(Color.BLUE)
            }


            Log.e("onPostExecute", "onPostExecute lineoptions decoded")

        }

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null) {
            activity.mMap!!.addPolyline(lineOptions)
        } else {
            Log.e("onPostExecute", "without Polylines drawn")
        }
    }
}