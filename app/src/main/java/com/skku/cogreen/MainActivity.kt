package com.skku.cogreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

data class loginResponse(
        var jwt: String,
        var isSuccess: Boolean,
        var code: Int,
        var message: String,
)

interface loginAPI{
    @FormUrlEncoded
    @POST("user")
    fun postLogin(
            @Field("accessToken") accessToken: String
    ):Call<loginResponse>
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("PREF","${GlobalApplication.prefs.token} ${GlobalApplication.prefs.missionIndex} ${GlobalApplication.prefs.submit}")

        val button=findViewById<Button>(R.id.loginbutton)

        val BaseUrl="http://3.36.148.225:3000/"
        val retrofit=Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api=retrofit.create(loginAPI::class.java)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("KAKAO_SESSION", "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i("KAKAO_SESSION", "로그인 성공 ${token.accessToken.toString()}")

                    val postapi=api.postLogin(token.accessToken.toString())
                    postapi.enqueue(object : Callback<loginResponse> {
                        override fun onResponse(
                                call: Call<loginResponse>,
                                response: Response<loginResponse>
                        ) {

                            if(response.isSuccessful()){
                                val result: loginResponse? =response.body()
                                Log.d("KAKAO_SESSION",response.body().toString())
                                if (result != null) {
                                    GlobalApplication.prefs.token=result.jwt
                                }
                                Log.d("KAKAO_SESSION","shared preference ${GlobalApplication.prefs.token.toString()}")
                                startActivity(Intent(this@MainActivity,MyPageActivity::class.java))

                            }
                            else{
                                Log.d("KAKAO_SESSION", "실패 : ${response.raw()}")
                            }
                        }

                        override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                            Log.d("KAKAO_SESSION", "실패 : ${t}")
                        }
                    })
                }
        }

        button.setOnClickListener {
            LoginClient.instance.run {
                //카카오톡 있으면 그걸로 로그인 ㄱ
                if (isKakaoTalkLoginAvailable(this@MainActivity)) {
                    loginWithKakaoTalk(this@MainActivity, callback = callback)
                }
                //카카오톡 없으면 계정으로 로그인 ㄱ
                else {
                    loginWithKakaoAccount(this@MainActivity, callback = callback)
                }
            }
        }


        if (GlobalApplication.prefs.token!=null){
            var handler = Handler()
            handler.postDelayed({
                val intent= Intent(this, MyPageActivity::class.java)
                startActivity(intent)
                finish()}, 2000)

            startActivity(intent)
        }
        else{
            button.setVisibility(View.VISIBLE)
        }

    }
}
