package com.example.mydiary

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mydiary.ui.main.DiaryListViewAdapter
import com.example.mydiary.ui.main.DiaryListViewItem
import com.google.android.gms.actions.ReserveIntents
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.diary_list.*
import kotlinx.android.synthetic.main.diary_list.view.*

class DiaryList : Fragment() {
    @SuppressLint("ResourceType")
    lateinit var root: DatabaseReference
    lateinit var yearSpinner: Spinner
    lateinit var monthSpinner: Spinner
    lateinit var yearAdapter: Adapter
    lateinit var monthAdapter: Adapter
    lateinit var diarySearch : ListView

    var diaryList = arrayListOf<DiaryListViewItem>()
    lateinit var diaryadapter:DiaryListViewAdapter
    lateinit var diaryRoot: DatabaseReference

    var yearMonth: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inf = inflater.inflate(R.layout.diary_list, container, false)
        root = FirebaseDatabase.getInstance().reference
            .child(UserModel.uid).child("Diary")
        yearSpinner = inf.spinner_year
        yearAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.date_year,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            yearSpinner.adapter = adapter
        }
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> yearMonth = "2016"
                    1 -> yearMonth = "2017"
                    2 -> yearMonth = "2018"
                    3 -> yearMonth = "2019"
                    4 -> yearMonth = "2020"
                }
                Toast.makeText(context, yearMonth, Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                yearMonth = "20161"
            }
        }

        monthSpinner = inf.spinner_month
        monthAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.date_month,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            monthSpinner.adapter = adapter
        }
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                yearMonth += position + 1
                Toast.makeText(context, yearMonth, Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                yearMonth = "20161"
            }
        }

        ///////////////////////////////////////////////////////////////
        diaryadapter = DiaryListViewAdapter(context, diaryList)
        diarySearch = inf.diarylistview
        diarySearch.adapter = diaryadapter
         ///////////////////////////////////////////////////////////////


        // click button
        inf.search_diary.setOnClickListener(View.OnClickListener {
            Toast.makeText(context, yearMonth, Toast.LENGTH_SHORT).show()

            ////////////////////////////////////////////////////////////


            diaryRoot = FirebaseDatabase.getInstance().reference.child(UserModel.uid).child("Diary").child(yearMonth)

            diaryRoot.addValueEventListener(object: ValueEventListener{
                @Override
                override fun onDataChange(dataSnapshot: DataSnapshot) { // 201911
                    diaryList.clear()/*
                    diaryadapter.notifyDataSetInvalidated()*/

                    /*var i : Int = 0*/
                    for(child in dataSnapshot.children){ // 1, 2, ...
                        val title = child.child("title").value.toString()
                        val date = yearMonth
                        val content = child.child("contents").value.toString()
                        Log.i("title",title)
                        Log.i("date", date)
                        Log.i("content", content)

                        diaryList.add(DiaryListViewItem(title, content, date))
                        Toast.makeText(context, diaryList.get(0).title, Toast.LENGTH_LONG).show()
                    }
                    diaryadapter.notifyDataSetChanged()

                    //Toast.makeText(context, "짜잔!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show()
                }

                @Override
                override fun onCancelled(p0: DatabaseError) {}
            })
            ////////////////////////////////////////////////////////////
        })
        return inf
    }
}

