package com.example.mydiary


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.saved_diary.*


class ViewDiary : AppCompatActivity() {
    private val adapter by lazy { SlideAdapter(supportFragmentManager) }
    var date = ""
    var yearMonth: String = ""
    var day: String = ""
    var title: String = ""
    var content: String = ""
    var filename = ""
    var tmpfile = ""
    var emotion: String = ""
    val imageRef =
        FirebaseStorage.getInstance().getReferenceFromUrl("gs://mydiary-1121.appspot.com")
    var root: DatabaseReference = FirebaseDatabase.getInstance().reference.child(UserModel.uid)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_diary)

        var vp = findViewById<ViewPager>(R.id.viewPager)
        vp.adapter = this.adapter
        vp.offscreenPageLimit = 2

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (intent.hasExtra("date")) {
            date = intent.getStringExtra("date") // 2019/11/18 형식
            yearMonth = date.split("/")[0] + date.split("/")[1]
            day = date.split("/")[2]
            supportActionBar?.title = date // 날짜를 toolbar의 타이틀로 넣는다

            val reloadRef = root.child("Diary").child(yearMonth)
            reloadRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {}
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    emotion = dataSnapshot.child(day).child("emotion").value.toString()
                    title = dataSnapshot.child(day).child("title").value.toString()
                    content = dataSnapshot.child(day).child("contents").value.toString()
                    tmpfile = dataSnapshot.child(day).child("url").value.toString()
                    val tmp = tmpfile.split("#")
                    if (!title.equals("null")) {
                        editTitle1.setText(title)
                        textContent1.setText(content)
                        if (!tmpfile.equals("null") && !tmpfile.equals("") && !tmpfile.equals(null)) {

                            for (i in 1 until tmp.size) {
                                imageRef.child(tmp[i]).downloadUrl.addOnSuccessListener {
                                    adapter.addItem(
                                        ImageFragment().apply {
                                            arguments = bundleOf(Pair("image", it))
                                        }
                                    )
                                }

                            }
                        }

                    }
                    if(tmpfile == "null")
                        tmpfile = ""
                }

            })

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = getMenuInflater()
        menuInflater.inflate(R.menu.fix_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            return when (item.itemId) {
                R.id.fix -> {
                     fix()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        return false
    }

    fun fix(){
        var intent = Intent(this, WriteDiary::class.java)
        intent.putExtra("date", date)
        startActivity(intent)



    }
}