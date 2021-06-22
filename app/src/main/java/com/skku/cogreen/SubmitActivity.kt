package com.skku.cogreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SubmitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)

        var intent=intent
        var date=intent.getIntExtra("date",0)
        var title=intent.getStringExtra("title")

        var dateview=findViewById<TextView>(R.id.date)
        var titleview=findViewById<TextView>(R.id.missiontitle)
        var btn=findViewById<Button>(R.id.button)

        dateview.text="Day ${date}"
        titleview.text=title

        btn.setOnClickListener {
            val mintent= Intent(this, MyPageActivity::class.java)
            startActivity(mintent)
        }
    }
}