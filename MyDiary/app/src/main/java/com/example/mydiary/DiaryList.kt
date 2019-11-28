package com.example.mydiary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.diary_list.view.*

class DiaryList() : Fragment(), AdapterView.OnItemSelectedListener {
    @SuppressLint("ResourceType")
    lateinit var root:DatabaseReference
    lateinit var yearSpinner:Spinner
    lateinit var monthSpinner:Spinner
    lateinit var yearAdapter : Adapter
    lateinit var monthAdapter : Adapter
    var yearMonth:String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inf = inflater.inflate(R.layout.diary_list, container, false)
        root = FirebaseDatabase.getInstance().reference
            .child("Diary").child(UserModel.uid)
        yearSpinner = inf.spinner_year
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
                    0 -> yearMonth += 2016
                    1 -> yearMonth += 2017
                    2 -> yearMonth += 2018
                    3 -> yearMonth += 2019
                    4 -> yearMonth += 2020
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        monthSpinner = inf.spinner_month
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

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        return inf
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
