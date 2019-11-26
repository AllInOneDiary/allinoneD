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
import android.graphics.Bitmap
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class PutPicture : AppCompatActivity() {
    private val pick_from_Album = 1
    private val adapter by lazy { SlideAdapter(supportFragmentManager) }
    var imageList = arrayOfNulls<ImageView>(10)
    val intent_get = Intent()
    val date = intent_get.getStringExtra("date")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.putpicture)

        imageList.set(0, fragmentImageA)
        imageList.set(1, fragmentImageB)
        imageList.set(2, fragmentImageC)
        imageList.set(3, fragmentImageD)
        imageList.set(4, fragmentImageE)
        imageList.set(5, fragmentImageF)
        imageList.set(6, fragmentImageG)
        imageList.set(7, fragmentImageH)
        imageList.set(8, fragmentImageI)
        imageList.set(9, fragmentImageJ)
        camera.setOnClickListener { imagePick() }

        viewPager.adapter = PutPicture@ adapter
        viewPager.offscreenPageLimit = 2
    }

    fun imagePick() {
        val intent = Intent()
        intent.type = "image/*"
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 1)
    }


    @Override
    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val uri: Uri? = data?.data

                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri)

                fragmentImageA.setImageBitmap(bitmap)

            }
        }
    }


}
