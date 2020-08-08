package com.mandarsadye.mandar.ess_user.Fragments.Chat


import android.os.Bundle
import android.support.v4.app.Fragment
import android.content.Context
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.Adapter.ChatAdapter
import com.mandarsadye.mandar.ess_user.Adapter.SearchAdapter
import com.mandarsadye.mandar.ess_user.CustomClasses.CustomScrollListener
import com.mandarsadye.mandar.ess_user.CustomClasses.OverAllStaticConstants
import com.mandarsadye.mandar.ess_user.DataServices.BusNumberData
import com.mandarsadye.mandar.ess_user.DataServices.ChatData
import com.mandarsadye.mandar.ess_user.Fragments.SearchPackage.BooVariable
import com.mandarsadye.mandar.ess_user.Fragments.SearchPackage.SearchLoader
import com.mandarsadye.mandar.ess_user.Model.ChatInput
import com.mandarsadye.mandar.ess_user.Model.Chats

import com.mandarsadye.mandar.ess_user.R
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 */
class ChatHistory : Fragment() {

    var activityj : StartActivity? = null
    var adapter : ChatAdapter? = null
    var reference : DatabaseReference? = null
    var timeValues : String? = null
    var value : ArrayList<String>? = null
    var recyclerview : RecyclerView? =null
    var layoutManager : LinearLayoutManager? = null
    var downloadVariable : Thread? = null
    var shouldBreakThread : Boolean = true

    var handler = Handler(){
        when (it.what.toInt()){
            1 -> {
                downloadMessages()
            }
            2 -> {
                adapter!!.swapData(ChatData.ourInstance.list)
            }
            3 -> {
                recyclerview!!.layoutManager.scrollToPosition(adapter!!.itemCount -1 )
            }
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
        var view = inflater.inflate(R.layout.fragment_chat_history, container, false)
        recyclerview = view.findViewById(R.id.recyclerChat)
        recyclerview!!.setHasFixedSize(true)

        adapter  = ChatAdapter(ChatData.ourInstance.getChat(),activityj!!,this)
        recyclerview!!.adapter = adapter

        layoutManager = LinearLayoutManager(context)
        layoutManager!!.orientation = LinearLayoutManager.VERTICAL
        layoutManager!!.scrollToPosition(adapter!!.itemCount -1 )
        recyclerview!!.layoutManager = layoutManager

        recyclerview!!.addOnScrollListener(CustomScrollListener(activityj!!,OverAllStaticConstants.userInformation!!.nickName.trim()))
        downloadData()

        return view
    }

    override fun onStop() {
        super.onStop()
        shouldBreakThread = false
    }

    class myLinearLayout(contect : Context ):LinearLayoutManager(contect){
        override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
            return RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
        }
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
                        if (v == null || v!!.Message == "" || v!!.Sender == ""){
                            OverAllStaticConstants.userInformation!!.timeLastSeen = ChatData.ourInstance.list[ChatData.ourInstance.list.size - 1].Time
                            return
                        }

                        var messageSplit = p.split(v!!.Message)
                        var senderSplit = p.split(v!!.Sender)
                        var index = messageSplit.size
                        var j = 0

                        while (j < index) {
                            ChatData.ourInstance.addChat(Chats(senderSplit[j], messageSplit[j], i))
                            try {
                            }catch (t : NullPointerException){
                                OverAllStaticConstants.userInformation!!.timeLastSeen = ChatData.ourInstance.list[ChatData.ourInstance.list.size - 1].Time
                                return
                            }

                            j++
                        }
                        try {
                        }catch (t : NullPointerException){
                            OverAllStaticConstants.userInformation!!.timeLastSeen = ChatData.ourInstance.list[ChatData.ourInstance.list.size - 1].Time
                            return

                        }

                        var n = value!!.size
                        var o = status
                        var m = n == o
                        if (value!!.size == status) {
                            handler.sendEmptyMessage(2)
                            status = 0
                        }
                    }
                })
            }

        }

        downloadVariable = Thread(r)
        downloadVariable!!.isDaemon = true
        downloadVariable!!.start()
    }


    fun downloadData(){
        val p = Pattern.compile("\\u022a", Pattern.LITERAL)
        reference!!.child("Time")!!.addValueEventListener(object  : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                timeValues = dataSnapshot.getValue(String::class.java).toString()
                if(timeValues == "" || timeValues == null)return
                var temp = p.split(timeValues)
                value = ArrayList()
                for (ii in temp){
//                    if (ii.compareTo(OverAllStaticConstants.userInformation!!.timeLastSeen) <= 0)continue
                    value!!.add(ii)
//                    OverAllStaticConstants.userInformation!!.timeLastSeen = ii
                }
                if (value != null)
                    handler.sendEmptyMessage(1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activityj,error.toString(), Toast.LENGTH_LONG).show()
            }
        })


    }
}// Required empty public constructor
