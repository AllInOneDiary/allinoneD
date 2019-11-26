package com.example.mydiary


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description

import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.emotion_result.*
import kotlinx.android.synthetic.main.emotion_result.view.*


class Emotion : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val a=inflater.inflate(R.layout.emotion_result, container, false)
                a.pieChart.setUsePercentValues(true)
        a.pieChart.getDescription().setEnabled(false)
        a.pieChart.setExtraOffsets(5F, 10F, 5F, 5F)

        a.pieChart.setDragDecelerationFrictionCoef(0.95f)

        a.pieChart.setDrawHoleEnabled(false)
        a.pieChart.setHoleColor(Color.WHITE)
        a.pieChart.setTransparentCircleRadius(61f)

        val yValues = ArrayList<PieEntry>()

        yValues.add(PieEntry(34f, "1"))
//        yValues.add(PieEntry(23f, "2"))
//        yValues.add(PieEntry(14f, "3"))
//        yValues.add(PieEntry(35f, "4"))
//        yValues.add(PieEntry(40f, "5"))




        a.pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic) //애니메이션

        val dataSet = PieDataSet(yValues, "기분 변화")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.setColors(*ColorTemplate.JOYFUL_COLORS)

        val data = PieData(dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.YELLOW)

        a.pieChart.setData(data)
        a.emo_select.setOnClickListener { v: View ->
            val intent = Intent(getContext(), EmotionSe::class.java)
            startActivity(intent)
        }
        return a


    }

}

