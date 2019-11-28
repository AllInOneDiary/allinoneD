package com.example.mydiary

import android.annotation.SuppressLint
import android.content.Context.MODE_NO_LOCALIZED_COLLATORS
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.calendar.*
import kotlinx.android.synthetic.main.calendar.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class Calendar : Fragment() {

    var fname: String = ""
    var str: String = ""

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inf = inflater.inflate(R.layout.calendar, container, false)
        inf.calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->


            var month = month + 1

            val msg: String =
                year.toString() + "/" + month.toString() + "/" + dayOfMonth.toString()

            val popupMenu: PopupMenu = PopupMenu(getContext(), view)
            popupMenu.menuInflater.inflate(R.menu.option_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.schedule ->{
                        Toast.makeText(
                            getContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()

                        //달력 날짜 선택 후 일정 버튼을 누르면
                        calendarTextView.visibility = View.VISIBLE //해당 날짜가 뜨는 textView
                        save_Btn.visibility = View.VISIBLE  //저장 버튼이 Visible
                        contextEditText.visibility = View.VISIBLE //EditText가 Visible
                        textView2.visibility = View.INVISIBLE //저장된 textView가 invisible
                        cha_Btn.visibility = View.INVISIBLE //수정 버튼이 invisible
                        del_Btn.visibility = View.INVISIBLE //삭제 버튼이 invisible

                        diaryTextView.text = String.format("%d / %d / %d", year, month, dayOfMonth)
//날짜를 보여주는 텍스트에 해달 날짜를 넣는다.
                        contextEditText.setText("")//EditText에 공백값 넣

                        checkedDay(year, month, dayOfMonth) //checkedDay 메소드 호출
                    }

                    R.id.diary -> {
                        Toast.makeText(
                            getContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                        val i = Intent(getContext(), WriteDiary::class.java)
                        i.putExtra("date", msg)
                        startActivity(i)
                    }
                    R.id.emotion ->{
                        Toast.makeText(
                            getContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(getContext(), EmotionSe::class.java)
                        startActivity(intent)
                    }
                }
                true
            })


            calendarTextView.visibility = View.INVISIBLE
            save_Btn.visibility = View.INVISIBLE
            contextEditText.visibility = View.INVISIBLE
            popupMenu.show()
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show() //날짜 터치시 알림표시로 나타내기

            save_Btn.setOnClickListener {
                //저장 버튼이 클릭되면
                saveDiary(fname) //saveDiary 메소드 호출
                Toast.makeText(activity,fname + "데이터를 저장했습니다.",Toast.LENGTH_LONG).show()
                str = contextEditText.getText().toString() // str 변수에 edittext내용을 toString
//형으로 저장
                textView2.text = "${str}" // textView에 str 출력
                save_Btn.visibility = View.INVISIBLE
                cha_Btn.visibility = View.VISIBLE
                del_Btn.visibility = View.VISIBLE
                contextEditText.visibility = View.INVISIBLE
                textView2.visibility = View.VISIBLE

            }

        }

        return inf
    }

    fun checkedDay(cYear: Int, cMonth: Int, cDay: Int) {
        fname = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"
// 저장할 파일 이름 설정. Ex) 2019-01-20.txt
        var fis: FileInputStream? = null // FileStream fis 변수 설정

        try {
            fis = activity?.openFileInput(fname) // fname 파일 오픈!!

            val fileData = fis?.available()?.let { ByteArray(it) } // fileData에 파이트 형식
//으로 저장

            fis?.read(fileData)
// fileData를 읽음
            fis?.close()


            str = fileData?.let { String(it) }.toString() // str 변수에 fileData를 저장

            contextEditText.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE
            textView2.text = "${str}" // textView에 str 출력

            save_Btn.visibility = View.INVISIBLE
            cha_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE

            cha_Btn.setOnClickListener {
                // 수정 버튼을 누를 시
                contextEditText.visibility = View.VISIBLE
                textView2.visibility = View.INVISIBLE
                contextEditText.setText(str) // editText에 textView에 저장된
// 내용을 출력
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
                Toast.makeText(activity,fname + "데이터를 삭제했습니다.",Toast.LENGTH_LONG).show()
            }

            if (textView2.getText() == "") {
                textView2.visibility = View.INVISIBLE
                diaryTextView.visibility = View.VISIBLE
                save_Btn.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                contextEditText.visibility = View.VISIBLE
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @SuppressLint("WrongConstant")
    fun saveDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = activity?.openFileOutput(readyDay,MODE_NO_LOCALIZED_COLLATORS) // MODE_NO_LOCALIZED_COLLATORS
            var content: String = contextEditText.getText().toString()
            fos?.write(content.toByteArray())
            fos?.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("WrongConstant")
    fun removeDiary(readyDay: String) {
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
}