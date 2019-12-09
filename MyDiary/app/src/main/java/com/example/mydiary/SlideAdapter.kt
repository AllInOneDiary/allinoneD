package com.example.mydiary


import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SlideAdapter(fm : FragmentManager) : FragmentStatePagerAdapter(fm){
    private val arrayList= mutableListOf<ImageFragment>()

    override fun getItem(position: Int): Fragment {
        return arrayList[position]
    }

    override fun getCount() = arrayList.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    fun addItem(item: ImageFragment) {
        arrayList.add(item)
        notifyDataSetChanged()
    }
}