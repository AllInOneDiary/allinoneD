package com.example.mydiary

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val msg: String = year.toString() + month.toString() + dayOfMonth.toString()
            runOnUiThread {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() //날짜 터치시 알림표시로 나타내기
            }
        }
    }
}