package com.skku.cogreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
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
        val rg1=findViewById<RadioGroup>(R.id.rg1)
        val rg2=findViewById<RadioGroup>(R.id.rg2)
        val rg3=findViewById<RadioGroup>(R.id.rg3)
        val rg4=findViewById<RadioGroup>(R.id.rg4)
        val rg5=findViewById<RadioGroup>(R.id.rg5)

        val b1=findViewById<Button>(R.id.b1)
        val b2=findViewById<Button>(R.id.b2)
        val b3=findViewById<Button>(R.id.b3)
        val b4=findViewById<Button>(R.id.b4)
        val b5=findViewById<Button>(R.id.b5)

        var answer1:Int=3
        var answer2:Int=3
        var answer3:Int=3
        var answer4:Int=3
        var answer5:Int=3

        b1.setOnClickListener {
            rg1.clearCheck()
        }
        b2.setOnClickListener {
            rg2.clearCheck()
        }
        b3.setOnClickListener {
            rg3.clearCheck()
        }
        b4.setOnClickListener {
            rg4.clearCheck()
        }
        b5.setOnClickListener {
            rg5.clearCheck()
        }

        rg1.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb11-> answer1=1
                R.id.rb12-> answer1=2
                R.id.rb13-> answer1=3
                R.id.rb14-> answer1=4
                R.id.rb15-> answer1=5
                else-> answer1=3
            }
        }
        rg2.setOnCheckedChangeListener { group, checkedId ->
            answer2=when(checkedId){
                R.id.rb21-> 1
                R.id.rb22-> 2
                R.id.rb23-> 3
                R.id.rb24-> 4
                R.id.rb25-> 5
                else -> 3
            }
        }
        rg3.setOnCheckedChangeListener { group, checkedId ->
            answer3=when(checkedId){
                R.id.rb31-> 1
                R.id.rb32-> 2
                R.id.rb33-> 3
                R.id.rb34-> 4
                R.id.rb35-> 5
                else -> 3
            }
        }
        rg4.setOnCheckedChangeListener { group, checkedId ->
            answer4=when(checkedId){
                R.id.rb41-> 1
                R.id.rb42-> 2
                R.id.rb43-> 3
                R.id.rb44-> 4
                R.id.rb45-> 5
                else -> 3
            }
        }
        rg5.setOnCheckedChangeListener { group, checkedId ->
            answer5=when(checkedId){
                R.id.rb51-> 1
                R.id.rb52-> 2
                R.id.rb53-> 3
                R.id.rb54-> 4
                R.id.rb55-> 5
                else -> 3
            }
        }

        button.setOnClickListener {
            Log.d("STATE_SESSION", "${answer1.toString()},${answer2.toString()},${answer3.toString()},${answer4.toString()},${answer5.toString()}")
            postState(answer1,answer2,answer3,answer4,answer5)
        }
    }

    fun postState(answer1: Int,answer2: Int,answer3: Int,answer4: Int,answer5: Int) {

        val intent= Intent(this@StateActivity, MissionActivity::class.java)

        val BaseUrl="http://3.36.148.225:3000/"
        val retrofit= Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api=retrofit.create(stateAPI::class.java)
        val stateapi=api.postState(GlobalApplication.prefs.token.toString(),answer1, answer2, answer3, answer4, answer5)
        stateapi.enqueue(object :Callback<stateResponse>{
            override fun onResponse(call: Call<stateResponse>, response: Response<stateResponse>) {
                if(response.isSuccessful()){
                    val result: stateResponse? =response.body()
                    Log.d("STATE_SESSION",response.body().toString())
                    if (result != null) {
                        Log.d("STATE_SESSION","미션 인덱스 들어감 ${result.missionIndex}")
                        intent.putExtra("missionIndex",result.missionIndex)
                        startActivity(intent)
                    }
                }
                else{
                    Log.d("STATE_SESSION","실패 ${response.body().toString()}")
                }
            }

            override fun onFailure(call: Call<stateResponse>, t: Throwable) {

                Log.d("STATE_SESSION","실패 ${t}")

            }
        })
    }
}