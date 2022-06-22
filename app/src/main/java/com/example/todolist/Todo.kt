package com.example.todolist

import java.util.*

data class Todo(
    var id:Int ,
    var tittle:String,
    var description:String,
    var deadline:String = "",
    var time:String = "",
    var state:Int = 0,
    var priority:String = "",
    var taskType:String = ""
) {

    companion object{
        var id = 0


        fun getAutoId():Int{
            val random = Random()
            return random.nextInt(1000000000)
            id++
            return id
        }
    }
}