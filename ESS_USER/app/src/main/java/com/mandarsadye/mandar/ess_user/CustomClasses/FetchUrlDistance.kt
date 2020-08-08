package com.mandarsadye.mandar.ess_user.CustomClasses

import android.os.AsyncTask
import android.os.Bundle
import android.os.Message
import android.os.Parcelable
import android.util.Log
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class FetchUrlDistance (activity: StartActivity,stopIndex : Int= 1) : AsyncTask<String, Void, String>() {

    val activity = activity
    val stopIndex = stopIndex
    override fun doInBackground(vararg url: String): String {

        // For storing data from web service
        var data = ""

        try {
            // Fetching the data from web service
            data = downloadUrl(url[0])
        } catch (e: Exception) {
            Log.e("Background Task", e.toString())
        }

        return data
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        Log.v("MyResult",result)
        val jObject: JSONObject = JSONObject(result)
        val rows = jObject.getJSONArray("rows")
        val elements = (rows.get(0) as JSONObject).getJSONArray("elements")
        val distance = (elements.get(0) as JSONObject).get("distance")
        val duration = (elements.get(0) as JSONObject).get("duration")
        val distValue = (distance as JSONObject).get("value").toString()
        val durationValue = (duration as JSONObject).get("value").toString()
        Log.e("Distance",distValue)
        Log.e("Duration",durationValue)

        var a = distance_time(distValue.toInt(),durationValue.toInt())
        var b = Bundle()
        b.putParcelable("MyDistanceTime",a as Parcelable)
        var message = Message.obtain()
        message.data = b
        message.what = stopIndex
        activity!!.handle.sendMessage(message)

//        val parserTask = ParserTask(activity)
//
//        // Invokes the thread for parsing the JSON data
//        parserTask.execute(result)

    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection

            // Connecting to url
            urlConnection!!.connect()

            // Reading data from url
            iStream = urlConnection!!.getInputStream()

            val br = BufferedReader(InputStreamReader(iStream))

            val sb = StringBuffer()

            var line = br.readLine()
            while (line != null) {
                sb.append(line)
                line = br.readLine()
            }

            data = sb.toString()
            Log.e("downloadUrl", data)
            br.close()

        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }
}