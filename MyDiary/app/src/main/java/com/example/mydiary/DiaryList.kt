package com.example.mydiary

import android.content.Intent
import android.os.Bundle
import android.renderscript.Sampler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Tasks
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_diary.*
import java.security.AccessController.getContext

data class Diary(
    var title : String = "",
    var contents : String = ""
)
class DiaryList: AppCompatActivity() {
    var date = ""
    var yearMonth:String = ""
    var day: String = ""
    lateinit var root:DatabaseReference
    lateinit var diaryRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diary)

        root = FirebaseDatabase.getInstance().reference.child("Diary")

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if(intent.hasExtra("date")) {
            date = intent.getStringExtra("date") // 2019/11/18 형식
            yearMonth = date.split("/")[0] + date.split("/")[1]
            supportActionBar?.title = date // 날짜를 toolbar의 타이틀로 넣는다
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

    fun moveToPricture(){
        var intent = Intent(this, PutPicture::class.java)
        intent.putExtra("date", date)
        startActivity(intent)
    }

    fun saveDiary(date:String) {
        var dataInput = Diary(editTitle.text.toString(), textContent.text.toString()) // title = editTitle의 내용, content = textContent의 내용
        yearMonth = date.split("/")[0] + date.split("/")[1]
        day = date.split("/")[2]
        diaryRef = root.child(yearMonth)

        val store = mutableMapOf(day to dataInput)
        diaryRef.updateChildren(store as Map<String, Any>).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this@DiaryList, "저장이 완료되었습니다:D!!", Toast.LENGTH_LONG).show()
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this@DiaryList, "저장에 실패했습니다::(", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun deleteDiary(date:String) {
        yearMonth = date.split("/")[0] + date.split("/")[1]
        day = date.split("/")[2]
        root.child(yearMonth).child(day).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this@DiaryList, "삭제가 완료되었습니다:D!!", Toast.LENGTH_LONG).show()
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this@DiaryList, "삭제가 실패했습니다::(", Toast.LENGTH_LONG).show()
            }
        }// 201911이 루트라고 생각하면 됨.
    }
}