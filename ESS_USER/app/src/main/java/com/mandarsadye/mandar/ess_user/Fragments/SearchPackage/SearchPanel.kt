package com.mandarsadye.mandar.ess_user.Fragments.SearchPackage

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.CustomClasses.OverAllStaticConstants

import com.mandarsadye.mandar.ess_user.R
import android.util.DisplayMetrics
import com.mandarsadye.mandar.ess_user.Adapter.SearchAdapter
import com.mandarsadye.mandar.ess_user.DataServices.BusNumberData
import com.mandarsadye.mandar.ess_user.Model.BusNumbers


class SearchPanel : Fragment() {

    var activityi : StartActivity? = null
    var editSearchBar : EditText? = null
    var adapter : SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityi = activity as StartActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view  = inflater.inflate(R.layout.fragment_search_panel, container, false)



        var fm : FragmentManager =  activityi!!.manager!!
        var searchLoader : SearchLoader = SearchLoader.newInstance(this)
        fm.beginTransaction().replace(R.id.searchreplacement,searchLoader).commit()

        editSearchBar = view.findViewById(R.id.SearchBar)
        editSearchBar!!.addTextChangedListener(SearchTextChange(activityi!!,this))

        return view
    }

}
class SearchTextChange(activityi: StartActivity,callinFragment: SearchPanel) : TextWatcher{
    var activity = activityi
    val callinFragment = callinFragment

    override fun afterTextChanged(s: Editable?) {
        if (s.toString().length == 0) {
            callinFragment.adapter!!.swapData(BusNumberData.ourInstance.getBusNumbers())
            return
        }
        var temp = ArrayList<BusNumbers>()
        for (i in BusNumberData.ourInstance.getBusNumbers()){
            if (i.Number!!.toLowerCase().contains(s.toString().toLowerCase()))
                temp.add(i)
        }
        callinFragment.adapter!!.swapData(temp)

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

}
