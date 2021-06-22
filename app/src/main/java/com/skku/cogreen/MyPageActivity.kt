package com.skku.cogreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.annotations.SerializedName
import com.kakao.sdk.user.UserApiClient
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.Serializable


data class Userinfo(
        var name: String,
        var continuous: Int,
        var ranking: Int,
        var rankingPercent: Float,
        var isSolvedToday: Int,
)


data class UserinfoResponse(
        var userInfo: Userinfo,
        var solvedMissions: List<Mission>,
        var isSuccess: Boolean,
        var code: Int,
        var message: String,
)

data class Mission(
        var title:String,
        var date:String,
):Serializable

interface UserinfoAPI{
    @GET("user")
    fun getUserinfo(
            @Header("x-access-token") jwt: String,
    ): Call<UserinfoResponse>
}

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        val greeting=findViewById<TextView>(R.id.greeting)
        val frag:FragmentManager=supportFragmentManager
        var ViewPager=findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.viewPager2)

        val Btn=findViewById<Button>(R.id.button)


        val BaseUrl="http://3.36.148.225:3000/"
        val retrofit= Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api=retrofit.create(UserinfoAPI::class.java)
        val getapi=api.getUserinfo(GlobalApplication.prefs.token.toString())
        Log.d("USER_SESSION",GlobalApplication.prefs.token.toString())

        // api로 정보 받아오기기
         getapi.enqueue(object : Callback<UserinfoResponse>{
            override fun onResponse(call: Call<UserinfoResponse>, response: Response<UserinfoResponse>) {
                if(response.isSuccessful()){
                    var result= response.body()!!
                    Log.d("USER_SESSION",response.body().toString())
                    greeting.text="${result.userInfo.name}님 안녕하세요!"
                    if(result.userInfo.isSolvedToday==1){
                        Btn.text="오늘의 미션 완료"
                        Btn.setEnabled(false)
                    }

                    var adapter=ViewAdapter(this@MyPageActivity, result.userInfo.continuous,result.userInfo.ranking,result.userInfo.rankingPercent,result.solvedMissions)
                    ViewPager.adapter=adapter
                }
                else{
                    Log.d("USER_SESSION", "실패 : ${response.raw()}")
                }
            }
            override fun onFailure(call: Call<UserinfoResponse>, t: Throwable) {
                Log.d("USER_SESSION", "실패 : ${t}")
            }

             inner class ViewAdapter(fa:FragmentActivity, continuos: Int, ranking: Int,rankingPercentage: Float,solvedMissions: List<Mission>):FragmentStateAdapter(fa){
                 var continuos:Int=continuos
                 var ranking1:Int=ranking
                 var rankingPercentage1:Float=rankingPercentage
                 var solvedMissions:List<Mission> = solvedMissions

                 override fun getItemCount(): Int {
                     return Integer.MAX_VALUE
                 }

                 override fun createFragment(position: Int): Fragment {

                     when(position%2){
                         0->return mypage1().apply {
                             arguments=Bundle().apply {
                                 putInt("continuos",continuos)
                             }
                         }
                         1->return mypage2().apply {
                             arguments=Bundle().apply {
                                 putInt("ranking",ranking1)
                                 putFloat("rankingPercentage",rankingPercentage1)
                                 putSerializable("solvedMissions",solvedMissions as Serializable)
                             }

                             Log.d("USER_SESSION","${ranking} ${rankingPercentage}")
                         }
                         else->return mypage1()
                     }
                 }
             }

        })


        Btn.setOnClickListener {
            if(GlobalApplication.prefs.submit==false){
                Log.d("PREF","${GlobalApplication.prefs.missionIndex}  ${GlobalApplication.prefs.submit}")

                val intent= Intent(this, MissionActivity::class.java)
                startActivity(intent)
            }
            else{

                Log.d("PREF","${GlobalApplication.prefs.token} ${GlobalApplication.prefs.missionIndex} ${GlobalApplication.prefs.submit}")
                val intent= Intent(this, StateActivity::class.java)
                startActivity(intent)
            }
        }


    }
}




//            UserApiClient.instance.logout { error ->
//                if (error != null) {
//                    Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
//                }else {
//                    Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
//                }
//            }