package com.example.mydiary

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
    var sad:Long=0
    var happy:Long=0
    var result:Long=0
    private lateinit var emotion: EmotionModel
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
            Change(name)

            Toast.makeText(this, "angry", Toast.LENGTH_SHORT).show()

        }
        imageButton2.setOnClickListener{
            name = "sad"
            Change(name)

            Toast.makeText(this, "sad", Toast.LENGTH_SHORT).show()

        }
        imageButton3.setOnClickListener{
            name = "confused"
            Change(name)

            Toast.makeText(this, "confused", Toast.LENGTH_SHORT).show()

        }
        imageButton4.setOnClickListener{
            name ="happy"
            Change(name)

            Toast.makeText(this, "happy", Toast.LENGTH_SHORT).show()

        }
        imageButton5.setOnClickListener{
            name ="love"
            Change(name)

            Toast.makeText(this, "love", Toast.LENGTH_SHORT).show()
        }

        root.addListenerForSingleValueEvent(object:ValueEventListener {
            override fun onCancelled(p: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p: DataSnapshot) {
                emotion = p.getValue(EmotionModel::class.java)!!
            }
        })
    }
    fun Change(name:String){
        when(name) {
            "angry" -> emotion.angry++
            "confused" -> emotion.confused++
            "happy" -> emotion.happy++
            "love" -> emotion.love++
            "sad" -> emotion.sad++
        }
        Toast.makeText(this@EmotionSe, emotion.toString(), Toast.LENGTH_SHORT).show()
        root.setValue(emotion)

//        if(name.equals("sad")){
//            sad=result.toInt().toLong()+1
//            root.child(name).setValue(sad)
//            Toast.makeText(this@EmotionSe, sad.toString(), Toast.LENGTH_SHORT).show()
//        }
//        else if(name.equals("happy")){
//            happy=result.toInt().toLong()+1
//            root.child(name).setValue(happy)
//            Toast.makeText(this@EmotionSe, happy.toString(), Toast.LENGTH_SHORT).show()
//        }



    }
}