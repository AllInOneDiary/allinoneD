package com.example.mydiary

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.emotion_result.view.*
import kotlinx.android.synthetic.main.emotion_result.view.search_chart as search_chart1


class Emotion : Fragment() {
    lateinit var root: DatabaseReference
    lateinit var yearSpinner: Spinner
    lateinit var monthSpinner: Spinner
    lateinit var yearAdapter : Adapter
    lateinit var monthAdapter : Adapter
    lateinit var inf:View
    var year:String=""
    var yearMonth:String = ""

    var emotionSave= ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inf = inflater.inflate(R.layout.emotion_result, container, false)
        yearSpinner = inf.chart_spinner_y
        yearAdapter = ArrayAdapter.createFromResource(
            context,
            R.array.date_year,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            yearSpinner.adapter = adapter
        }

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    1 -> year = 2016.toString()
                    2 -> year = 2017.toString()
                    3 -> year = 2018.toString()
                    4 -> year = 2019.toString()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        monthSpinner = inf.chart_spinner_m
        monthAdapter = ArrayAdapter.createFromResource(
            context,
            R.array.date_month,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            monthSpinner.adapter = adapter
        }
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                yearMonth=year+position.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        inf.search_chart1.setOnClickListener {
            emotionResult(yearMonth)

            Log.d(tag,yearMonth)
            Toast.makeText(getContext(),yearMonth,Toast.LENGTH_LONG)
        }

        return inf
    }
    private fun emotionResult(y: String) {
        root = FirebaseDatabase.getInstance().reference.child(UserModel.uid).child("Diary").child(y)
        root.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p: DataSnapshot) {
                EmotionModel.emotionInit()
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
                chartUpdate()

            }
        })

    }
    private fun chartUpdate(){
        with(inf) {
            pieChart.setUsePercentValues(true)
            pieChart.getDescription().setEnabled(false)
            pieChart.setExtraOffsets(5F, 10F, 5F, 5F)

            pieChart.setDragDecelerationFrictionCoef(0.95f)

            pieChart.setDrawHoleEnabled(false)
            pieChart.setHoleColor(android.graphics.Color.WHITE)
            pieChart.setTransparentCircleRadius(61f)
        }
        val yValues = ArrayList<PieEntry>()
        if(!EmotionModel.angry.equals(0F))
            yValues.add(PieEntry(EmotionModel.angry, "angry"))
        if(!EmotionModel.sad.equals(0F))
            yValues.add(PieEntry(EmotionModel.sad, "sad"))
        if(!EmotionModel.confused.equals(0F))
            yValues.add(PieEntry(EmotionModel.confused, "confused"))
        if(!EmotionModel.happy.equals(0F))
            yValues.add(PieEntry(EmotionModel.happy, "happy"))
        if(!EmotionModel.love.equals(0F))
            yValues.add(PieEntry(EmotionModel.love, "love"))


        inf.pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic) //애니메이션

        val dataSet = PieDataSet(yValues, "기분 변화")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.setColors(*ColorTemplate.PASTEL_COLORS)

        val data = PieData(dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.YELLOW)

        inf.pieChart.setData(data)
    }
}

