package com.example.mydiary

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.PermissionChecker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment.*
import kotlinx.android.synthetic.main.fragment_b.*
import java.io.File
import java.lang.Exception
import java.net.URI
import java.util.jar.Manifest
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.ImageView
import kotlinx.android.synthetic.main.putpicture.*
import java.nio.file.Files.size
import android.content.pm.ResolveInfo
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import kotlinx.android.synthetic.main.fragment_a.*
import kotlinx.android.synthetic.main.fragment_c.*
import kotlinx.android.synthetic.main.fragment_d.*
import kotlinx.android.synthetic.main.fragment_e.*
import kotlinx.android.synthetic.main.fragment_f.*
import kotlinx.android.synthetic.main.fragment_g.*
import kotlinx.android.synthetic.main.fragment_h.*
import kotlinx.android.synthetic.main.fragment_i.*
import kotlinx.android.synthetic.main.fragment_j.*
import java.io.InputStream
import android.R.attr.data
import android.R.attr.fragment
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.CursorAdapter
import androidx.loader.content.CursorLoader
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class PutPicture : AppCompatActivity() {

    private val adapter by lazy { SlideAdapter(supportFragmentManager) }
    val intent_get = Intent()
    val date = intent_get.getStringExtra("date")
    var filedata: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.putpicture)


        // 갤러리 접근 권한 요청 ????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        // 카메라 사진 누르면 갤러리로 이동 리스너
        camera.setOnClickListener { imagePick() }


        // 이미지 뷰페이저 (사진 넘김)
        viewPager.adapter = PutPicture@ adapter
        viewPager.offscreenPageLimit = 2


        // 저장 버튼 누르면 이전 액티비티로 넘어감
        button.setOnClickListener {
            upload(returnData())
            var intent = Intent(this, DiaryList::class.java)
            startActivity(intent)
        }
    }


    // 파이어 베이스 storage에 이미지 저장할 때 쓰이는 함수
    fun returnData(): Uri? {
        return filedata
    }

    fun setFileData(data: Uri) {
        filedata = data
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
        var input: InputStream? = null

        // 갤러리에서 이미지 받아와서 imageview에 저장 ( 삼성: 다중 선택 불가능 )
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                try {
                    var filePath: Uri = data.data
                    fragmentImageA.setImageURI(filePath)
                    //upload(filePath)
                    setFileData(filePath)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    // 파이어베이스 storage 에 이미지 저장
    fun upload(file: Uri?) {
        if(file != null){
            Log.i("heepie", file.path + "")

            var tmp = file.path.split("/")
            var filename = tmp[tmp.size - 1]

            var ref: StorageReference =
                FirebaseStorage.getInstance().getReference().child("diaryImage/" + "확인")

            ref.putFile(file).addOnSuccessListener(OnSuccessListener {
                Toast.makeText(this, "성공했습니다", Toast.LENGTH_LONG).show()
            })
        } else
            Toast.makeText(this, "이미지가 넘어오지 않음. 업로드 불가 !!", Toast.LENGTH_LONG).show()
    }

}

