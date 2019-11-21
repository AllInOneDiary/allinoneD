package com.example.mydiary

import android.annotation.SuppressLint
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
        val inf=inflater.inflate(R.layout.calendar, container, false)
        inf.calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var month = month +1
            val msg: String = year.toString()+"/"+ month.toString() + "/"+dayOfMonth.toString()
/*            val inf1=inflater.inflate(R.menu.option_menu, container, false)
            inf1.setOnClickListener { v:View ->
                if(v.id ==R.id.schedule){
                    Toast.makeText(getContext(), "schedule", Toast.LENGTH_SHORT).show()
                }
                if(v.id==R.id.diary){
                    Toast.makeText(getContext(), "diary", Toast.LENGTH_SHORT).show()
                }
                if(v.id==R.id.emotion){
                    Toast.makeText(getContext(), "emotion", Toast.LENGTH_SHORT).show()
                }

            }*/
            val popupMenu: PopupMenu = PopupMenu(getContext(),view)
            popupMenu.menuInflater.inflate(R.menu.option_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.schedule ->
                        Toast.makeText(
                            getContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                    R.id.diary ->
                        Toast.makeText(
                            getContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
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

       /* calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var month = month +1
            val msg: String = year.toString()+"/"+ month.toString() + "/"+dayOfMonth.toString()
        }*/
    }
/*
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item!!.itemId
        if(id==R.id.schedule){
            Toast.makeText(getContext(), "schedule", Toast.LENGTH_SHORT).show()
        }
        if(id==R.id.diary){
            Toast.makeText(getContext(), "diary", Toast.LENGTH_SHORT).show()
        }
        if(id==R.id.emotion){
            Toast.makeText(getContext(), "emotion", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }*/
}