package com.skku.cogreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

data class stateResponse(
        var missionIndex:Int,
        var isSuccess:Boolean,
        var code:Int,
        var message:String,
)

interface stateAPI{
    @FormUrlEncoded
    @POST("questions")
    fun postState(
            @Header("x-access-token") jwt: String,
            @Field("answer1") answer1: Int,
            @Field("answer2") answer2: Int,
            @Field("answer3") answer3: Int,
            @Field("answer4") answer4: Int,
            @Field("answer5") answer5: Int,
    ): Call<stateResponse>
}

class StateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)

        val button=findViewById<Button>(R.id.button)
        val intent= Intent(this, MissionActivity::class.java)

        button.setOnClickListener {
            val BaseUrl="http://3.36.148.225:3000/"
            val retrofit= Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val api=retrofit.create(stateAPI::class.java)


            val stateapi=api.postState(GlobalApplication.prefs.token.toString(),3,3,3,3,3)
            stateapi.enqueue(object :Callback<stateResponse>{
                override fun onResponse(call: Call<stateResponse>, response: Response<stateResponse>) {
                    if(response.isSuccessful()){
                        val result: stateResponse? =response.body()
                        Log.d("STATE_SESSION",response.body().toString())
                        if (result != null) {
                            intent.putExtra("missionIndex",result.missionIndex)
                        }
                        startActivity(intent)
                    }
                    else{
                        Log.d("STATE_SESSION","실패 ${response.body().toString()}")
                    }
                }

                override fun onFailure(call: Call<stateResponse>, t: Throwable) {

                    Log.d("STATE_SESSION","실패 ${t}")
                    TODO("Not yet implemented")
                }
            })


        }
    }
}