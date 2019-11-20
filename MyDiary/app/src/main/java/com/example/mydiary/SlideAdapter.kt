package com.example.mydiary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

 class SlideAdapter(fm : FragmentManager) : FragmentStatePagerAdapter(fm){

     override fun getItem(position: Int): Fragment {
         return when(position){
             0 -> AFragment()
             1 -> BFragment()
             2 -> CFragment()
             3 -> DFragment()
             4 -> EFragment()
             5 -> FFragment()
             6 -> GFragment()
             7 -> HFragment()
             8 -> IFragment()
             9 -> JFragment()
             else -> ZFragment()
         }
     }

     override fun getCount() = 10

     override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
         super.destroyItem(container, position, `object`)
     }
}