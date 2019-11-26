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
    var re:Long=0
    var result =""
    lateinit var root :DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emotion_select)



        val c = java.util.Calendar.getInstance()
        val year = c.get(java.util.Calendar.YEAR)
        val month = c.get(java.util.Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        root = FirebaseDatabase.getInstance().reference.child("Emotion")

        imageButton.setOnClickListener{
            name = "angry"

            Change()

            Toast.makeText(this, "angry", Toast.LENGTH_SHORT).show()

        }
        imageButton2.setOnClickListener{
            name = "sad"
            Change()

            Toast.makeText(this, "sad", Toast.LENGTH_SHORT).show()

        }
        imageButton3.setOnClickListener{
            name = "confused"
            Change()

            Toast.makeText(this, "confused", Toast.LENGTH_SHORT).show()

        }
        imageButton4.setOnClickListener{
            name ="happy"
            Change()
            Toast.makeText(this, "happy", Toast.LENGTH_SHORT).show()

        }
        imageButton5.setOnClickListener{
            name ="love"
            Change()

            Toast.makeText(this, "love", Toast.LENGTH_SHORT).show()
        }

    }
    fun Change(){
        root.addListenerForSingleValueEvent(object:ValueEventListener {
            override fun onCancelled(p: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p: DataSnapshot) {
                result=""
                re=0
                result = p.child(name).getValue().toString()
                re = (result.toInt()+1).toLong()
                Toast.makeText(this@EmotionSe, re.toString(), Toast.LENGTH_SHORT).show()

            }

        })
        root.child(name).setValue(re)
    }
}