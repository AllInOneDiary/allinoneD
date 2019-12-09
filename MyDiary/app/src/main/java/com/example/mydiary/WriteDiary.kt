package com.example.mydiary

import android.app.Activity


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.google.android.gms.tasks.OnSuccessListener
import android.util.Log

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager
import com.example.mydiary.ui.main.GlideApp
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.content_diary.*
import kotlinx.android.synthetic.main.fragment.*
import java.lang.Exception

data class Diary(
    var title: String = "",
    var contents: String = "",
    var url: String = "",
    var emotion: String = ""
)

class WriteDiary : AppCompatActivity() {
    private val adapter by lazy { SlideAdapter(supportFragmentManager) }
    var file: Uri? = null
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
    lateinit var dataInput: Diary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diary)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

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
                    Log.e("tag", emotion + "/" + title + "/" + content + "/" + filename)

                    if (!title.equals("null")) {
                        editTitle.text.clear()
                        textContent.text.clear()
                        editTitle.setText(title)
                        textContent.setText(content)
                        if (!tmpfile.equals("null") && !tmpfile.equals("") && !tmpfile.equals(null)) {
                            Log.e("temp", "$tmpfile")

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

        var fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { moveToPricture() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = getMenuInflater()
        menuInflater.inflate(R.menu.diary_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            return when (item.itemId) {
                R.id.save -> {
                    saveDiary()
                    true
                }
                R.id.delete -> {
                    deleteDiary()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        return false
    }


    fun saveDiary() {
        var saveRef = root.child("Diary").child(yearMonth)
        if ((editTitle.text.toString().equals("")) || (textContent.text.toString().equals(""))) {
            Toast.makeText(applicationContext, "제목과 내용을 모두 써주세요!", Toast.LENGTH_SHORT).show()
        } else {
            dataInput = Diary(
                editTitle.text.toString(),
                textContent.text.toString(),
                tmpfile,
                emotion
            ) // title = editTitle의 내용, content = textContent의 내용
            var tmp = tmpfile.split("#")
            for (i in 0..tmp.size - 2) {
                putTag(tmp[i])
            }

            val store = mutableMapOf(day to dataInput)
            saveRef.updateChildren(store as Map<String, Any>).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@WriteDiary, "저장이 완료되었습니다:D!!", Toast.LENGTH_LONG).show()
                    val i = Intent(applicationContext, ViewPage::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(this@WriteDiary, "저장에 실패했습니다::(", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun deleteDiary() {
        val deleteRef = root.child("Diary").child(yearMonth).child(day)
        deleteRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    deleteRef.removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@WriteDiary, "삭제가 완료되었습니다:D!!", Toast.LENGTH_LONG)
                                .show()
                            val i = Intent(applicationContext, ViewPage::class.java)
                            startActivity(i)
                        } else {
                            Toast.makeText(this@WriteDiary, "삭제가 실패했습니다::(", Toast.LENGTH_LONG)
                                .show()
                        }
                    }// 201911이 루트라고 생각하면 됨.
                } else {
                    Toast.makeText(this@WriteDiary, "해당 일자에는 일기가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun moveToPricture() {
        imagePick()
    }

    fun setDataFile(file: Uri) {
        this.file = file
    }

    fun getDataFile(): Uri? {
        return file
    }

    // 갤러리로 넘어가는 intent 생성
    fun imagePick() {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 갤러리에서 이미지 받아와서 imageview에 저장 ( 삼성: 다중 선택 불가능 )

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                try {
                    if (data.clipData != null) {
                        for (i in 0 until data.clipData.itemCount) {
                            val imageUri = data.clipData.getItemAt(i).uri
                            setDataFile(imageUri)
                            adapter.addItem(
                                ImageFragment().apply {
                                    arguments = bundleOf(Pair("image", imageUri))
                                }
                            )
                            var tmp = imageUri.path.split("/")
                            filename = tmp[tmp.size - 1]
                            tmpfile = "$tmpfile#$filename"
                            var ref: StorageReference = imageRef.child(filename)
                            ref.putFile(imageUri)
                        }
                    } else if (data.data != null) {
                        val file = data.data
                        setDataFile(file)
                        adapter.addItem(
                            ImageFragment().apply {
                                arguments = bundleOf(Pair("image", file))
                            }
                        )
                        var tmp = file.path.split("/")
                        filename = tmp[tmp.size - 1]
                        tmpfile = "$tmpfile#$filename"

                        var ref: StorageReference = imageRef.child(filename)
                        ref.putFile(file).addOnSuccessListener(OnSuccessListener {
                            Toast.makeText(this, "성공했습니다", Toast.LENGTH_LONG).show()

                        })
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    var hashPicture = root.child("/Picture/Hash")


    fun putTag(fname: String) {
        if (!findViewById<EditText>(R.id.tag).text.equals("")) {
            var tmp = tag.text.trim().split("#")
            for (i in 1..tmp.size - 1) {
                val ref1 = hashPicture.child(tmp[i]).child(fname).setValue(fname)
            }
        }
    }
}