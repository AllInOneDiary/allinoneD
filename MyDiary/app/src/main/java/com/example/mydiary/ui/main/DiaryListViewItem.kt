package com.example.mydiary.ui.main

class DiaryListViewItem{
    var title = ""
    var content = ""
    var date = ""
    var day = ""
    constructor(title: String, content: String, date: String, day: String){
        this.title = title
        this.content = content
        this.date = date
        this.day = day
    }

    fun getDiaryTitle(): String{
        return title
    }

    fun getDiaryContent():String{
        return content
    }

    fun getDiaryDate():String{
        return date
    }
}