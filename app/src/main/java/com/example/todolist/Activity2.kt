package com.example.todolist

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Activity2 : AppCompatActivity(),DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    // Variables for date and time set
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var dayInWeek = 0
    var daysRoutine = ""

    // Saved date and time
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 24
    var savedMinute = ""

    // View variables
    private lateinit var btHome: ImageButton
    private lateinit var btModifiRoutine: ImageButton
    private lateinit var btGoals: ImageButton
    private lateinit var recyclerViewMonday:RecyclerView
    private lateinit var recyclerViewTuesday:RecyclerView
    private lateinit var recyclerViewWednesday:RecyclerView
    private lateinit var recyclerViewThursday:RecyclerView
    private lateinit var recyclerViewFriday:RecyclerView
    private lateinit var recyclerViewSaturday:RecyclerView
    private lateinit var recyclerViewSunday:RecyclerView
    private var adapterMonday:TodoAdapterRoutineModifi? = null
    private var adapterTuesday:TodoAdapterRoutineModifi? = null
    private var adapterWednesday:TodoAdapterRoutineModifi? = null
    private var adapterThursday:TodoAdapterRoutineModifi? = null
    private var adapterFriday:TodoAdapterRoutineModifi? = null
    private var adapterSaturday:TodoAdapterRoutineModifi? = null
    private var adapterSunday:TodoAdapterRoutineModifi? = null
    private lateinit var tvDayTime: TextView
    private lateinit var tvUncompleted: TextView
    private lateinit var tvToday: TextView
    private lateinit var tvTomorrow: TextView
    private lateinit var tvOther: TextView
    private lateinit var cbHigh: CheckBox
    private lateinit var cbMedium: CheckBox
    private lateinit var cbLow: CheckBox
    private lateinit var btRoutineTaskAdd: ImageButton
    private lateinit var btUpdateTask: Button
    private lateinit var tittle: EditText
    private lateinit var description: EditText
    private lateinit var noneTasks: TextView

    private lateinit var cbSchool:CheckBox
    private lateinit var cbProductivity:CheckBox
    private lateinit var cbFreetime:CheckBox
    private lateinit var cbWorkout:CheckBox
    private lateinit var cbSleep:CheckBox
    private lateinit var cbFood:CheckBox


    private lateinit var data:Data
    private var todo:Todo? = null
    private var taskType:String = ""

    // Other variables
    private var priority = ""
    private var typeRT = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        initView()
        initRecyclerView()

        data = Data(this)
        // Show all tasks
        getTodos()

        btHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
            finish()

        }
        btGoals.setOnClickListener {
            val intent = Intent(this,Activity3::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
            finish()

        }

        adapterMonday?.setOnDeleteItem {
            var status = deleteRoutine(it)

        }
        adapterTuesday?.setOnDeleteItem {
            var status = deleteRoutine(it)

        }
        adapterWednesday?.setOnDeleteItem {
            var status = deleteRoutine(it)

        }
        adapterThursday?.setOnDeleteItem {
            var status = deleteRoutine(it)

        }
        adapterFriday?.setOnDeleteItem {
            var status = deleteRoutine(it)

        }
        adapterSaturday?.setOnDeleteItem {
            var status = deleteRoutine(it)

        }
        adapterSunday?.setOnDeleteItem {
            var status = deleteRoutine(it)

        }


        val bottomSheetDialog = BottomSheetDialog(this@Activity2)

        // Create Bottom sheet
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.bottom_sheet_update_rutine,
            findViewById(R.id.bottomSheetUpdateRoutine)
        )

        // Bottom sheet views initialization
        var tvTime = bottomSheetView.findViewById<TextView>(R.id.tvPickTimeRoutine)
        var btUpdateRoutine = bottomSheetView.findViewById<Button>(R.id.btUpdateRoutine)
        var tittleRoutine = bottomSheetView.findViewById<EditText>(R.id.etTittleRoutine)
        var cbMon = bottomSheetView.findViewById<CheckBox>(R.id.cbMon)
        var cbTue = bottomSheetView.findViewById<CheckBox>(R.id.cbTue)
        var cbWed = bottomSheetView.findViewById<CheckBox>(R.id.cbWed)
        var cbThu = bottomSheetView.findViewById<CheckBox>(R.id.cbThu)
        var cbFri = bottomSheetView.findViewById<CheckBox>(R.id.cbFri)
        var cbSat = bottomSheetView.findViewById<CheckBox>(R.id.cbSat)
        var cbSun = bottomSheetView.findViewById<CheckBox>(R.id.cbSun)
        var cbAll = bottomSheetView.findViewById<CheckBox>(R.id.cbAll)

        adapterMonday?.setOnClickItem {
            bottomSheet(it)
        }

        adapterTuesday?.setOnClickItem {
            bottomSheet(it)
        }
        adapterWednesday?.setOnClickItem {
            bottomSheet(it)
        }
        adapterThursday?.setOnClickItem {
            bottomSheet(it)
        }
        adapterFriday?.setOnClickItem {
            bottomSheet(it)
        }
        adapterSaturday?.setOnClickItem {
            bottomSheet(it)
        }
        adapterSunday?.setOnClickItem {
            bottomSheet(it)
        }

        clearSavedDates()
        taskType = "ROUTINE_TASK"

        // Bottom sheet Choose functionality - choose what type of task do you want to add

        btRoutineTaskAdd.setOnClickListener {


            clearSavedDates()
            taskType = "ROUTINE_TASK"
            val bottomSheetDialog = BottomSheetDialog(this@Activity2)

            // Create Bottom sheet
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottom_sheet_add_routine,
                findViewById(R.id.bottomSheetRoutine)
            )

            // Bottom sheet views initialization
            var tvTime = bottomSheetView.findViewById<TextView>(R.id.tvPickTimeRoutine)
            var btAddRoutine = bottomSheetView.findViewById<Button>(R.id.btAddRoutine)
            var tittleRoutine = bottomSheetView.findViewById<EditText>(R.id.etTittleRoutine)
            var cbMon = bottomSheetView.findViewById<CheckBox>(R.id.cbMon)
            var cbTue = bottomSheetView.findViewById<CheckBox>(R.id.cbTue)
            var cbWed = bottomSheetView.findViewById<CheckBox>(R.id.cbWed)
            var cbThu = bottomSheetView.findViewById<CheckBox>(R.id.cbThu)
            var cbFri = bottomSheetView.findViewById<CheckBox>(R.id.cbFri)
            var cbSat = bottomSheetView.findViewById<CheckBox>(R.id.cbSat)
            var cbSun = bottomSheetView.findViewById<CheckBox>(R.id.cbSun)
            var cbAll = bottomSheetView.findViewById<CheckBox>(R.id.cbAll)
            cbSchool = bottomSheetView.findViewById<CheckBox>(R.id.cbSchool)
            cbProductivity = bottomSheetView.findViewById<CheckBox>(R.id.cbProductivity)
            cbFreetime = bottomSheetView.findViewById<CheckBox>(R.id.cbFreetime)
            cbWorkout = bottomSheetView.findViewById<CheckBox>(R.id.cbWorkout)
            cbSleep = bottomSheetView.findViewById<CheckBox>(R.id.cbSleep)
            cbFood = bottomSheetView.findViewById<CheckBox>(R.id.cbFood)

            cbSchool.setOnClickListener{
                cbProductivity.isChecked = false
                cbFreetime.isChecked = false
                cbWorkout.isChecked = false
                cbSleep.isChecked = false
                cbFood.isChecked = false
                typeRT = "School"
            }
            cbProductivity.setOnClickListener{
                cbSchool.isChecked = false
                cbFreetime.isChecked = false
                cbWorkout.isChecked = false
                cbSleep.isChecked = false
                cbFood.isChecked = false
                typeRT = "Productivity"
            }
            cbFreetime.setOnClickListener{
                cbSchool.isChecked = false
                cbProductivity.isChecked = false
                cbWorkout.isChecked = false
                cbSleep.isChecked = false
                cbFood.isChecked = false
                typeRT = "Freetime"
            }
            cbWorkout.setOnClickListener{
                cbSchool.isChecked = false
                cbProductivity.isChecked = false
                cbFreetime.isChecked = false
                cbSleep.isChecked = false
                cbFood.isChecked = false
                typeRT = "Workout"
            }
            cbSleep.setOnClickListener{
                cbSchool.isChecked = false
                cbProductivity.isChecked = false
                cbFreetime.isChecked = false
                cbWorkout.isChecked = false
                cbFood.isChecked = false
                typeRT = "Sleep"
            }
            cbFood.setOnClickListener{
                cbSchool.isChecked = false
                cbProductivity.isChecked = false
                cbFreetime.isChecked = false
                cbWorkout.isChecked = false
                cbSleep.isChecked = false
                typeRT = "Food"
            }


            // Chose routine time
            tvTime.setOnClickListener {

                getDateTimeCalendar()

                TimePickerDialog(this,this,hour,minute,true).show()
            }

            // Add a new routine task
            btAddRoutine.setOnClickListener {// Set day to show the routine task
                if(cbMon.isChecked) {
                    addRoutineDay(1)
                }
                if(cbTue.isChecked) {
                    addRoutineDay(2)
                }
                if(cbWed.isChecked) {
                    addRoutineDay(3)
                }
                if(cbThu.isChecked) {
                    addRoutineDay(4)
                }
                if(cbFri.isChecked) {
                    addRoutineDay(5)
                }
                if(cbSat.isChecked) {
                    addRoutineDay(6)
                }
                if(cbSun.isChecked) {
                    addRoutineDay(7)
                }
                if(cbAll.isChecked) {
                    addRoutineDay(1)
                    addRoutineDay(2)
                    addRoutineDay(3)
                    addRoutineDay(4)
                    addRoutineDay(5)
                    addRoutineDay(6)
                    addRoutineDay(7)
                }
                // Check successful insert
                ifNothingIsChecked()
                var success = addTask(tittleRoutine.text.toString(),"")
                if (success == 2) { // When not passed a time
                    Toast.makeText(this, "Pick time", Toast.LENGTH_SHORT).show()
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
    private fun bottomSheet(it:Todo){
        val bottomSheetDialog = BottomSheetDialog(this@Activity2)

        // Create Bottom sheet
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.bottom_sheet_update_rutine,
            findViewById(R.id.bottomSheetUpdateRoutine)
        )

        // Bottom sheet views initialization
        var tvTime = bottomSheetView.findViewById<TextView>(R.id.tvPickTimeRoutine)
        var btUpdateRoutine = bottomSheetView.findViewById<Button>(R.id.btUpdateRoutine)
        var tittleRoutine = bottomSheetView.findViewById<EditText>(R.id.etTittleRoutine)
        var cbMon = bottomSheetView.findViewById<CheckBox>(R.id.cbMon)
        var cbTue = bottomSheetView.findViewById<CheckBox>(R.id.cbTue)
        var cbWed = bottomSheetView.findViewById<CheckBox>(R.id.cbWed)
        var cbThu = bottomSheetView.findViewById<CheckBox>(R.id.cbThu)
        var cbFri = bottomSheetView.findViewById<CheckBox>(R.id.cbFri)
        var cbSat = bottomSheetView.findViewById<CheckBox>(R.id.cbSat)
        var cbSun = bottomSheetView.findViewById<CheckBox>(R.id.cbSun)
        var cbAll = bottomSheetView.findViewById<CheckBox>(R.id.cbAll)
        cbSchool = bottomSheetView.findViewById<CheckBox>(R.id.cbSchool)
        cbProductivity = bottomSheetView.findViewById<CheckBox>(R.id.cbProductivity)
        cbFreetime = bottomSheetView.findViewById<CheckBox>(R.id.cbFreetime)
        cbWorkout = bottomSheetView.findViewById<CheckBox>(R.id.cbWorkout)
        cbSleep = bottomSheetView.findViewById<CheckBox>(R.id.cbSleep)
        cbFood = bottomSheetView.findViewById<CheckBox>(R.id.cbFood)

        clearSavedDates()
        var todo = it
        // Set tasks data
        tittleRoutine.setText(it.tittle)
        extractRoutineDays(it.deadline)
        if("1" in daysRoutine){
            cbMon.isChecked = true
        }
        if("2" in daysRoutine){
            cbTue.isChecked = true
        }
        if("3" in daysRoutine){
            cbWed.isChecked = true
        }
        if("4" in daysRoutine){
            cbThu.isChecked = true
        }
        if("5" in daysRoutine){
            cbFri.isChecked = true
        }
        if("6" in daysRoutine){
            cbSat.isChecked = true
        }
        if("7" in daysRoutine){
            cbSun.isChecked = true
        }

        if(it.priority == "School"){
            cbSchool.isChecked = true
            typeRT = "School"
        }
        if(it.priority == "Productivity"){
            cbProductivity.isChecked = true
            typeRT = "Productivity"
        }
        if(it.priority == "Freetime"){
            cbFreetime.isChecked = true
            typeRT = "Freetime"
        }
        if(it.priority == "Workout"){
            cbWorkout.isChecked = true
            typeRT = "Workout"
        }
        if(it.priority == "Sleep"){
            cbSleep.isChecked = true
            typeRT = "Sleep"
        }
        if(it.priority == "Food"){
            cbFood.isChecked = true
            typeRT = "Food"
        }

        cbSchool.setOnClickListener{
            cbProductivity.isChecked = false
            cbFreetime.isChecked = false
            cbWorkout.isChecked = false
            cbSleep.isChecked = false
            cbFood.isChecked = false
            typeRT = "School"
        }
        cbProductivity.setOnClickListener{
            cbSchool.isChecked = false
            cbFreetime.isChecked = false
            cbWorkout.isChecked = false
            cbSleep.isChecked = false
            cbFood.isChecked = false
            typeRT = "Productivity"
        }
        cbFreetime.setOnClickListener{
            cbSchool.isChecked = false
            cbProductivity.isChecked = false
            cbWorkout.isChecked = false
            cbSleep.isChecked = false
            cbFood.isChecked = false
            typeRT = "Freetime"
        }
        cbWorkout.setOnClickListener{
            cbSchool.isChecked = false
            cbProductivity.isChecked = false
            cbFreetime.isChecked = false
            cbSleep.isChecked = false
            cbFood.isChecked = false
            typeRT = "Workout"
        }
        cbSleep.setOnClickListener{
            cbSchool.isChecked = false
            cbProductivity.isChecked = false
            cbFreetime.isChecked = false
            cbWorkout.isChecked = false
            cbFood.isChecked = false
            typeRT = "Sleep"
        }
        cbFood.setOnClickListener{
            cbSchool.isChecked = false
            cbProductivity.isChecked = false
            cbFreetime.isChecked = false
            cbWorkout.isChecked = false
            cbSleep.isChecked = false
            typeRT = "Food"
        }

        // Chose deadline date and time

        tvTime.setOnClickListener {

            // Show calendar
            getDateTimeCalendar()
            TimePickerDialog(this,this,hour,minute,true).show()


        }

        // Update task button onClick
        btUpdateRoutine.setOnClickListener {
            daysRoutine = ""
            if(cbMon.isChecked) {
                addRoutineDay(1)
            }
            if(cbTue.isChecked) {
                addRoutineDay(2)
            }
            if(cbWed.isChecked) {
                addRoutineDay(3)
            }
            if(cbThu.isChecked) {
                addRoutineDay(4)
            }
            if(cbFri.isChecked) {
                addRoutineDay(5)
            }
            if(cbSat.isChecked) {
                addRoutineDay(6)
            }
            if(cbSun.isChecked) {
                addRoutineDay(7)
            }
            if(cbAll.isChecked) {
                addRoutineDay(1)
                addRoutineDay(2)
                addRoutineDay(3)
                addRoutineDay(4)
                addRoutineDay(5)
                addRoutineDay(6)
                addRoutineDay(7)
            }
            ifNothingIsChecked()
            update(tittleRoutine.text.toString(),"","",todo)
            // hide bottom sheet
            bottomSheetDialog.dismiss()

            // Show all tasks
            getTodos()


        }
        // Set bottom sheet content and show it
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun extractRoutineDays(string:String){
        var string2 = string.split(" ")
        for(i in string2){
            if("/" in i){

            }else{
                daysRoutine = daysRoutine + " " + i
            }
        }
    }
    // Update Task
    private fun update(tittle:String,description:String,priority:String, todo:Todo){
        var tittleText = tittle
        var descriptionText = description

        if(todo.taskType == "ROUTINE_TASK"){
            if(tittleText.isEmpty()){

            }else{

                var time = todo.time

                if(savedHour !=24){
                    time = savedHour.toString() + ":" + savedMinute
                }
                getDateTimeCalendar()
                todo.tittle = tittleText
                todo.description = descriptionText
                todo.priority = typeRT
                todo.deadline = day.toString() + "/"+ (month+1).toString() + "/"+ year.toString()+ " " + daysRoutine
                todo.time = time
                val status = data.updateTodo(todo)

                // check successful insert
                if (status> -1){
                    Toast.makeText(this, "Todo updated...", Toast.LENGTH_SHORT).show()
                    getTodos()
                }else{
                    Toast.makeText(this, "Record not saved...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun addTask(tittle:String,description:String):Int{

        // Just for TASKs
        // When we want to create TASK taskType == "TASK"
        var tittleText = tittle
        var descriptionText = description

        // Just for ROUTINE_TASKs
        // When we want to create ROUTINE_TASK taskType == "ROUTINE_TASK"
        if(taskType == "ROUTINE_TASK"){
            val cal = Calendar.getInstance()
            cal.time = Date()
            if(tittleText.isEmpty() ){
            }else{
                if(savedHour == 24 ) {
                    savedHour = 23
                    savedMinute = "59"
                }

                var time = savedHour.toString() + ":" + savedMinute
                getDateTimeCalendar()
                var deadline =   day.toString() + "/"+ (month+1).toString() + "/"+ year.toString()+ " " + daysRoutine
                var todoRoutine = Todo(tittle = tittleText, description = descriptionText, time = time,
                    priority = typeRT, taskType = taskType, deadline = deadline, id= createId())
                priority = ""
                taskType = ""
                val status = data.insertTodo(todoRoutine)

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
            return 3 // Return 3 if tittle or description is empty
        }


        return 3 // Return 3 if taskType is none of those

    }
    private fun getTodos(){
        clearSavedDates()
        val cal = Calendar.getInstance()
        cal.time = Date()

        cal.set(Calendar.HOUR_OF_DAY,0)
        cal.set(Calendar.MINUTE,0)
        cal.set(Calendar.SECOND,0)
        // Make date and time for today 00:00
        var today0000 = cal.time
        var today = Date()

        var deadlineDate: Date = today
        var sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val todoList = data.getAllTodos()
        Log.e("ppp", "${todoList.size}")
        var alMonday:ArrayList<Todo> = ArrayList()
        var mlMonday = mutableListOf<Todo>()
        var alTuesday:ArrayList<Todo> = ArrayList()
        var mlTuesday = mutableListOf<Todo>()
        var alWednesday:ArrayList<Todo> = ArrayList()
        var mlWednesday = mutableListOf<Todo>()
        var alThursday:ArrayList<Todo> = ArrayList()
        var mlThursday = mutableListOf<Todo>()
        var alFriday:ArrayList<Todo> = ArrayList()
        var mlFriday = mutableListOf<Todo>()
        var alSaturday:ArrayList<Todo> = ArrayList()
        var mlSaturday = mutableListOf<Todo>()
        var alSunday:ArrayList<Todo> = ArrayList()
        var mlSunday = mutableListOf<Todo>()


        for(todo in todoList) {

            // Just for ROUTINE_TASKs
            if(todo.taskType == "ROUTINE_TASK"){
                // task.deadline looks like this dd/mm/yyyy r r r r
                // r - for routine days
                var str1 = todo.deadline.split(" ")
                var todoRoutineDays = ""
                var str = todo.deadline.split(" ")[0] + " " + todo.time

                // We need separate date from routine days
                for(i in str1){
                    if("/" in i){

                    }else{
                        todoRoutineDays = todoRoutineDays +  i + " "
                    }
                }

                // Make a date type "dd/MM/yyyy HH:mm" from string
                deadlineDate = sdf.parse(str)

                // Uncheck routine tasks from yesterday and other days
                var daysDifference0000 = (deadlineDate.time - today0000.time   ) / 86400000
                if(daysDifference0000 > 1){

                    getDateTimeCalendar()
                    todo.deadline = day.toString() + "/"+ (month+1).toString() + "/"+ year.toString()+ " " + daysRoutine
                    todo.state = 0
                    data.updateTodo(todo)
                }

                // Separate routines for day
                for(day in todoRoutineDays.split(" ")){

                    if(day == "1" ){
                        mlMonday.add(todo)
                    }else if(day == "2"){
                        mlTuesday.add(todo)
                    }else if(day == "3"){
                        mlWednesday.add(todo)
                    }else if(day == "4"){
                        mlThursday.add(todo)
                    }else if(day == "5"){
                        mlFriday.add(todo)
                    }else if(day == "6"){
                        mlSaturday.add(todo)
                    }else if(day == "7"){
                        mlSunday.add(todo)
                    }

                }
            }
        }
        var noneMonday = findViewById<TextView>(R.id.tvNoneMonday)
        var noneTuesday = findViewById<TextView>(R.id.tvNoneTuesday)
        var noneWednesday = findViewById<TextView>(R.id.tvNoneWednesday)
        var noneThursday = findViewById<TextView>(R.id.tvNoneThursday)
        var noneFriday = findViewById<TextView>(R.id.tvNoneFriday)
        var noneSaturday = findViewById<TextView>(R.id.tvNoneSaturday)
        var noneSunday = findViewById<TextView>(R.id.tvNoneSunday)
        noneMonday.visibility = View.GONE
        noneTuesday.visibility = View.GONE
        noneWednesday.visibility = View.GONE
        noneThursday.visibility = View.GONE
        noneSaturday.visibility = View.GONE
        noneFriday.visibility = View.GONE
        noneSunday.visibility = View.GONE
        if(mlMonday.isNotEmpty()){
            recyclerViewMonday.visibility = View.VISIBLE

        }else{
            recyclerViewMonday.visibility = View.GONE
            noneMonday.visibility = View.VISIBLE
        }
        if(mlTuesday.isNotEmpty()){
            recyclerViewTuesday.visibility = View.VISIBLE

        }else{
            recyclerViewTuesday.visibility = View.GONE
            noneTuesday.visibility = View.VISIBLE
        }
        if(mlWednesday.isNotEmpty()){
            recyclerViewWednesday.visibility = View.VISIBLE

        }else{
            recyclerViewWednesday.visibility = View.GONE
            noneWednesday.visibility = View.VISIBLE
        }
        if(mlThursday.isNotEmpty()){
            recyclerViewThursday.visibility = View.VISIBLE

        }else{
            recyclerViewThursday.visibility = View.GONE
            noneThursday.visibility = View.VISIBLE
        }
        if(mlFriday.isNotEmpty()){
            recyclerViewFriday.visibility = View.VISIBLE

        }else{
            recyclerViewFriday.visibility = View.GONE
            noneFriday.visibility = View.VISIBLE
        }
        if(mlSaturday.isNotEmpty()){
            recyclerViewSaturday.visibility = View.VISIBLE

        }else{
            recyclerViewSaturday.visibility = View.GONE
            noneSaturday.visibility = View.VISIBLE
        }
        if(mlSunday.isNotEmpty()){
            recyclerViewSunday.visibility = View.VISIBLE

        }else{
            recyclerViewSunday.visibility = View.GONE
            noneSunday.visibility = View.VISIBLE
        }
        //if(uncompleted.isNotEmpty()){
        //    recyclerViewUncompleted.visibility = View.VISIBLE
        //    tvUncompleted.visibility = View.VISIBLE
        //}else{
        //    recyclerViewUncompleted.visibility = View.GONE
        //    tvUncompleted.visibility = View.GONE
        //}
        //if(todays.isNotEmpty() || routineTaskMutableList.isNotEmpty()){

        //    recyclerViewSmallAndRoutineTasks.minimumHeight = (smallAndRoutineTasks.size*180)
        //    recyclerViewToday.visibility = View.VISIBLE
        //    noneTasks.visibility = View.GONE

        //}else{
        //    recyclerViewToday.visibility = View.GONE
        //    noneTasks.visibility = View.VISIBLE
        //}
        //if(tomorrows.isNotEmpty()){
        //    recyclerViewTomorrow.visibility = View.VISIBLE
        //    tvTomorrow.visibility = View.VISIBLE
        //}else{
        //    recyclerViewTomorrow.visibility = View.GONE
        //    tvTomorrow.visibility = View.GONE
        //}
        //if(others.isNotEmpty()){
        //    recyclerViewOther.visibility = View.VISIBLE
        //    tvOther.visibility = View.VISIBLE
        //}else{
        //    recyclerViewOther.visibility = View.GONE
        //    tvOther.visibility = View.GONE
        //}
        recyclerViewMonday.minimumHeight = (mlMonday.size*180)
        recyclerViewTuesday.minimumHeight = (mlTuesday.size*180)
        recyclerViewWednesday.minimumHeight = (mlWednesday.size*180)
        recyclerViewThursday.minimumHeight = (mlThursday.size*180)
        recyclerViewFriday.minimumHeight = (mlFriday.size*180)
        recyclerViewSaturday.minimumHeight = (mlSaturday.size*180)
        recyclerViewSunday.minimumHeight = (mlSunday.size*180)

        alMonday =sortByDateTimeReturnArraylist(mlMonday,"BY_TIME")
        alTuesday =sortByDateTimeReturnArraylist(mlTuesday,"BY_TIME")
        alWednesday =sortByDateTimeReturnArraylist(mlWednesday,"BY_TIME")
        alThursday=sortByDateTimeReturnArraylist(mlThursday,"BY_TIME")
        alFriday =sortByDateTimeReturnArraylist(mlFriday,"BY_TIME")
        alSaturday =sortByDateTimeReturnArraylist(mlSaturday,"BY_TIME")
        alSunday =sortByDateTimeReturnArraylist(mlSunday,"BY_TIME")

        adapterMonday?.addItem(alMonday)
        adapterTuesday?.addItem(alTuesday)
        adapterWednesday?.addItem(alWednesday)
        adapterThursday?.addItem(alThursday)
        adapterFriday?.addItem(alFriday)
        adapterSaturday?.addItem(alSaturday)
        adapterSunday?.addItem(alSunday)
    }
    private fun sortByDateTimeReturnArraylist(mutableList: MutableList<Todo>,how:String):ArrayList<Todo>{
        var arraylist:ArrayList<Todo> = ArrayList()

        if(how == "BY_TIME"){
            mutableList.sortBy { it.time.split(":")[0].toInt()*3600 + it.time.split(":")[1].toInt() *60  }

        }else if(how == "BY_DATETIME"){
            mutableList.sortBy { it.deadline + " " + it.time }
        }
        for (item in  mutableList){
            arraylist.add(item)
        }
        return arraylist
    }

    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
        dayInWeek = cal.get(Calendar.DAY_OF_WEEK)
    }
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        savedDay = day
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()

        TimePickerDialog(this,this,hour,minute,true).show()
    }
    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        savedHour = hour
        if(minute < 10){
            savedMinute = "0" + minute.toString()
        }else{
            savedMinute = minute.toString()
        }

    }
    private fun clearSavedDates(){
        savedDay = 0
        savedMonth = 0
        savedYear = 0
        savedHour = 24
        savedMinute = ""
        priority = ""
        taskType = ""
        daysRoutine = ""
        typeRT = ""
    }

    private fun deleteRoutine(todo:Todo){
        if(todo.id == null) return

        val builder = AlertDialog.Builder(this)
        builder.setMessage("If you delete any task it will be deleted from all days. Are you sure you want to delete this task?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){

                dialog,_->
            data.deleteTodoById(todo.id)
            getTodos()
            dialog.dismiss()

        }
        builder.setNegativeButton("No"){
                dialog,_-> dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    // Initialization functions
    private fun initRecyclerView() {
        // Initialization all recycler views and adapters set
        recyclerViewMonday.layoutManager = LinearLayoutManager(this)
        recyclerViewTuesday.layoutManager = LinearLayoutManager(this)
        recyclerViewWednesday.layoutManager = LinearLayoutManager(this)
        recyclerViewThursday.layoutManager = LinearLayoutManager(this)
        recyclerViewSaturday.layoutManager = LinearLayoutManager(this)
        recyclerViewFriday.layoutManager = LinearLayoutManager(this)
        recyclerViewSunday.layoutManager = LinearLayoutManager(this)
        adapterMonday = TodoAdapterRoutineModifi()
        adapterTuesday= TodoAdapterRoutineModifi()
        adapterWednesday = TodoAdapterRoutineModifi()
        adapterThursday = TodoAdapterRoutineModifi()
        adapterFriday = TodoAdapterRoutineModifi()
        adapterSaturday = TodoAdapterRoutineModifi()
        adapterSunday = TodoAdapterRoutineModifi()
        recyclerViewMonday.adapter = adapterMonday
        recyclerViewTuesday.adapter = adapterTuesday
        recyclerViewWednesday.adapter = adapterWednesday
        recyclerViewThursday.adapter = adapterThursday
        recyclerViewFriday.adapter = adapterFriday
        recyclerViewSaturday.adapter = adapterSaturday
        recyclerViewSunday.adapter = adapterSunday
    }
    private fun initView(){
        btHome = findViewById(R.id.btHome)
        btModifiRoutine = findViewById(R.id.btModifiRutine)
        btGoals = findViewById(R.id.btGoals)
        recyclerViewMonday = findViewById(R.id.rwMonday)
        recyclerViewTuesday = findViewById(R.id.rwTuesday)
        recyclerViewWednesday = findViewById(R.id.rwWednesday)
        recyclerViewThursday = findViewById(R.id.rwThursday)
        recyclerViewFriday = findViewById(R.id.rwFriday)
        recyclerViewSaturday = findViewById(R.id.rwSaturday)
        recyclerViewSunday = findViewById(R.id.rwSunday)
        btRoutineTaskAdd = findViewById(R.id.btAdd)
    }
    private fun addRoutineDay(day: Int){
        if(day.toString() in daysRoutine) {
        }else{
            daysRoutine = daysRoutine + " "  + day.toString()
        }
    }
    private fun ifNothingIsChecked(){
        if(cbSchool.isChecked){ }
        else if(cbProductivity.isChecked){ }
        else if(cbFreetime.isChecked){ }
        else if(cbWorkout.isChecked){ }
        else if(cbSleep.isChecked){ }
        else if(cbFood.isChecked){ }
        else {
            typeRT = ""
        }
    }
    // Identification
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
