package com.mandarsadye.mandar.ess_user.Fragments.Chat


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.opengl.Visibility
import android.support.design.widget.FloatingActionButton
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.CustomClasses.OverAllStaticConstants
import com.mandarsadye.mandar.ess_user.Fragments.SearchPackage.SearchLoader
import com.mandarsadye.mandar.ess_user.Fragments.SearchPackage.SearchPanel
import com.mandarsadye.mandar.ess_user.Fragments.SearchPackage.SearchTextChange

import com.mandarsadye.mandar.ess_user.R
import kotlinx.android.synthetic.*


/**
 * A simple [Fragment] subclass.
 */
class ChatMain : Fragment() {

    var activityi : StartActivity? = null
    var EditTextChat : EditText? = null
    var EnterButton : ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        activity.findViewById<FloatingActionButton>(R.id.fab).visibility = View.GONE

        activityi = activity as StartActivity
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_chat_main, container, false)

        activityi!!.fm =  activityi!!.manager!!
        var chatHistory  = LoadingChats.getInstance(this)
        activityi!!.fm!!.beginTransaction().replace(R.id.charReplacement,chatHistory).commit()

        EditTextChat = view.findViewById(R.id.addChat)
        EditTextChat!!.addTextChangedListener(ChatTextChange(activityi!!,view))

        view.findViewById<ImageView>(R.id.enterChat).setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                var k : String = OverAllStaticConstants.userInformation!!.nickName + 16.toChar() + EditTextChat!!.text.toString() + 16.toChar() + OverAllStaticConstants.DateTime()
                activityi!!.dbRef!!.child("CHAT").child("New").addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        var m = p0!!.getValue(String::class.java)
                        if(m==null){
                            activityi!!.dbRef!!.child("CHAT").child("New").setValue(k)
                        }
                        else if (m.trim() == ""){
                            activityi!!.dbRef!!.child("CHAT").child("New").setValue(k)
                        }
                        else{
                            activityi!!.dbRef!!.child("CHAT").child("New").setValue(m.trim() + 17.toChar() + k)
                        }
                        activityi!!.dbRef!!.child("CHAT").child("status").child("isRead").setValue(0)
                        EditTextChat!!.setText("")
                    }
                })
            }
        })

        return view
    }

    override fun onStop() {
        super.onStop()
//        activity.findViewById<FloatingActionButton>(R.id.fab).visibility = View.VISIBLE

    }

    fun changeFrag(){
        var fragment = ChatHistory()
        activityi!!.fm!!.beginTransaction().replace(R.id.charReplacement,fragment).commit()
    }
}
class ChatTextChange(activityi: StartActivity,view: View) : TextWatcher {
    var activity = activityi
    var view = view
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

}
