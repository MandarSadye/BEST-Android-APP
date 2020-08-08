package com.mandarsadye.mandar.ess_user.Fragments.SearchPackage


import android.os.Bundle
import android.support.v4.app.Fragment
import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.Adapter.SearchAdapter
import com.mandarsadye.mandar.ess_user.DataServices.BusNumberData
import com.mandarsadye.mandar.ess_user.Model.BusNumbers

import com.mandarsadye.mandar.ess_user.R
import kotlinx.android.synthetic.main.app_bar_start.*


/**
 * A simple [Fragment] subclass.
 * Use the [SearchLoader.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchLoader : Fragment() {

    var activityj : StartActivity? = null
    var callinFrag : SearchPanel? = null
    var adapter : SearchAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityj = activity as StartActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_search_loader, container, false)



        var recyclerview : RecyclerView = view.findViewById(R.id.recyclerSearch)
        recyclerview.setHasFixedSize(true)

        adapter  = SearchAdapter(BusNumberData.ourInstance.getBusNumbers(),activityj as Context,activityj!!)
        recyclerview.adapter = adapter
        callinFrag!!.adapter = adapter

        var layoutManager = myLinearLayout(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerview.layoutManager = layoutManager

        return view
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(callinFragment: SearchPanel): SearchLoader {
            val fragment = SearchLoader()
            fragment.callinFrag =  callinFragment
            return fragment
        }
    }

    class myLinearLayout(contect : Context ):LinearLayoutManager(contect){
        override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
            return RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }



}
