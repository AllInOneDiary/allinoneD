package com.example.mydiary

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.putpicture.*


class DiaryList: AppCompatActivity() {
    var date = ""
    val putpicture = PutPicture()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diary)

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if(intent.hasExtra("date")) {
            date = intent.getStringExtra("date") // 2019/11/18 형식
            supportActionBar?.title = date // 날짜를 toolbar의 타이틀로 넣는다
        }

        var fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{moveToPricture()}
    }

    fun moveToPricture(){
        var intent = Intent(this, PutPicture::class.java)
        intent.putExtra("date", date)
        startActivity(intent)
    }
}