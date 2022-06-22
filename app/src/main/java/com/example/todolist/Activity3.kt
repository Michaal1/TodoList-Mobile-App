package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.*
import android.app.UiModeManager.MODE_NIGHT_NO
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class Activity3 : AppCompatActivity() {

    private lateinit var recyclerView:RecyclerView
    private lateinit var btHome: ImageButton
    private lateinit var btModifiRoutine: ImageButton
    private lateinit var btGoals: ImageButton
    private lateinit var btAdd: ImageButton
    private lateinit var btDelete: ImageButton
    // Data
    private lateinit var data:Data
    private var todo:Todo? = null
    private var taskType:String = ""

    // Adapters
    private var adapter:TodoAdapterGoals? = null

    // Other
    private var priority = ""
    private var type = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)
        clearSavedDatas()

        initView()
        initRecyclerView()


        data = Data(this)


        // Show all tasks
        getTodos()

        btModifiRoutine.setOnClickListener {
            val intent = Intent(this,Activity2::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
            finish()
        }
        btHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
            finish()

        }

        btAdd.setOnClickListener {
            clearSavedDatas()

            val bottomSheetDialog = BottomSheetDialog(this@Activity3)

            // Create Bottom sheet
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottom_sheet_add_goal,
                findViewById(R.id.bottomSheetChoose)
            )
            taskType = "GOAL_TASK"


            // Bottom sheet views initialization
            var btAddTask = bottomSheetView.findViewById<Button>(R.id.btAdd)
            var tittle = bottomSheetView.findViewById<EditText>(R.id.etTittle)
            var description = bottomSheetView.findViewById<EditText>(R.id.etTask)

            var cbSchool = bottomSheetView.findViewById<CheckBox>(R.id.cbSchool)
            var cbProductivity = bottomSheetView.findViewById<CheckBox>(R.id.cbProductivity)
            var cbFreetime = bottomSheetView.findViewById<CheckBox>(R.id.cbFreetime)
            var cbWorkout = bottomSheetView.findViewById<CheckBox>(R.id.cbWorkout)
            var cbSleep = bottomSheetView.findViewById<CheckBox>(R.id.cbSleep)
            var cbFood = bottomSheetView.findViewById<CheckBox>(R.id.cbFood)



            cbSchool.setOnClickListener {
                cbProductivity.isChecked = false
                cbFreetime.isChecked = false
                cbWorkout.isChecked = false
                cbSleep.isChecked = false
                cbFood.isChecked = false
                type = "School"
            }
            cbProductivity.setOnClickListener {
                cbSchool.isChecked = false
                cbFreetime.isChecked = false
                cbWorkout.isChecked = false
                cbSleep.isChecked = false
                cbFood.isChecked = false
                type = "Productivity"
            }
            cbFreetime.setOnClickListener {
                cbSchool.isChecked = false
                cbProductivity.isChecked = false
                cbWorkout.isChecked = false
                cbSleep.isChecked = false
                cbFood.isChecked = false
                type = "Freetime"
            }
            cbWorkout.setOnClickListener {
                cbSchool.isChecked = false
                cbProductivity.isChecked = false
                cbFreetime.isChecked = false
                cbSleep.isChecked = false
                cbFood.isChecked = false
                type = "Workout"
            }
            cbSleep.setOnClickListener {
                cbSchool.isChecked = false
                cbProductivity.isChecked = false
                cbFreetime.isChecked = false
                cbWorkout.isChecked = false
                cbFood.isChecked = false
                type = "Sleep"
            }
            cbFood.setOnClickListener {
                cbSchool.isChecked = false
                cbProductivity.isChecked = false
                cbFreetime.isChecked = false
                cbWorkout.isChecked = false
                cbSleep.isChecked = false
                type = "Food"
            }

            btAddTask.setOnClickListener {

                // Check successful insert
                var success = addTask(tittle.text.toString(), description.text.toString())
                if (success == 2) { // When not passed a date
                    Toast.makeText(this, "Pick a date and time", Toast.LENGTH_SHORT).show()
                } else {
                    // Hide bottom sheet
                    bottomSheetDialog.dismiss()
                }
            }

            // Show Bottom sheet
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

        }
    }
    private fun getTodos(){

        val todoList = data.getAllTodos()
        Log.e("ppp", "${todoList.size}")
        var goals:ArrayList<Todo> = ArrayList()

        for(todo in todoList) {


            // Just for TASKs
            if(todo.taskType == "GOAL_TASK"){
                goals.add(todo)
            }

        }

        adapter?.addItem(goals)


    }

    private fun addTask(tittle:String,description:String):Int{

        // Just for TASKs
        // When we want to create TASK taskType == "TASK"
        var tittleText = tittle
        var descriptionText = description
        if(taskType == "GOAL_TASK"){
            var todo:Todo
            if(tittleText.isEmpty() || descriptionText.isEmpty()){
            }else{
                todo = Todo(tittle = tittleText, description = descriptionText, deadline = "", time = "",
                    priority = type, taskType = taskType,id = createId())
                priority = ""
                taskType = ""
                val status = data.insertTodo(todo)

                // check successful insert
                if (status> -1){
                    //setAlarm(todo)
                    Toast.makeText(this, "Todo Added...", Toast.LENGTH_SHORT).show()
                    getTodos()
                }else{
                    Toast.makeText(this, "Record not saved...", Toast.LENGTH_SHORT).show()
                }
                return 1 // Return 1 if everything is good
            }
            return 2 // Return 2 if not passed a date
        }
        return 3 // Return 3 if tittle or description is empty
    }

    private fun clearSavedDatas(){
        priority = ""
        taskType = ""
    }
    private fun initRecyclerView() {
        // Initialization all recycler views and adapters set
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TodoAdapterGoals()
        recyclerView.adapter = adapter

    }
    private fun initView(){
        btHome = findViewById(R.id.btHome)
        btModifiRoutine = findViewById(R.id.btModifiRutine)
        btGoals = findViewById(R.id.btGoals)
        btAdd = findViewById(R.id.btAdd)
        btDelete = findViewById(R.id.btDelete)
        recyclerView = findViewById(R.id.rvGoals)



    }
    private fun createId():Int{
        var todolist = data.getAllTodos()
        var isUnique = true
        while(true){
            var id = Random().nextInt(1000000000)
            for(todo in todolist){
                if(todo.id == id ){
                    isUnique = false
                    break
                }
            }
            if(isUnique){
                return id
            }
        }
    }
}