package com.skku.cogreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

data class missionInfo(
        var day:Int,
        var missionIndex:Int,
        var title:String,
        var description:String,
        var question1:String,
        var question2:String,
        var question3:String,
        var answer1:String,
        var answer2:String,
        var answer3:String,
)

data class missionResponse(
        var isSuccess:Boolean,
        var code:Int,
        var message:String,
        var result:missionInfo,

)

interface missionAPI{
    @GET("missions/{id}?")
    fun getMission(
        @Header("x-access-token") jwt: String,
        @Path("id") id:Int,
    ): Call<missionResponse>
}

data class resultInfo(
        var day:Int,
        var missionIndex:Int,
        var title:String
)

data class submitResponse(
        var isSuccess: Boolean,
        var code: Int,
        var message:String,
        var result:resultInfo,
)

interface submitAPI{
    @FormUrlEncoded
    @PATCH("missions/{id}?")
    fun patchSubmit(
            @Header("x-access-token") jwt:String,
            @Field("answer1") answer1: String,
            @Field("answer2") answer2: String,
            @Field("answer3") answer3: String,
            @Path("id") id:Int,
            @Query("temporary") temporary:String,
    ):Call<submitResponse>
}


class MissionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission)

        var date=findViewById<TextView>(R.id.date)
        var missiontitle=findViewById<TextView>(R.id.missiontitle)
        var description=findViewById<TextView>(R.id.description)
        var q1=findViewById<TextView>(R.id.q1)
        var q2=findViewById<TextView>(R.id.q2)
        var q3=findViewById<TextView>(R.id.q3)
        var a1=findViewById<EditText>(R.id.a1)
        var a2=findViewById<EditText>(R.id.a2)
        var a3=findViewById<EditText>(R.id.a3)

        var intent1= Intent(this,SubmitActivity::class.java)
        var button=findViewById<Button>(R.id.button)
        var button2=findViewById<Button>(R.id.button2)

        var missionIndex=GlobalApplication.prefs.missionIndex
        Log.d("MISSION_SESSION","미션 인덱스 : ${missionIndex.toString()}")


        val BaseUrl="http://3.36.148.225:3000/"
        val retrofit= Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val mapi=retrofit.create(missionAPI::class.java)
        val missionapi= missionIndex?.let { mapi.getMission(GlobalApplication.prefs.token.toString(), it) }

         //api로 정보 받아오기

        if (missionapi != null) {
            missionapi.enqueue(object:Callback<missionResponse>{
                override fun onResponse(call: Call<missionResponse>, response: Response<missionResponse>) {
                    if(response.isSuccessful()){
                        val body: missionResponse? =response.body()
                        Log.d("MISSION_SESSION",response.body().toString())

                        if (body != null) {
                            intent1.putExtra("date",body.result.day)
                            intent1.putExtra("title",body.result.title)

                            date.text="Day ${body.result.day}"
                            missiontitle.text=body.result.title
                            description.text=body.result.description

                            q1.text=body.result.question1
                            q2.text=body.result.question2
                            q3.text=body.result.question3

                            a1.setText(body.result.answer1)
                            a2.setText(body.result.answer2)
                            a3.setText(body.result.answer3)
                        }
                    } else{
                        Log.d("MISSION_SESSION", "실패 : ${response.raw()}")
                    }
                }

                override fun onFailure(call: Call<missionResponse>, t: Throwable) {
                    Log.d("MISSION_SESSION", "실패 : ${t}")
                }
            })
        }


        button.setOnClickListener {
            val sapi=retrofit.create(submitAPI::class.java)
            val submitAPI= missionIndex?.let { it1 ->
                sapi.patchSubmit(GlobalApplication.prefs.token.toString(),
                        a1.text.toString(),a2.text.toString(),a3.text.toString(),
                        it1, "false")
            }
            if (submitAPI != null) {
                submitAPI.enqueue(object :Callback<submitResponse>{
                    override fun onResponse(call: Call<submitResponse>, response: Response<submitResponse>) {
                        if(response.isSuccessful()){
                            val result: submitResponse? =response.body()
                            Log.d("MISSION_SESSION",response.body().toString())
                        }
                        else{
                            Log.d("MISSION_SESSION", "실패 : ${response.raw()}")
                        }
                    }

                    override fun onFailure(call: Call<submitResponse>, t: Throwable) {
                        Log.d("SUBMIT_SESSION","실패 ${t}")
                    }
                })
            }
            GlobalApplication.prefs.submit=true
            startActivity(intent1)
        }

        button2.setOnClickListener {

            var intent2=Intent(this,MainActivity::class.java)
            val sapi=retrofit.create(submitAPI::class.java)
            val submitAPI= missionIndex?.let { it1 ->
                sapi.patchSubmit(GlobalApplication.prefs.token.toString(),
                        a1.text.toString(),a2.text.toString(),a3.text.toString(),
                        it1, "true")
            }
            if (submitAPI != null) {
                submitAPI.enqueue(object :Callback<submitResponse>{
                    override fun onResponse(call: Call<submitResponse>, response: Response<submitResponse>) {
                        if(response.isSuccessful()){
                            val result: submitResponse? =response.body()
                            Log.d("MISSION_SESSION",response.body().toString())
                        }
                        else{
                            Log.d("MISSION_SESSION", "실패 : ${response.raw()}")
                        }
                    }

                    override fun onFailure(call: Call<submitResponse>, t: Throwable) {
                        Log.d("SUBMIT_SESSION","실패 ${t}")
                    }
                })
            }
            GlobalApplication.prefs.submit=false
            startActivity(intent2)
        }

    }
}