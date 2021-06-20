package com.skku.cogreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

//순위 -> int
//상위 퍼센트 -> float
// 수행 완료 미션 목록 (이름이랑 날짜) ->solvedMissions array로 받아와서


class mypage2 : Fragment() {
    var ranking:Int=1
    var rankingPercentage:Float= 0F
    lateinit var solvedmissions:List<List<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ranking=it.getInt("ranking")
            rankingPercentage=it.getFloat("rankingPercentage")
//            solvedmissions=it.getSerializable("solvedMissions") as List<Solvedmissions>

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage2, container, false)
    }

}