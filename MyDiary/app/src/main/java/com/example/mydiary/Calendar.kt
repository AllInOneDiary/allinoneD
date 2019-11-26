package com.example.mydiary

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.calendar.view.*

class Calendar : Fragment() {
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
                    R.id.schedule ->
                        Toast.makeText(
                            getContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                    R.id.diary -> {
                        Toast.makeText(
                            getContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                        val i = Intent(getContext(), DiaryList::class.java)
                        i.putExtra("date", msg)
                        startActivity(i)
                    }
                    R.id.emotion ->
                        Toast.makeText(
                            getContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                }
                true
            })
            popupMenu.show()
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show() //날짜 터치시 알림표시로 나타내기
        }


        return inf
    }


}
