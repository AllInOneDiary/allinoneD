package com.example.mydiary


import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SlideAdapter(fm : FragmentManager) : FragmentStatePagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        val arryList=List(10){ImageFragment()}

        return when(position){
            0 -> arryList[position]
            1 -> arryList[position]
            2 -> arryList[position]
            3 -> arryList[position]
            4 -> arryList[position]
            5 -> arryList[position]
            6 -> arryList[position]
            7 -> arryList[position]
            8 -> arryList[position]
            9 -> arryList[position]
            else ->  arryList[position]
        }
    }

    override fun getCount() = 10

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }
}