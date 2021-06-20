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
            Log.d("FRAG_SESSION", date.toString())

        }
        val dateview= view?.findViewById<TextView>(R.id.dateview)
        dateview?.setText("${date}일째")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dateview= view?.findViewById<TextView>(R.id.dateview)
        dateview?.setText("${date}일째")

        return inflater.inflate(R.layout.fragment_mypage1, container, false)
    }

}