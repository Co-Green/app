package com.skku.cogreen

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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


        val BaseUrl="http://3.36.148.225:3000/"
        val retrofit= Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api=retrofit.create(UserinfoAPI::class.java)
        val getapi=api.getUserinfo(GlobalApplication.prefs.token.toString())

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



        var btn=findViewById<Button>(R.id.button)
        btn.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}