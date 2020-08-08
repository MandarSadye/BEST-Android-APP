package com.mandarsadye.mandar.ess_user.Fragments.SignUpPackage


import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mandarsadye.mandar.ess_user.Activities.SigninSignUp

import com.mandarsadye.mandar.ess_user.R


/**
 * A simple [Fragment] subclass.
 */
class ChooseEnter : Fragment(),View.OnClickListener {

    var activityi : SigninSignUp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityi = activity as SigninSignUp
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_choose_enter, container, false)
        val buttonLogin : Button? = view.findViewById<Button>(R.id.buttonSignIn)
        val buttonSignup : Button? = view.findViewById<Button>(R.id.buttonSignUp)
        val buttonskip : Button? = view.findViewById<Button>(R.id.SkipAuth)
        buttonLogin!!.setOnClickListener(this)
        buttonSignup!!.setOnClickListener(this)
        buttonskip!!.setOnClickListener(this)
        return view;
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.buttonSignUp->{
                activityi!!.changeFragmentstart("SignUpChoose")
            }
            R.id.buttonSignIn->{
                activityi!!.changeFragmentstart("LogInChoose")
            }
            R.id.SkipAuth->{
                activityi!!.changeFragmentstart("skipChoose")
            }
            else->null

        }
    }

}// Required empty public constructor
