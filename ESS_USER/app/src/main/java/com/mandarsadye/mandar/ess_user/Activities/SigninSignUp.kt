package com.mandarsadye.mandar.ess_user.Activities

import android.app.Fragment
import android.app.FragmentManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.mandarsadye.mandar.ess_user.CustomClasses.OverAllStaticConstants
import com.mandarsadye.mandar.ess_user.DataServices.UserInformation
import com.mandarsadye.mandar.ess_user.Fragments.SignUpPackage.ChooseEnter
import com.mandarsadye.mandar.ess_user.Fragments.SignUpPackage.SignIn
import com.mandarsadye.mandar.ess_user.Fragments.SignUpPackage.logIn
import com.mandarsadye.mandar.ess_user.R
import android.util.DisplayMetrics
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.os.StrictMode
import android.util.Log


class SigninSignUp : AppCompatActivity() {
    var firebaseAuth : FirebaseAuth? = null
    var manager : FragmentManager? = null
    var databaseReference : DatabaseReference? =null
    var useri : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_sign_up)
        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        useri = firebaseAuth!!.currentUser

        try {
            val settings = applicationContext.getSharedPreferences("settings", 0)
            OverAllStaticConstants.isAnonymous = settings.getInt("isAnonymous", 0)==1
        }catch (t : Throwable){

        }
        if((firebaseAuth!!.currentUser != null) ||(OverAllStaticConstants.isAnonymous == true))
        {
            Log.i("justATest","lskkf")
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }

        manager = fragmentManager
        var fragment : Fragment? = manager!!.findFragmentById(R.id.SignUpReplaceFrame)
        if(fragment == null){
            fragment = ChooseEnter()
            manager!!.beginTransaction().replace(R.id.SignUpReplaceFrame,fragment).commit()
        }
    }

    fun changeFragmentstart(key : String){
        when (key){
            "SignUpChoose"->{
                var fragment = SignIn()
                manager!!.beginTransaction().replace(R.id.SignUpReplaceFrame,fragment).addToBackStack(null).commit()
            }
            "LogInChoose"->{
                var fragment = logIn()
                manager!!.beginTransaction().replace(R.id.SignUpReplaceFrame,fragment).addToBackStack(null).commit()
            }
            "skipChoose"->{
                OverAllStaticConstants.isAnonymous = true
                val settings = applicationContext.getSharedPreferences("settings", 0)
                val editor = settings.edit()
                editor.putInt("isAnonymous", 1)
                editor.apply()
                startActivity(Intent(this, StartActivity::class.java))
                finish()
            }
            "ChooseScreen"->{
                var fragment = ChooseEnter()
                popStack()
                manager!!.beginTransaction().replace(R.id.SignUpReplaceFrame,fragment).commit()
            }
            "Login"->{
                startActivity(Intent(this, StartActivity::class.java))
                finish()
            }
        }
    }

    fun popStack()
    {
        for (i in 0 until manager!!.backStackEntryCount) {
            manager!!.popBackStack()
        }
    }
}
