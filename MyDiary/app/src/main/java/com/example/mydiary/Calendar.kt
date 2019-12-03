package com.example.mydiary

import android.annotation.SuppressLint
import android.content.Context.MODE_NO_LOCALIZED_COLLATORS
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.add_schedule.*

import kotlinx.android.synthetic.main.add_schedule.view.*
import kotlinx.android.synthetic.main.calendar.*

import kotlinx.android.synthetic.main.calendar.view.*
import kotlinx.android.synthetic.main.emotion_select.*

import java.io.FileOutputStream
import java.util.*

data class Schedule(
    var comment : String = ""
)

class Calendar : Fragment() {
    lateinit var root: DatabaseReference
    var name = " "
    var fname: String = ""
    var str: String = ""

    lateinit var rootT: DatabaseReference
    lateinit var txtRef: DatabaseReference

    lateinit var SchRef: DatabaseReference

    var comment: String = ""


    var year = 0
    var month = 0
    var day = 0
    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        rootT = FirebaseDatabase.getInstance().reference.child(UserModel.uid)
        txtRef = FirebaseDatabase.getInstance().reference.child("text")

        val inf = inflater.inflate(R.layout.calendar, container, false)

        with(inf) {
            textMsg.setSelected(true)
            nextTXT.setOnClickListener {

                var listener = object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p1: DataSnapshot) {
                        val random = Random()
                        val num = random.nextInt(5)
                        val arr = arrayOfNulls<String>(5)
                        var i = 0
                        for (text in p1.children) {
                            arr[i] = text.value.toString()
                            i++
                        }
                        textMsg.setText(arr[num])
                    }
                }
                txtRef.addValueEventListener(listener)
            }
            schedule.visibility = View.VISIBLE


            calendarView?.setOnDateChangeListener { view, y, m, d ->
                emotion.visibility = View.GONE
                schedule.visibility = View.GONE

                month = m + 1
                year = y
                day = d
                val msg: String =
                    year.toString() + "/" + month.toString() + "/" + day.toString()

                val popupMenu = PopupMenu(getContext(), view)
                popupMenu.menuInflater.inflate(R.menu.option_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->

                    when (item.itemId) {
                        R.id.schedule -> {
                            //달력 날짜 선택 후 일정 버튼을 누르면
                            scheduleCheck()
                        }
                        R.id.diary -> {
                            val i = Intent(getContext(), WriteDiary::class.java)
                            i.putExtra("date", msg)
                            startActivity(i)
                        }
                        R.id.emotion -> {
                            Toast.makeText(
                                getContext(),
                                "You Clicked : " + item.title,
                                Toast.LENGTH_SHORT
                            ).show()
                            emotion.visibility = View.VISIBLE
                            schedule.visibility = View.GONE

                            emotionSelect()

                        }
                    }
                    true
                })

                with(schedule) {
                    diaryTextView.visibility = View.INVISIBLE
                    save_Btn.visibility = View.INVISIBLE
                    contextEditText.visibility = View.INVISIBLE
                    popupMenu.show()
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show() //날짜 터치시 알림표시로 나타내기

                    save_Btn.setOnClickListener {
                        //저장 버튼이 클릭되면
                        saveDiary(fname) //saveDiary 메소드 호출
                        Toast.makeText(activity, fname + "데이터를 저장했습니다.", Toast.LENGTH_LONG)
                            .show()
                        str = contextEditText.getText()
                            .toString() // str 변수에 edittext내용을 toString 형으로 저장
                        textView2.text = "${str}" // textView에 str 출력
                        save_Btn.visibility = View.INVISIBLE
                        cha_Btn.visibility = View.VISIBLE
                        del_Btn.visibility = View.VISIBLE
                        contextEditText.visibility = View.VISIBLE
                        textView2.visibility = View.VISIBLE

                    }
                    cha_Btn.setOnClickListener {
                        // 수정 버튼을 누를 시
                        contextEditText.visibility = View.VISIBLE
                        textView2.visibility = View.INVISIBLE
                        contextEditText.setText(str) // editText에 textView에 저장된 내용을 출력
                        save_Btn.visibility = View.VISIBLE
                        cha_Btn.visibility = View.INVISIBLE
                        del_Btn.visibility = View.INVISIBLE
                        textView2.text = "${contextEditText.getText()}"
                    }

                    del_Btn.setOnClickListener {
                        textView2.visibility = View.INVISIBLE
                        contextEditText.setText("")
                        contextEditText.visibility = View.VISIBLE
                        save_Btn.visibility = View.VISIBLE
                        cha_Btn.visibility = View.INVISIBLE
                        del_Btn.visibility = View.INVISIBLE
                        removeDiary(fname)
                        Toast.makeText(activity, fname + "데이터를 삭제했습니다.", Toast.LENGTH_LONG).show()
                    }

                }

            }
        }
        return inf
    }

    fun scheduleCheck(){
        val sche=rootT.child("Schedule").child("$year$month").child(day.toString())
        sche.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                schedule.visibility = View.VISIBLE
                if(p0.getValue()!=null){
                    val attach_txt=p0.getValue().toString()
                    contextEditText.setText(attach_txt)
                    with(schedule){

                        save_Btn.visibility = View.INVISIBLE  //저장 버튼이 Visible
                        cha_Btn.visibility = View.VISIBLE //수정 버튼이 invisible
                        del_Btn.visibility = View.VISIBLE //삭제 버튼이 invisible
                    }

                }
                else{
                    with(schedule){

                        save_Btn.visibility = View.VISIBLE  //저장 버튼이 Visible
                        cha_Btn.visibility = View.INVISIBLE //수정 버튼이 invisible
                        del_Btn.visibility = View.INVISIBLE //삭제 버튼이 invisible

                    }

                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        with(schedule){
            diaryTextView.visibility = View.VISIBLE //해당 날짜가 뜨는 textView

            contextEditText.visibility = View.VISIBLE //EditText가 Visible
            textView2.visibility = View.INVISIBLE //저장된 textView가 invisible

            diaryTextView.text = String.format(
                "%d / %d / %d",
                year,
                month,
                day
            ) //날짜를 보여주는 텍스트에 해달 날짜를 넣는다.
            contextEditText.setText("")//EditText에 공백값 넣기
        }
        emotion.visibility = View.GONE
    }

    @SuppressLint("WrongConstant")
    fun saveDiary(readyDay: String) {

        var dataInput=  Schedule(contextEditText.text.toString())
        var dR = rootT.child("Schedule").child("$year$month").child(day.toString())
        val text =dR.setValue(contextEditText.text.toString())
        contextEditText.text.clear()
        contextEditText.text.append(comment)
    }

    @SuppressLint("WrongConstant")
    fun removeDiary(readyDay: String) {

        SchRef = rootT.child("Schedule").child("$year$month").child(day.toString())
        SchRef.removeValue()
        var fos: FileOutputStream? = null
        try {
            fos = activity?.openFileOutput(readyDay, MODE_NO_LOCALIZED_COLLATORS)//MODE_PRIVATE
            var content: String = ""
            fos?.write(content.toByteArray())
            fos?.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun emotionSelect() {
        root = FirebaseDatabase.getInstance().reference.child(UserModel.uid)

        imageButton.setOnClickListener {
            name = "angry"
            Change(name)

            Toast.makeText(getContext(), "angry", Toast.LENGTH_SHORT).show()

        }
        imageButton2.setOnClickListener {
            name = "sad"
            Change(name)

            Toast.makeText(getContext(), "sad", Toast.LENGTH_SHORT).show()

        }
        imageButton3.setOnClickListener {
            name = "confused"
            Change(name)

            Toast.makeText(getContext(), "confused", Toast.LENGTH_SHORT).show()

        }
        imageButton4.setOnClickListener {
            name = "happy"
            Change(name)

            Toast.makeText(getContext(), "happy", Toast.LENGTH_SHORT).show()

        }
        imageButton5.setOnClickListener {
            name = "love"
            Change(name)

            Toast.makeText(getContext(), "love", Toast.LENGTH_SHORT).show()
        }

    }

    fun Change(name: String) {
        val emotionRef = root.child("Diary").child("$year$month").child("$day").child("emotion")
        emotionRef.setValue(name)
    }
}