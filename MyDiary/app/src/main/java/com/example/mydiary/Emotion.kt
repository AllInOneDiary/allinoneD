package com.example.mydiary

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.animation.Easing

import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.emotion_result.view.*
import java.util.Calendar


class Emotion : Fragment() {
    lateinit var root: DatabaseReference
    lateinit var emotionSe: EmotionModel
    var emotionSave= ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        emotionResult()
        val a = inflater.inflate(R.layout.emotion_result, container, false)
        with(a) {
            pieChart.setUsePercentValues(true)
            pieChart.getDescription().setEnabled(false)
            pieChart.setExtraOffsets(5F, 10F, 5F, 5F)

            pieChart.setDragDecelerationFrictionCoef(0.95f)

            pieChart.setDrawHoleEnabled(false)
            pieChart.setHoleColor(Color.WHITE)
            pieChart.setTransparentCircleRadius(61f)
        }
        val yValues = ArrayList<PieEntry>()

        yValues.add(PieEntry(EmotionModel.angry, "angry"))
        yValues.add(PieEntry(EmotionModel.sad, "sad"))
        yValues.add(PieEntry(EmotionModel.confused, "confused"))
        yValues.add(PieEntry(EmotionModel.happy, "happy"))
        yValues.add(PieEntry(EmotionModel.love, "love"))


        a.pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic) //애니메이션

        val dataSet = PieDataSet(yValues, "기분 변화")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.setColors(*ColorTemplate.JOYFUL_COLORS)

        val data = PieData(dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.YELLOW)

        a.pieChart.setData(data)

        return a


    }

    private fun emotionResult(){
        val currentDate :Calendar= Calendar.getInstance()
        root = FirebaseDatabase.getInstance().reference.child(UserModel.uid).child("Diary").child(currentDate.get(Calendar.YEAR).toString()+(currentDate.get(Calendar.MONTH)+1).toString())
        root.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p: DataSnapshot) {
                for (filesnapshot in p.getChildren()) {
                    emotionSave = filesnapshot.child("emotion").value.toString()
                    when (emotionSave) {
                        "angry" -> EmotionModel.angry++
                        "confused" -> EmotionModel.confused++
                        "happy" -> EmotionModel.happy++
                        "love" -> EmotionModel.love++
                        "sad" -> EmotionModel.sad++
                    }
                }
            }
        })

    }
}

