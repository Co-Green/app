package com.skku.cogreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//순위 -> int
//상위 퍼센트 -> float
// 수행 완료 미션 목록 (이름이랑 날짜) ->solvedMissions array로 받아와서


class mypage2 : Fragment() {
    var ranking:Int=1
    var rankingPercentage:Float= 0F
    lateinit var solvedmissions:List<Mission>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ranking=it.getInt("ranking")
            rankingPercentage=it.getFloat("rankingPercentage")
            solvedmissions=it.getSerializable("solvedMissions") as List<Mission>

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view:View=inflater.inflate(R.layout.fragment_mypage2, container, false)
        val rankview= view.findViewById<TextView>(R.id.ranking)
        val percentview=view.findViewById<TextView>(R.id.rankingPercentage)
        val recycler=view.findViewById<RecyclerView>(R.id.recycler)

        rankview.text="${ranking}위"
        percentview.text="상위 ${rankingPercentage}%"
//
//        var adapter=Adapter()
//        adapter.solvedmissions=solvedmissions
//        recycler.adapter=adapter

        Log.d("FRAG_SESSION",solvedmissions[0].title)

        return view
    }
}

class Adapter: RecyclerView.Adapter<Holder>() {
    lateinit var solvedmissions:List<Mission>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.mypage2_list,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val mission=solvedmissions.get(position)
        holder.setMission(mission)
    }

    override fun getItemCount(): Int {
        return solvedmissions.size
    }
}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setMission(mission:Mission){
        val title=itemView.findViewById<TextView>(R.id.title)
        val date=itemView.findViewById<TextView>(R.id.date)

        title.text=mission.title
        date.text=mission.date
    }
}