package com.example.mydiary

import android.app.Activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.google.android.gms.tasks.OnSuccessListener

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager

import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.content_diary.*

import java.lang.Exception


data class Diary(
    var title : String = "",
    var contents : String = ""
)

class WriteDiary: AppCompatActivity() {
    private val adapter by lazy { SlideAdapter(supportFragmentManager) }
    var file : Uri? = null
    lateinit var tagRef : StorageReference

    var date = ""
    var yearMonth:String = ""
    var day: String = ""
    var title:String = ""
    var content:String = ""
    lateinit var root:DatabaseReference
    lateinit var diaryRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diary)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        var vp = findViewById<ViewPager>(R.id.viewPager)
        vp.adapter = PutPicture@ adapter
        vp.offscreenPageLimit = 2

        root = FirebaseDatabase.getInstance().reference.child(UserModel.uid)
        diaryRef = root.child("Diary")

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if(intent.hasExtra("date")) {
            date = intent.getStringExtra("date") // 2019/11/18 형식
            yearMonth = date.split("/")[0] + date.split("/")[1]
            day = date.split("/")[2]
            supportActionBar?.title = date // 날짜를 toolbar의 타이틀로 넣는다
            diaryRef = diaryRef.child(yearMonth)
            val listener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.hasChild(day)) {
                        title = dataSnapshot.child(day).child("title").value.toString()
                        content = dataSnapshot.child(day).child("contents").value.toString()

                        editTitle.text.clear()
                        textContent.text.clear()
                        editTitle.text.append(title)
                        textContent.text.append(content)
                    } else {
                        editTitle.text.append("")
                        textContent.text.append("")
                    }
                }
            }
            diaryRef.addValueEventListener(listener)
        }

        var fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{moveToPricture()}
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
                    saveDiary(date)
                    upload(getDataFile())
                    true
                }
                R.id.delete -> {
                    deleteDiary(date)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        return false
    }



    fun saveDiary(date:String) {
        var dataInput = Diary(editTitle.text.toString(), textContent.text.toString()) // title = editTitle의 내용, content = textContent의 내용
        diaryRef = root.child("Diary")
        var dR = diaryRef.child(yearMonth)

        val store = mutableMapOf(day to dataInput)
        dR.updateChildren(store as Map<String, Any>).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this@WriteDiary, "저장이 완료되었습니다:D!!", Toast.LENGTH_LONG).show()
                val i = Intent(applicationContext, ViewPage::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this@WriteDiary, "저장에 실패했습니다::(", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun deleteDiary(date:String) {
        diaryRef = root.child("Diary").child(yearMonth).child(day)
        diaryRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this@WriteDiary, "삭제가 완료되었습니다:D!!", Toast.LENGTH_LONG).show()
                val i = Intent(applicationContext, ViewPage::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this@WriteDiary, "삭제가 실패했습니다::(", Toast.LENGTH_LONG).show()
            }
        }// 201911이 루트라고 생각하면 됨.
    }


    fun moveToPricture(){
        imagePick()
    }

    fun setDataFile(file: Uri){
        this.file = file
    }

    fun getDataFile():Uri?{
        return file
    }

    // 갤러리로 넘어가는 intent 생성
    fun imagePick() {
        var intent = Intent()
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        intent.setAction(Intent.ACTION_PICK)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)

    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 갤러리에서 이미지 받아와서 imageview에 저장 ( 삼성: 다중 선택 불가능 )
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                try {
                    var file = data.data
                    setDataFile(file)
                    var frag = findViewById<ImageView>(R.id.fragmentImage)
                    //upload(filePath)
                    frag.setImageURI(file)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    // 파이어베이스 storage 에 이미지 저장
    fun upload(file: Uri?) {
        if(file != null) {
            tagRef =  FirebaseStorage.getInstance().reference
            var tmp = file.path.split("/")
            var filename = tmp[tmp.size - 1]

            var ref: StorageReference =
                tagRef.child("diaryImage/" + "수정")

            ref.putFile(file).addOnSuccessListener(OnSuccessListener {
                Toast.makeText(this, "성공했습니다", Toast.LENGTH_LONG).show()
                putTag(file)
            })
        } else
            Toast.makeText(this, "이미지가 넘어오지 않음. 업로드 불가 !!", Toast.LENGTH_LONG).show()

    }

    var tagRoot: DatabaseReference = FirebaseDatabase.getInstance().reference.child(UserModel.uid)
    var picture = tagRoot.child("Picture")
    var tagMap = mutableMapOf<String, Any>()

    fun putTag(file: Uri){
        if(!tag.text.equals("")){
            var tmp = tag.text.trim().split("#")

            for(i in 1..tmp.size-1) {
                var pictureChild = picture.child(tmp[i])
                tagMap.put("index", file.toString())
                pictureChild.updateChildren(tagMap)
            }
        } else{}
    }
}