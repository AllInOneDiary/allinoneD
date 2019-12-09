package com.example.mydiary

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.mydiary.ui.main.DiaryListViewAdapter
import com.example.mydiary.ui.main.DiaryListViewItem
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.diary_list.view.*

class DiaryList : Fragment(){
    @SuppressLint("ResourceType")
    lateinit var root:DatabaseReference
    lateinit var yearSpinner:Spinner
    lateinit var monthSpinner:Spinner
    lateinit var yearAdapter : Adapter
    lateinit var monthAdapter : Adapter
    lateinit var diarySearch : ListView
    lateinit var diaryadapter:DiaryListViewAdapter
    lateinit var diaryRoot: DatabaseReference
    var date = ""
    var year:String=""
    var month:String=""
    var day=""
    val hashArray = ArrayList<Hash>() // 검색한 해시와 해시의 사진 url을 저장하는 배열
    var diaryList = arrayListOf<DiaryListViewItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inf = inflater.inflate(R.layout.diary_list, container, false)
        root = FirebaseDatabase.getInstance().reference.child(UserModel.uid).child("Diary")

        val hashAdapter = HashListAdatper(context, hashArray)
        inf.diarylistview.adapter = hashAdapter

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
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> year = "2016"
                    1 -> year = "2016"
                    2 -> year = "2017"
                    3 -> year = "2018"
                    4 -> year = "2019"
                    5 -> year = "2020"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                year="2016"
                month="1"
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
                month = position.toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                year = "2016"
                month = "1"
            }
        }

        diaryadapter = DiaryListViewAdapter(context, diaryList)
        diarySearch = inf.diarylistview
        diarySearch.adapter = diaryadapter
        // click button
        inf.search_diary.setOnClickListener(View.OnClickListener {
            diaryRoot = FirebaseDatabase.getInstance().reference.child(UserModel.uid).child("Diary")
            diaryRoot.addValueEventListener(object: ValueEventListener{
                @Override
                override fun onDataChange(dataSnapshot: DataSnapshot) { // 201911
                    if(dataSnapshot.hasChild(year+month)) {
                        var dataChild = dataSnapshot.child(year+month)
                        diaryList.clear()
                        for (child in dataChild.children) { // 1, 2, ...
                            day = child.key.toString()
                            val title = child.child("title").value.toString()
                            date = "${year}/${month}/${day}"

                            if(child.hasChild("title")){
                                diaryList.add(DiaryListViewItem(title, date))
                            }
                        }
                        diaryadapter.notifyDataSetChanged()
                    }
                    else
                        Toast.makeText(context, "해당 달의 일기 목록이 존재하지 않습니다", Toast.LENGTH_SHORT).show()
                }
                override fun onCancelled(p0: DatabaseError) {}
            })

        })

        diarySearch.setOnItemClickListener(MoveListener())

        return inf
    }
    inner class MoveListener : AdapterView.OnItemClickListener{
        @Override
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val intent = Intent(context, ViewDiary::class.java)
            val item = diaryList[position]
            intent.putExtra("date", item.date)
            startActivity(intent)
        }
    }

}