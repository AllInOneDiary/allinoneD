package com.example.mydiary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class HashListAdatper(val context: Context?, val hashArray:ArrayList<Hash>) : BaseAdapter()  {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.hash_item, null)

        val hashTag = view.findViewById<TextView>(R.id.titleText)
        val hashValue = view.findViewById<TextView>(R.id.urlText)

        val hash = hashArray[position]

        hashTag.text = hash.hashtag
        hashValue.text = hash.url

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