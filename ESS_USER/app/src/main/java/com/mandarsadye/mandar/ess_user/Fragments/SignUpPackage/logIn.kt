package com.mandarsadye.mandar.ess_user.Fragments.SignUpPackage


import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mandarsadye.mandar.ess_user.Activities.SigninSignUp
import com.mandarsadye.mandar.ess_user.CustomClasses.OverAllStaticConstants
import com.mandarsadye.mandar.ess_user.DataServices.UserInformation

import com.mandarsadye.mandar.ess_user.R


/**
 * A simple [Fragment] subclass.
 */
class logIn : Fragment() , View.OnClickListener {

    var editTextEmail : EditText? = null
    var editTextPassword : EditText? = null
    var activityi : SigninSignUp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityi = activity as SigninSignUp
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_log_in, container, false)


        view.findViewById<Button>(R.id.logInButton).setOnClickListener(this)
        view.findViewById<TextView>(R.id.changeLoginOption).setOnClickListener(this)
        editTextEmail = view.findViewById<EditText>(R.id.logInTextEmailAddtess)
        editTextPassword = view.findViewById<EditText>(R.id.logInTextPassword)
        return view
    }

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.changeLoginOption -> {
                activityi!!.changeFragmentstart("ChooseScreen")
            }
            R.id.logInButton -> {
                var email = editTextEmail!!.text.toString()
                var password = editTextPassword!!.text.toString()

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(activityi,"Please Enter Email", Toast.LENGTH_LONG).show()
                    return
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(activityi,"Please Enter Password", Toast.LENGTH_LONG).show()
                    return
                }
                Toast.makeText(activityi,email+password,Toast.LENGTH_LONG).show()
                try {
                    activityi!!.firebaseAuth!!.signInWithEmailAndPassword(email,password).addOnCompleteListener(activityi!!, test1(activityi))
                }catch (t : Throwable){
                    Toast.makeText(activityi,t.toString(),Toast.LENGTH_LONG).show()
                }

            }
        }
    }

}// Required empty public constructor
class test1(activityi : SigninSignUp?) : OnCompleteListener<AuthResult> {
    var activityi1 : SigninSignUp? = null
    init {
        activityi1 = activityi
    }
    override fun onComplete(task: Task<AuthResult>) {
        if (task.isSuccessful){
            Toast.makeText(activityi1,"11111",Toast.LENGTH_LONG).show()
            activityi1!!.changeFragmentstart("Login")
        }
        else{
            Toast.makeText(activityi1,"Something went wrong", Toast.LENGTH_LONG).show()
        }
    }
}
