package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Data(context: Context): SQLiteOpenHelper(context, DATABASE_NAME , null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "todos.db"
        private const val DATABASE_VERSION = 1
        private const val TBL_DATABASE = "tbl_database"
        private const val ID = "id"
        private const val TITTLE = "tittle"
        private const val DESCRIPTION = "description"
        private const val TIME = "time"
        private const val DEADLINE = "deadline"
        private const val STATE = "state"
        private const val PRIORITY = "priority"
        private const val TASKTYPE = "tasktype"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblTodos =
            ("CREATE TABLE " + TBL_DATABASE + "(" + ID + " INTEGER_PRIMARY_KEY, " + TITTLE + " TEXT, " + DESCRIPTION + " TEXT, "
                    + PRIORITY + " TEXT, " + DEADLINE + " TEXT, " + TIME + " TEXT, " + TASKTYPE + " TEXT,"
                    + STATE + " INTEGER_PRIMARY_KEY" + ")")
        db?.execSQL(createTblTodos)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_DATABASE")
        onCreate(db)
    }


    fun insertTodo(todo: Todo):Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, todo.id)
        contentValues.put(TITTLE, todo.tittle)
        contentValues.put(STATE, todo.state)
        contentValues.put(DEADLINE, todo.deadline)
        contentValues.put(DESCRIPTION, todo.description)
        contentValues.put(TIME, todo.time)
        contentValues.put(PRIORITY, todo.priority)
        contentValues.put(TASKTYPE, todo.taskType)


        val success = db.insert(TBL_DATABASE, null, contentValues)
        db.close()
        return success
    }

    fun getAllTodos(): ArrayList<Todo> {
        var todoList: ArrayList<Todo> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_DATABASE"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var tittle: String
        var description: String
        var deadline: String
        var state: Int
        var time:String
        var priority:String
        var taskType:String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                tittle = cursor.getString(cursor.getColumnIndex("tittle"))
                state = cursor.getInt(cursor.getColumnIndex("state"))
                description = cursor.getString(cursor.getColumnIndex("description"))
                deadline = cursor.getString(cursor.getColumnIndex("deadline"))
                time = cursor.getString(cursor.getColumnIndex("time"))
                priority = cursor.getString(cursor.getColumnIndex("priority"))
                taskType = cursor.getString(cursor.getColumnIndex("tasktype"))
                val todo = Todo(id = id, tittle = tittle, state = state,deadline = deadline, description = description , time = time,priority = priority, taskType = taskType)
                todoList.add(todo)

            } while (cursor.moveToNext())
        }
        return todoList
    }

    fun updateTodo(todo: Todo):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, todo.id)
        contentValues.put(TITTLE, todo.tittle)
        contentValues.put(STATE, todo.state)
        contentValues.put(DEADLINE, todo.deadline)
        contentValues.put(DESCRIPTION, todo.description)
        contentValues.put(TIME, todo.time)
        contentValues.put(PRIORITY, todo.priority)
        contentValues.put(TASKTYPE, todo.taskType)

        val success = db.update(TBL_DATABASE,contentValues ,"id=" + todo.id, null)

        db.close()
        return success

    }

    fun deleteTodoById(id:Int): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID,id)

        val success = db.delete(TBL_DATABASE, "id=$id", null)
        db.close()
        return success
    }
}