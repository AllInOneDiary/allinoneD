package com.example.mydiary
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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