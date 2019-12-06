package com.example.mydiary.ui.main


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.mydiary.R
import kotlinx.android.synthetic.main.diary_detail.view.*

class DiaryListViewAdapter(val context: Context?, val diaryList:ArrayList<DiaryListViewItem>) : BaseAdapter(){
    var inflater : LayoutInflater? = null

    @Override
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var _context: Context? = parent?.context
        var mConvertView = LayoutInflater.from(_context).inflate(R.layout.diary_detail, null)


        val diaryTitle = mConvertView?.title
        val diaryContent = mConvertView?.content
        val diaryDate = mConvertView?.date

        val diary = diaryList[position]
        diaryTitle?.text = diary.getDiaryTitle()
        diaryContent?.text = diary.getDiaryContent()
        diaryDate?.text = diary.getDiaryDate()

        return mConvertView
    }

    @Override
    override fun getItem(position: Int): Any {
        return diaryList[position]
    }

    @Override
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @Override
    override fun getCount(): Int {
        return diaryList.size
    }

}