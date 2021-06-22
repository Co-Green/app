package com.skku.cogreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class mypage1 : Fragment() {
    var date:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date=it.getInt("continuos")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.fragment_mypage1, container, false)
        val dateview= view.findViewById<TextView>(R.id.dateview)
        dateview.text="${date}일째"

        return view
    }

}