package com.example.mydiary.ui.main

class DiaryListViewItem{
    var title = ""
    var content = ""
    var date = ""

    constructor(title: String, content: String, date: String){
        this.title = title
        this.content = content
        this.date = date
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