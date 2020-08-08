package com.mandarsadye.mandar.ess_user.CustomClasses

import android.app.Activity
import com.mandarsadye.mandar.ess_user.R.id.btn_no
import com.mandarsadye.mandar.ess_user.R.id.btn_yes
import android.view.Window.FEATURE_NO_TITLE
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.app.Dialog
import android.app.FragmentManager
import android.content.Context
import android.support.v4.app.ActivityCompat.finishAffinity
import android.view.View
import android.view.Window
import android.widget.Button
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.Fragments.SearchPackage.Loading
import com.mandarsadye.mandar.ess_user.R


class LoadingDialogClass(c: Context,callingFragment: Loading?,a : StartActivity?)// TODO Auto-generated constructor stub
    : Dialog(c), android.view.View.OnClickListener {
    var d: Dialog? = null
    var yes: Button? = null
    var no: Button? = null
    var a = a
    var parentFragment = callingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_dialog_reload_data)
        yes = findViewById(R.id.btn_yes) as Button
        no = findViewById(R.id.btn_no) as Button
        yes!!.setOnClickListener(this)
        no!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.getId()) {

            R.id.btn_yes -> parentFragment!!.downloadDataCont()
            R.id.btn_no -> {
                dismiss()
                finishAffinity(a)
                System.exit(0)
            }
            else -> {
            }
        }
        dismiss()
    }
}