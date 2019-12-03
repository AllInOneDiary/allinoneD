package com.example.mydiary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.diary_list.view.*

data class Hash(val hashtag:String, val url:String)
class DiaryList : Fragment(), AdapterView.OnItemSelectedListener {
    @SuppressLint("ResourceType")
    lateinit var root:DatabaseReference
    lateinit var hashRef:DatabaseReference // 해시태그 레퍼런스(Picutre 아래의 Hash를 가리킴)
    lateinit var yearSpinner:Spinner
    lateinit var monthSpinner:Spinner
    lateinit var yearAdapter : Adapter
    lateinit var monthAdapter : Adapter
    var yearMonth:String = ""
    val hashArray = ArrayList<Hash>() // 검색한 해시의 사진 url을 저장하는 배열
    var hashValue:String = "" // 검색된 해시의 사진 url을 저장하는 임시 변수
    var hash:String = "" // 검색한 hashtag

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inf = inflater.inflate(R.layout.diary_list, container, false)
        root = FirebaseDatabase.getInstance().reference
            .child(UserModel.uid)

        hashRef = root.child("Picture").child("Hash")

        val hashAdapter = HashListAdatper(context, hashArray)
        inf.diarylistview.adapter = hashAdapter
        inf.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i(tag, "query:"+ query.toString())
                hash = query.toString()
                hashRef.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.child(hash).exists()) {
                            hashArray.clear()
                            val myRef = dataSnapshot.child(hash)
                            for(i in myRef.children) {
                                hashValue = i.value.toString()
                                Log.i(tag, hashValue)
                                hashArray.add(Hash(hash, hashValue))
                            }
                            Log.i(tag, hashArray.toString())
                        } else {
                            Toast.makeText(context, "'${hash}' 에 해당하는 태그가 존재하지 않습니다:(", Toast.LENGTH_SHORT).show()
                        }
                        hashAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
                return false
            }
        })

        yearSpinner = inf.spinner_year
        yearAdapter = ArrayAdapter.createFromResource(
            context,
            R.array.date_year,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            yearSpinner.adapter = adapter
        }
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> yearMonth = "2016"
                    1 -> yearMonth = "2016"
                    2 -> yearMonth = "2017"
                    3 -> yearMonth = "2018"
                    4 -> yearMonth = "2019"
                    5 -> yearMonth = "2020"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                yearMonth = "20161"
            }
        }

        monthSpinner = inf.spinner_month
        monthAdapter = ArrayAdapter.createFromResource(
            context,
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
                yearMonth += position
                Toast.makeText(context, yearMonth, Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                yearMonth = "20161"
            }
        }
        return inf
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
