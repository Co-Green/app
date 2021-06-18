package com.skku.cogreen

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


data class Userinfo(
        var name: String,
        var continuos: Int,
        var ranking: Int,
        var rankingPercentage: Float,
        var isSolvedToday: Boolean,
)

data class Solvedmissions(
        var title: String,
        var date: String
)

data class UserinfoResponse(
        var userInfo: Userinfo,
        var solvedMissions: List<Userinfo>,
        var isSuccess: Boolean,
        var code: Int,
        var message: String,
)


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

        var ViewPager=findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.viewPager2)
        var adapter=ViewAdapter(this)
        ViewPager.adapter=adapter


        val BaseUrl="http://3.36.148.225:3000/"
        val retrofit= Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api=retrofit.create(UserinfoAPI::class.java)
        val getapi=api.getUserinfo(GlobalApplication.prefs.token.toString())

        // api로 정보 받아오기기
         getapi.enqueue(object : Callback<UserinfoResponse>{
            override fun onResponse(call: Call<UserinfoResponse>, response: Response<UserinfoResponse>) {
                if(response.isSuccessful()){
                    val result: UserinfoResponse? =response.body()
                    Log.d("USER_SESSION",response.body().toString())

                }
                else{
                    Log.d("USER_SESSION", "실패 : ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<UserinfoResponse>, t: Throwable) {
                Log.d("USER_SESSION", "실패 : ${t}")
            }

        })


    }
}

class ViewAdapter(fa:FragmentActivity):FragmentStateAdapter(fa){
    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }

    override fun createFragment(position: Int): Fragment {
        when(position%2){
            0->return mypage1()
            1->return mypage2()
            else->return mypage1()
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