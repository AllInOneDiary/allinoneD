package com.example.mydiary.ui.main

class DiaryListViewItem{
    var title = ""
    var date = ""

    constructor(title: String, date: String){
        this.title = title
        this.date = date
    }

    fun getDiaryTitle(): String{
        return title
    }

    fun getDiaryDate():String{
        return date
    }
}