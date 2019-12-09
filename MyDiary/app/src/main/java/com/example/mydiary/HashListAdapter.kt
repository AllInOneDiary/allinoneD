package com.example.mydiary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.mydiary.ui.main.GlideApp
import com.google.firebase.storage.FirebaseStorage


class HashListAdatper(val context: Context?, val hashArray:ArrayList<Hash>) : BaseAdapter()  {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.hash_item, null)
        val hash = hashArray[position]
        val hashValue = hash.url

        val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mydiary-1121.appspot.com")
        val image = view.findViewById<ImageView>(R.id.searchItem)
        if(!hashValue.equals(null) && !hashValue.equals("")&&context != null) {
            val imageRefChild = imageRef.child(hashValue)
            GlideApp.with(context).load(imageRefChild).into(image)
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return hashArray[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return hashArray.size
    }
}