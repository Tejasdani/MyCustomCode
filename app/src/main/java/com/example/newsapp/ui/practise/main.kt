package com.example.newsapp.ui.practise

fun main() {

    var str:String? = "Tejas"
    str = null
    println(str?.length)

    var listType:List<Int?>? = listOf(1,2,null)
    listType = null
    println(listType)


}