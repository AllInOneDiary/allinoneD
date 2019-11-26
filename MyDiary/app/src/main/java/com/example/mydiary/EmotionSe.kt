package com.example.mydiary

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.emotion_select.*
import java.util.Calendar

class EmotionSe: AppCompatActivity() {
    var date = ""
    var name =" "
    var re=0
    var result =""
    lateinit var root :DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emotion_select)

        val mPickTimeBtn = findViewById<Button>(R.id.centerView)
        val textView = findViewById<TextView>(R.id.dateTv)

        val c = java.util.Calendar.getInstance()
        val year = c.get(java.util.Calendar.YEAR)
        val month = c.get(java.util.Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        mPickTimeBtn.setOnClickListener{
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, year, monthOfYear, dayOfMonth -> textView.setText(" "+year+"년" +(monthOfYear+1) +"월 "+dayOfMonth+"일 ")}, year,month,day)
            dpd.show()
        }

        root = FirebaseDatabase.getInstance().reference.child("Emotion")

        imageButton.setOnClickListener{
            name = "angry"
            Change()
            result=""
            Toast.makeText(this, "angry", Toast.LENGTH_SHORT).show()

        }
        imageButton2.setOnClickListener{
            name = "sad"
            Change()
            result=""
            Toast.makeText(this, "sad", Toast.LENGTH_SHORT).show()

        }
        imageButton3.setOnClickListener{
            name = "confused"
            Change()
            result=""
            Toast.makeText(this, "confused", Toast.LENGTH_SHORT).show()

        }
        imageButton4.setOnClickListener{
            name ="happy"
            Change()
            result=""
            Toast.makeText(this, "happy", Toast.LENGTH_SHORT).show()

        }
        imageButton5.setOnClickListener{
            name ="love"
            Change()
            result=""
            Toast.makeText(this, "love", Toast.LENGTH_SHORT).show()
        }

    }
    fun Change(){
        root.addListenerForSingleValueEvent(object:ValueEventListener {
            override fun onCancelled(p: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p: DataSnapshot) {
/*
                re++
*/
                result = p.child(name).getValue().toString()
                re =Integer.parseInt(result)+1
                Toast.makeText(this@EmotionSe, re, Toast.LENGTH_SHORT).show()

            }
        })
        root.child(name).setValue(re)
    }
}