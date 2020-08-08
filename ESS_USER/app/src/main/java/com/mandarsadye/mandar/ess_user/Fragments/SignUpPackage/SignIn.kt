package com.mandarsadye.mandar.ess_user.Fragments.SignUpPackage


import android.os.Bundle
import android.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.mandarsadye.mandar.ess_user.Activities.SigninSignUp
import com.mandarsadye.mandar.ess_user.CustomClasses.OverAllStaticConstants
import com.mandarsadye.mandar.ess_user.DataServices.UserInformation

import com.mandarsadye.mandar.ess_user.R
import java.util.*
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 */
class SignIn : Fragment() ,View.OnClickListener{

    var editTextEmail : EditText? = null
    var editTextPassword : EditText? = null
    var passwordConfirm : EditText? = null
    var editUserName : EditText? = null
    var activityi : SigninSignUp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityi = activity as SigninSignUp
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        editTextEmail = view.findViewById<EditText>(R.id.SignUpTextEmailAddtess)
        editTextPassword = view.findViewById(R.id.SignUpTextPassword)
        passwordConfirm = view.findViewById(R.id.SignUpConfirmPassword)
        editUserName = view.findViewById(R.id.SignUpUserName)
        view.findViewById<Button>(R.id.buttonNextA).setOnClickListener(this)
        view.findViewById<TextView>(R.id.changeSigninOption).setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.changeSigninOption -> {
                activityi!!.changeFragmentstart("ChooseScreen")
            }
            R.id.buttonNextA -> {
                var email = editTextEmail!!.text.toString()
                var password = editTextPassword!!.text.toString()
                var confirm = passwordConfirm!!.text.toString()
                var usename = editUserName!!.text.toString()
                if(TextUtils.isEmpty(usename)){
                    Toast.makeText(activityi,"Please Enter UserName", Toast.LENGTH_LONG).show()
                    return
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(activityi,"Please Enter Email", Toast.LENGTH_LONG).show()
                    return
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(activityi,"Please Enter Password", Toast.LENGTH_LONG).show()
                    return
                }
                if(password != confirm){
                    Toast.makeText(activityi,"Re-enterd password is not same as original", Toast.LENGTH_LONG).show()
                    return
                }

                activityi!!.firebaseAuth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener(activityi!!, test(activityi,usename))
            }
        }
    }

}

class test(activityi : SigninSignUp?, string: String) : OnCompleteListener<AuthResult> {
    var activityi1 : SigninSignUp? = null
    var string : String = string
    init {
        activityi1 = activityi
    }
    override fun onComplete(task: Task<AuthResult>) {
        if (task.isSuccessful){
            var userInformation = UserInformation(string,OverAllStaticConstants.DateTime())
            OverAllStaticConstants.userInformation = userInformation

            val data = activityi1!!.applicationContext.getSharedPreferences("UserSavedCopy", 0)
            val editor = data.edit()
            editor.putString("nickName",OverAllStaticConstants.userInformation!!.nickName)
            editor.putString("timeOfStart",OverAllStaticConstants.userInformation!!.timeOfStart)
            editor.putString("timeLastSeen",OverAllStaticConstants.userInformation!!.timeLastSeen)
            editor.putString("Favorites",OverAllStaticConstants.userInformation!!.Favorites)
            editor.putFloat("currentLatitude",OverAllStaticConstants.userInformation!!.currentLatitude)
            editor.putFloat("currentLongitude",OverAllStaticConstants.userInformation!!.currentLongitude)
            editor.apply()

            activityi1!!.firebaseAuth = FirebaseAuth.getInstance()
            activityi1!!.useri = activityi1!!.firebaseAuth!!.currentUser
            activityi1!!.databaseReference!!.child("USERS").child(activityi1!!.useri!!.uid).setValue(userInformation)
            Toast.makeText(activityi1,"Information Saved .... ", Toast.LENGTH_LONG).show()
            activityi1!!.changeFragmentstart("Login")
        }
        else{
            Toast.makeText(activityi1,"Something went wrong", Toast.LENGTH_LONG).show()
        }
    }
}
