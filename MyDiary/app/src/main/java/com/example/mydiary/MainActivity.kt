package com.example.mydiary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var msg: String
            runOnUiThread {
                msg = year.toString() + "/" + (month + 1).toString() + "/" + dayOfMonth.toString()
                intent = Intent(this, DiaryList::class.java)
                intent.putExtra("date", msg)
                startActivity(intent)
            }

        }
    }
}