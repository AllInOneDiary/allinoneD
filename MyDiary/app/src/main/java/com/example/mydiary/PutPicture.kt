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
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.android.synthetic.main.putpicture.*


class PutPicture: AppCompatActivity(){
    private val pick_from_Album = 1
    private val adapter by lazy { SlideAdapter(supportFragmentManager) }

    val intent_get = Intent()
    val date = intent_get.getStringExtra("date")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.putpicture)

        camera.setOnClickListener{openGallery()}

        viewPager.adapter = PutPicture@adapter
        viewPager.offscreenPageLimit = 2
    }

    private fun openGallery(){
        val intent : Intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), pick_from_Album)


    }

    @Override
    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }
}
