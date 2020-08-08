package com.mandarsadye.mandar.ess_user.Fragments.Chat


import android.os.Bundle
import android.support.v4.app.Fragment
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.CustomClasses.OverAllStaticConstants
import com.mandarsadye.mandar.ess_user.DataServices.BusNumberData
import com.mandarsadye.mandar.ess_user.DataServices.ChatData
import com.mandarsadye.mandar.ess_user.Fragments.SearchPackage.BooVariable
import com.mandarsadye.mandar.ess_user.Model.BusNumbers
import com.mandarsadye.mandar.ess_user.Model.ChatInput
import com.mandarsadye.mandar.ess_user.Model.Chats

import com.mandarsadye.mandar.ess_user.R
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 */
class LoadingChats : Fragment() {

    var activityj : StartActivity? = null
    var reference : DatabaseReference? = null
    var isDone : BooVariable = BooVariable()
    var timeValues : String? = null
    var value : Array<String>? = null
    var callingFrag:ChatMain? = null
    var downloadVariable : Thread? = null
    var v : View? = null

    var handler = Handler(){
        when (it.what.toInt()){
            1 -> {
                downloadMessages()
            }
            2 -> callingFrag!!.changeFrag()
        }
        return@Handler true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityj = activity as StartActivity
        reference = activityj!!.dbRef!!.child("CHAT_DISPLAY")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

//        downloadData()
        v = inflater.inflate(R.layout.fragment_loading_chats, container, false)
        downloadByThread()
        return v
    }

    fun downloadByThread(){
        var r = Runnable {
            val p = Pattern.compile("\\u022a", Pattern.LITERAL)
            reference!!.child("Time")!!.addListenerForSingleValueEvent(object  : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    timeValues = dataSnapshot.getValue(String::class.java).toString().trim()
                    value = p.split(timeValues)
                    if (value != null && timeValues != "")
                        handler.sendEmptyMessage(1)
                    else
                        handler.sendEmptyMessage(2)
                    reference!!.child("Time")!!.removeEventListener(this)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activityj,error.toString(), Toast.LENGTH_LONG).show()
                }
            })
        }

        downloadVariable = Thread(r)
        downloadVariable!!.isDaemon = true
        downloadVariable!!.start()
    }

    fun downloadMessages(){

        var r = Runnable {
            var status = 0
            val p = Pattern.compile("\\u022a", Pattern.LITERAL)
            ChatData.ourInstance.list = ArrayList()
            for (i in value!!) {
                reference!!.child(i).addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError?) {
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        status++
                        var v = p0!!.getValue(ChatInput::class.java)
                        Log.e("chatCrash",(v == null).toString() + i)
                        var messageSplit = p.split(v!!.Message)
                        var senderSplit = p.split(v!!.Sender)
                        var index = messageSplit.size
                        var j = 0
                        while (j < index) {
                            ChatData.ourInstance.addChat(Chats(senderSplit[j], messageSplit[j], i))
                            j++
                        }

                        Toast.makeText(context, j.toString() + "done", Toast.LENGTH_LONG).show()
                        if (value!!.size == status) {
                            OverAllStaticConstants.userInformation!!.timeLastSeen = i
                            handler.sendEmptyMessage(2)
                        }
                    }
                })
            }
        }

        downloadVariable = Thread(r)
        downloadVariable!!.isDaemon = true
        downloadVariable!!.start()
    }

    companion object {
        @JvmStatic
        fun getInstance(input : ChatMain):LoadingChats{
            var a = LoadingChats()
            a.callingFrag = input
            return  a
        }
    }

}// Required empty public constructor
