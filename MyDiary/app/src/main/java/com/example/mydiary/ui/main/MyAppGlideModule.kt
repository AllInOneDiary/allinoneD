package com.example.mydiary.ui.main

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.annotation.GlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference

import java.io.InputStream

@GlideModule
class MyAppGlideModule : AppGlideModule(){
    @Override
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(
            StorageReference::class.java,
            InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}