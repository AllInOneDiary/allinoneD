package com.example.mydiary
import android.annotation.SuppressLint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.search_layout.view.*

data class Hash(val hashtag:String, val url:String)
class SearchHash : Fragment() , AdapterView.OnItemSelectedListener{
    lateinit var hashRef: DatabaseReference // 해시태그 레퍼런스(Picutre 아래의 Hash를 가리킴)
    val hashArray = ArrayList<Hash>() // 검색한 해시와 해시의 사진 url을 저장하는 배열
    var hashValue:String = "" // 검색된 해시의 사진 url을 저장하는 임시 변수
    var hash:String = "" // 검색한 hashtag

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inf = inflater.inflate(R.layout.search_layout, container, false)
        hashRef = FirebaseDatabase.getInstance().reference.child(UserModel.uid).child("Picture").child("Hash")

        val hashAdapter = HashListAdatper(context, hashArray)
        inf.searchlist.adapter = hashAdapter
        inf.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                hash = query.toString()
                hashRef.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.child(hash).exists()) {
                            hashArray.clear()
                            val myRef = dataSnapshot.child(hash)
                            for(i in myRef.children) {
                                hashValue = i.value.toString()
                                hashArray.add(Hash(hash, hashValue))
                            }
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

        return inf
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}