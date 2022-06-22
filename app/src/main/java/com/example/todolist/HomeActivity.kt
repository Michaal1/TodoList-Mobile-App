package com.example.todolist

// |||              |||
// ||| IMPORTS HERE |||
// vvv              vvv

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
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.sql.Time
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.chrono.JapaneseEra.values
import java.util.*
import kotlin.Unit.toString
import kotlin.collections.ArrayList
import kotlin.math.min


// |||                    |||
// ||| Home ACTIVITY HERE |||
// vvv                    vvv


class HomeActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // |||                        |||
    // ||| VARIABLE SETTINGS HERE |||
    // vvv                        vvv

    // Variables for date and time set
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var dayInWeek = 0
    var daysRoutine = ""
    var milisecond = 0

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
    private lateinit var btAdd: ImageButton
    private lateinit var btDelete: ImageButton
    private lateinit var recyclerViewToday: RecyclerView
    private lateinit var recyclerViewTomorrow: RecyclerView
    private lateinit var recyclerViewOther: RecyclerView
    private lateinit var recyclerViewUncompleted: RecyclerView
    private lateinit var recyclerViewSmallAndRoutineTasks:RecyclerView
    private lateinit var recyclerViewSmallsTomorrow: RecyclerView
    private lateinit var tvDayTime:TextView
    private lateinit var tvUncompleted:TextView
    private lateinit var tvToday:TextView
    private lateinit var tvTomorrow:TextView
    private lateinit var llTomorrow:LinearLayout
    private lateinit var tvOther:TextView
    private lateinit var llOther:LinearLayout
    private lateinit var cbHigh:CheckBox
    private lateinit var cbMedium:CheckBox
    private lateinit var cbLow:CheckBox
    private lateinit var btAddTask:Button
    private lateinit var btUpdateTask:Button
    private lateinit var btSettings:ImageButton
    private lateinit var tittle:EditText
    private lateinit var description:EditText
    private lateinit var noneTasks:TextView

    private lateinit var cbSchool:CheckBox
    private lateinit var cbProductivity:CheckBox
    private lateinit var cbFreetime:CheckBox
    private lateinit var cbWorkout:CheckBox
    private lateinit var cbSleep:CheckBox
    private lateinit var cbFood:CheckBox

    // Data
    private lateinit var data:Data
    private var todo:Todo? = null
    private var taskType:String = ""

    // Adapters
    private var adapter:TodoAdapter? = null
    private var adapterTomorrow:TodoAdapter? = null
    private var adapterOther:TodoAdapter? = null
    private var adapterUncompleted:TodoAdapter? = null
    private var adapterSmallAndRoutine:TodoAdapterSmallAndRoutine? = null
    private var adapterSmallTomorrow: TodoAdapterSmallAndRoutine? = null

    // Notification
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarm: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private val CHANNEL_ID = "chennel_01"
    private val notificationId = 0

    // Other variables
    private var priority = ""
    private var typeRT = ""
    private var notificationIsSet = false
    var date = ""

    // |||                   |||
    // ||| MAIN PROGRAM HERE |||
    // vvv                   vvv

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        // View initialization
        initView()
        initRecyclerView()

        //binding= ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        //setNotification()


        // Set Data
        data = Data(this)

        // Always set old data to 0
        clearSavedDates()

        // Show all tasks
        getTodos()


        // Other activitys
        btModifiRoutine.setOnClickListener {
            val intent = Intent(this,Activity2::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
            finish()
        }
        btGoals.setOnClickListener {
            val intent = Intent(this,Activity3::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
            finish()
        }
        btSettings.setOnClickListener {
            val sp = PreferenceManager.getDefaultSharedPreferences(this)
            val theme = sp.getString("theme","")
            val color = sp.getString("color","")
            val notification = sp.getBoolean("notification", false)
            val intent = Intent(this,SettingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
            finish()
        }


        // Update database item onCheck
        adapter?.setOnCheck {

            var status = data.updateTodo(it)
            if (status > -1){
            }else{
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
            //getTodos()
        }
        adapterTomorrow?.setOnCheck {

            var status = data.updateTodo(it)
            if (status > -1){
            }else{
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
            //getTodos()
        }
        adapterOther?.setOnCheck {

            var status = data.updateTodo(it)
            if (status > -1){
            }else{
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
            //getTodos()
        }
        adapterUncompleted?.setOnCheck {
            data.deleteTodoById(it.id)
            //getTodos()
        }
        adapterSmallAndRoutine?.setOnCheck {
            extractRoutineDays(it.deadline)
            var status = data.updateTodo(it)
            if (status > -1){
            }else{
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
            //notificationIsSet =false
            //getTodos()

        }
        adapterSmallTomorrow?.setOnCheck {
            extractRoutineDays(it.deadline)
            var status = data.updateTodo(it)
            if (status > -1){
            }else{
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
            //notificationIsSet =false
            //getTodos()
        }


        // On task clicked - show bottom sheet dialog to update task
        adapter?.setOnClickItem {
            clearSavedDates()
            var todo = it
            val bottomSheetDialog = BottomSheetDialog(this@HomeActivity)

            // Create Bottom sheet
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottom_sheet_update,
                findViewById(R.id.bottomSheet)
            )

            // Bottom sheet views initialization
            tvDayTime = bottomSheetView.findViewById(R.id.tvDayTime)
            btUpdateTask = bottomSheetView.findViewById(R.id.btUpdate)
            tittle = bottomSheetView.findViewById(R.id.etTittle)
            description = bottomSheetView.findViewById(R.id.etTask)
            cbHigh = bottomSheetView.findViewById(R.id.cbHigh)
            cbMedium = bottomSheetView.findViewById(R.id.cbMedium)
            cbLow = bottomSheetView.findViewById(R.id.cbLow)

            // Set tasks data
            tittle.setText(it.tittle)
            description.setText(it.description)

            // Set check boxes to checked if tasks priority is set
            if(it.priority == "high"){
                cbHigh.isChecked = true
            }
            if(it.priority == "medium"){
                cbMedium.isChecked = true
            }
            if(it.priority == "low"){
                cbLow.isChecked = true
            }
            priority= it.priority

            // Set priority when check boxes are checked
            cbHigh.setOnClickListener {
                priority = "high"
                cbMedium.isChecked = false
                cbLow.isChecked = false
            }
            cbMedium.setOnClickListener {
                priority = "medium"
                cbHigh.isChecked = false
                cbLow.isChecked = false
            }
            cbLow.setOnClickListener {
                priority = "low"
                cbHigh.isChecked = false
                cbMedium.isChecked = false
            }



            // Chose deadline date and time
            tvDayTime.setOnClickListener {

                // Show calendar
                getDateTimeCalendar()

                DatePickerDialog(this,this,year,month,day).show()
            }

            // Update task button onClick
            btUpdateTask.setOnClickListener {
                // clear priority if no priority checkbox is checked
                if(cbHigh.isChecked ==false && cbMedium.isChecked == false && cbLow.isChecked ==false){
                    priority = ""
                }
                update(tittle.text.toString(),description.text.toString(),priority,todo)
                // hide bottom sheet
                bottomSheetDialog.dismiss()

                // Show all tasks
                getTodos()


            }
            // Set bottom sheet content and show it
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

        // Same as adapter but for Tomorrow tasks adapter
        adapterTomorrow?.setOnClickItem {
            clearSavedDates()
            var todo = it
            val bottomSheetDialog = BottomSheetDialog(this@HomeActivity)

            // Create Bottom sheet
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottom_sheet_update,
                findViewById(R.id.bottomSheet)
            )

            // Bottom sheet views initialization
            tvDayTime = bottomSheetView.findViewById(R.id.tvDayTime)
            btUpdateTask = bottomSheetView.findViewById(R.id.btUpdate)
            tittle = bottomSheetView.findViewById(R.id.etTittle)
            description = bottomSheetView.findViewById(R.id.etTask)
            cbHigh = bottomSheetView.findViewById(R.id.cbHigh)
            cbMedium = bottomSheetView.findViewById(R.id.cbMedium)
            cbLow = bottomSheetView.findViewById(R.id.cbLow)

            // Set tasks data
            tittle.setText(it.tittle)
            description.setText(it.description)

            if(it.priority == "high"){
                cbHigh.isChecked = true
            }
            if(it.priority == "medium"){
                cbMedium.isChecked = true
            }
            if(it.priority == "low"){
                cbLow.isChecked = true
            }
            priority= it.priority

            cbHigh.setOnClickListener {
                priority = "high"
                cbMedium.isChecked = false
                cbLow.isChecked = false
            }
            cbMedium.setOnClickListener {
                priority = "medium"
                cbHigh.isChecked = false
                cbLow.isChecked = false
            }
            cbLow.setOnClickListener {
                priority = "low"
                cbHigh.isChecked = false
                cbMedium.isChecked = false
            }



            // Chose deadline date and time
            tvDayTime.setOnClickListener {

                getDateTimeCalendar()

                DatePickerDialog(this,this,year,month,day).show()
                //bottomSheetDialog.dismiss()
            }
            btUpdateTask.setOnClickListener {
                if(cbHigh.isChecked ==false && cbMedium.isChecked == false && cbLow.isChecked ==false){
                    priority = ""
                }
                update(tittle.text.toString(),description.text.toString(),priority,todo)
                bottomSheetDialog.dismiss()
                getTodos()

            }
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

        // Same as adapter but for Other tasks adapter
        adapterOther?.setOnClickItem {
            clearSavedDates()
            var todo = it
            val bottomSheetDialog = BottomSheetDialog(this@HomeActivity)

            // Create Bottom sheet
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottom_sheet_update,
                findViewById(R.id.bottomSheet)
            )

            // Bottom sheet views initialization
            tvDayTime = bottomSheetView.findViewById(R.id.tvDayTime)
            btUpdateTask = bottomSheetView.findViewById(R.id.btUpdate)
            tittle = bottomSheetView.findViewById(R.id.etTittle)
            description = bottomSheetView.findViewById(R.id.etTask)
            cbHigh = bottomSheetView.findViewById(R.id.cbHigh)
            cbMedium = bottomSheetView.findViewById(R.id.cbMedium)
            cbLow = bottomSheetView.findViewById(R.id.cbLow)

            // Set tasks data
            tittle.setText(it.tittle)
            description.setText(it.description)

            if(it.priority == "high"){
                cbHigh.isChecked = true
            }
            if(it.priority == "medium"){
                cbMedium.isChecked = true
            }
            if(it.priority == "low"){
                cbLow.isChecked = true
            }
            priority= it.priority

            cbHigh.setOnClickListener {
                priority = "high"
                cbMedium.isChecked = false
                cbLow.isChecked = false
            }
            cbMedium.setOnClickListener {
                priority = "medium"
                cbHigh.isChecked = false
                cbLow.isChecked = false
            }
            cbLow.setOnClickListener {
                priority = "low"
                cbHigh.isChecked = false
                cbMedium.isChecked = false
            }



            // Chose deadline date and time
            tvDayTime.setOnClickListener {

                getDateTimeCalendar()

                DatePickerDialog(this,this,year,month,day).show()
                //bottomSheetDialog.dismiss()
            }
            btUpdateTask.setOnClickListener {
                if(cbHigh.isChecked ==false && cbMedium.isChecked == false && cbLow.isChecked ==false){
                    priority = ""
                }
                update(tittle.text.toString(),description.text.toString(),priority,todo)
                bottomSheetDialog.dismiss()
                getTodos()

            }
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

        // Same as adapter but for Uncompleted tasks adapter
        adapterUncompleted?.setOnClickItem {
            clearSavedDates()
            var todo = it
            val bottomSheetDialog = BottomSheetDialog(this@HomeActivity)

            // Create Bottom sheet
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottom_sheet_update,
                findViewById(R.id.bottomSheet)
            )

            // Bottom sheet views initialization
            tvDayTime = bottomSheetView.findViewById(R.id.tvDayTime)
            btUpdateTask = bottomSheetView.findViewById(R.id.btUpdate)
            tittle = bottomSheetView.findViewById(R.id.etTittle)
            description = bottomSheetView.findViewById(R.id.etTask)
            cbHigh = bottomSheetView.findViewById(R.id.cbHigh)
            cbMedium = bottomSheetView.findViewById(R.id.cbMedium)
            cbLow = bottomSheetView.findViewById(R.id.cbLow)

            // Set tasks data
            tittle.setText(it.tittle)
            description.setText(it.description)

            if(it.priority == "high"){
                cbHigh.isChecked = true
            }
            if(it.priority == "medium"){
                cbMedium.isChecked = true
            }
            if(it.priority == "low"){
                cbLow.isChecked = true
            }
            priority= it.priority

            cbHigh.setOnClickListener {
                priority = "high"
                cbMedium.isChecked = false
                cbLow.isChecked = false
            }
            cbMedium.setOnClickListener {
                priority = "medium"
                cbHigh.isChecked = false
                cbLow.isChecked = false
            }
            cbLow.setOnClickListener {
                priority = "low"
                cbHigh.isChecked = false
                cbMedium.isChecked = false
            }

            // Chose deadline date and time
            tvDayTime.setOnClickListener {

                getDateTimeCalendar()

                DatePickerDialog(this,this,year,month,day).show()
                //bottomSheetDialog.dismiss()
            }
            btUpdateTask.setOnClickListener {
                if(cbHigh.isChecked ==false && cbMedium.isChecked == false && cbLow.isChecked ==false){
                    priority = ""
                }
                update(tittle.text.toString(),description.text.toString(),priority,todo)
                bottomSheetDialog.dismiss()
                getTodos()

            }
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

        // Same as adapter but for Routine tasks adapter
        adapterSmallAndRoutine?.setOnClickItem {
            clearSavedDates()
            var todo = it
            if(todo.taskType == "ROUTINE_TASK"){
                val bottomSheetDialog = BottomSheetDialog(this@HomeActivity)

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

            else if(todo.taskType == "SMALL_TASK") {
                val bottomSheetDialog = BottomSheetDialog(this@HomeActivity)

                // Create Bottom sheet
                val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                    R.layout.bottom_sheet_update_small,
                    findViewById(R.id.bottomSheetUpdateSmall)
                )
                // Bottom sheet views initialization
                var tvTime = bottomSheetView.findViewById<TextView>(R.id.tvPickTimeSmall)
                var btUpdateSmall = bottomSheetView.findViewById<Button>(R.id.btUpdateSmall)
                var tittleSmall = bottomSheetView.findViewById<EditText>(R.id.etTittleSmall)
                var cbToday = bottomSheetView.findViewById<CheckBox>(R.id.cbToday)
                var cbTomorrow = bottomSheetView.findViewById<CheckBox>(R.id.cbTomorrow)

                val cal = Calendar.getInstance()
                var date = ""
                cal.time = Date()

                cal.set(Calendar.HOUR_OF_DAY,0)
                cal.set(Calendar.MINUTE,0)
                cal.set(Calendar.SECOND,0)
                // Make date and time for today 00:00
                var today0000 = cal.time
                var today = Date()

                var deadlineDate:Date = today
                var sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")


                cbToday.isChecked = true

                // Set tasks data
                tittleSmall.setText(it.tittle)
                cbSchool = bottomSheetView.findViewById<CheckBox>(R.id.cbSchool)
                cbProductivity = bottomSheetView.findViewById<CheckBox>(R.id.cbProductivity)
                cbFreetime = bottomSheetView.findViewById<CheckBox>(R.id.cbFreetime)
                cbWorkout = bottomSheetView.findViewById<CheckBox>(R.id.cbWorkout)
                cbSleep = bottomSheetView.findViewById<CheckBox>(R.id.cbSleep)
                cbFood = bottomSheetView.findViewById<CheckBox>(R.id.cbFood)


                // Set tasks data

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

                    TimePickerDialog(this, this, hour, minute, true).show()
                }

                // Update task button onClick
                btUpdateSmall.setOnClickListener {
                    var typeRTStorage = typeRT
                    var date= ""
                    var today = LocalDateTime.now()
                    var tomorrow = today.plusDays(1)
                    if(cbToday.isChecked){
                        date = today.dayOfMonth.toString() +"/" + today.monthValue.toString() +"/" + today.year.toString()
                        ifNothingIsChecked()
                        update(tittleSmall.text.toString(), date, "", todo)
                    }else{
                        data.deleteTodoById(todo.id)
                    }
                    if(cbTomorrow.isChecked){
                        typeRT = typeRTStorage
                        taskType = "SMALL_TASK"
                        date = tomorrow.dayOfMonth.toString() +"/" + tomorrow.monthValue.toString() +"/" + tomorrow.year.toString()
                        addTask(tittleSmall.text.toString(), date)

                    }



                    // hide bottom sheet
                    bottomSheetDialog.dismiss()

                    // Show all tasks
                    getTodos()


                }
                // Set bottom sheet content and show it
                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()
            }


        }

        adapterSmallTomorrow?.setOnClickItem{
            clearSavedDates()
            var todo = it

            val bottomSheetDialog = BottomSheetDialog(this@HomeActivity)

            // Create Bottom sheet
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottom_sheet_update_small,
                findViewById(R.id.bottomSheetUpdateSmall)
            )
            // Bottom sheet views initialization
            var tvTime = bottomSheetView.findViewById<TextView>(R.id.tvPickTimeSmall)
            var btUpdateSmall = bottomSheetView.findViewById<Button>(R.id.btUpdateSmall)
            var tittleSmall = bottomSheetView.findViewById<EditText>(R.id.etTittleSmall)
            var cbToday = bottomSheetView.findViewById<CheckBox>(R.id.cbToday)
            var cbTomorrow = bottomSheetView.findViewById<CheckBox>(R.id.cbTomorrow)

            val cal = Calendar.getInstance()
            var date = ""
            cal.time = Date()

            cal.set(Calendar.HOUR_OF_DAY,0)
            cal.set(Calendar.MINUTE,0)
            cal.set(Calendar.SECOND,0)
            // Make date and time for today 00:00
            var today0000 = cal.time
            var today = Date()

            var deadlineDate:Date = today
            var sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")


            cbTomorrow.isChecked = true

            // Set tasks data
            tittleSmall.setText(it.tittle)
            cbSchool = bottomSheetView.findViewById<CheckBox>(R.id.cbSchool)
            cbProductivity = bottomSheetView.findViewById<CheckBox>(R.id.cbProductivity)
            cbFreetime = bottomSheetView.findViewById<CheckBox>(R.id.cbFreetime)
            cbWorkout = bottomSheetView.findViewById<CheckBox>(R.id.cbWorkout)
            cbSleep = bottomSheetView.findViewById<CheckBox>(R.id.cbSleep)
            cbFood = bottomSheetView.findViewById<CheckBox>(R.id.cbFood)


            // Set tasks data

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

                TimePickerDialog(this, this, hour, minute, true).show()
            }

            // Update task button onClick
            btUpdateSmall.setOnClickListener {
                var typeRTStorage = typeRT
                var date= ""
                var today = LocalDateTime.now()
                var tomorrow = today.plusDays(1)
                if(cbToday.isChecked){

                    taskType = "SMALL_TASK"
                    date = today.dayOfMonth.toString() +"/" + today.monthValue.toString() +"/" + today.year.toString()
                    addTask(tittleSmall.text.toString(), date)


                }

                if(cbTomorrow.isChecked){
                    typeRT = typeRTStorage
                    date = tomorrow.dayOfMonth.toString() +"/" + tomorrow.monthValue.toString() +"/" + tomorrow.year.toString()
                    ifNothingIsChecked()
                    update(tittleSmall.text.toString(), date, "", todo)

                }else {
                    data.deleteTodoById(todo.id)
                }


                // hide bottom sheet
                bottomSheetDialog.dismiss()

                // Show all tasks
                getTodos()


            }
            // Set bottom sheet content and show it
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

        }

        // Delete button onClick
        btDelete.setOnClickListener { delete() }

        // Bottom sheet Choose functionality - choose what type of task do you want to add
        btAdd.setOnClickListener {
            clearSavedDates()
            val bottomSheetDialog = BottomSheetDialog(this@HomeActivity)

            // Create Bottom sheet
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottom_sheet_choose,
                findViewById(R.id.bottomSheetChoose)
            )

            // Bottom sheet views initialization
            var tvTask = bottomSheetView.findViewById<TextView>(R.id.tvTask)
            var tvSmallTask= bottomSheetView.findViewById<TextView>(R.id.tvSmallTask)
            var tvRoutineTask = bottomSheetView.findViewById<TextView>(R.id.tvRoutineTask)

            // On tvTask click we show bottom sheet add task and than add TASK
            tvTask.setOnClickListener {
                // Hide bottom sheet for choose
                bottomSheetDialog.dismiss()

                clearSavedDates()
                taskType = "TASK"
                val bottomSheetDialog = BottomSheetDialog(this@HomeActivity)

                // Create Bottom sheet
                val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                    R.layout.bottom_sheet,
                    findViewById(R.id.bottomSheet)
                )

                // Bottom sheet views initialization
                tvDayTime = bottomSheetView.findViewById(R.id.tvDayTime)
                btAddTask = bottomSheetView.findViewById(R.id.btAdd)
                tittle = bottomSheetView.findViewById(R.id.etTittle)
                description = bottomSheetView.findViewById(R.id.etTask)
                cbHigh = bottomSheetView.findViewById(R.id.cbHigh)
                cbMedium = bottomSheetView.findViewById(R.id.cbMedium)
                cbLow = bottomSheetView.findViewById(R.id.cbLow)



                // Chose deadline date and time
                tvDayTime.setOnClickListener {

                    getDateTimeCalendar()

                    DatePickerDialog(this, this, year, month, day).show()
                    //bottomSheetDialog.dismiss()
                }

                // Set priority on check box checked
                cbHigh.setOnClickListener {
                    priority = "high"
                    cbMedium.isChecked = false
                    cbLow.isChecked = false
                }
                cbMedium.setOnClickListener {
                    priority = "medium"
                    cbHigh.isChecked = false
                    cbLow.isChecked = false
                }
                cbLow.setOnClickListener {
                    priority = "low"
                    cbHigh.isChecked = false
                    cbMedium.isChecked = false
                }

                // Add a new task
                btAddTask.setOnClickListener {
                    if (cbHigh.isChecked == false && cbMedium.isChecked == false && cbLow.isChecked == false) {
                        priority = ""
                    }

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

            // On tvSmallTask click we show bottom sheet add task and than add SMALLTASK
            tvSmallTask.setOnClickListener {
                // Hide bottom seet for choose
                bottomSheetDialog.dismiss()

                clearSavedDates()
                taskType = "SMALL_TASK"
                val bottomSheetDialog = BottomSheetDialog(this@HomeActivity)

                // Create Bottom sheet
                val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                    R.layout.bottom_sheet_add_small,
                    findViewById(R.id.bottomSheet)
                )

                // Bottom sheet views initialization
                var tvPickTimeSmall = bottomSheetView.findViewById<TextView>(R.id.tvPickTimeSmall)
                var btAdd = bottomSheetView.findViewById<Button>(R.id.btAddSmall)
                tittle = bottomSheetView.findViewById(R.id.etTittleSmall)
                var cbToday = bottomSheetView.findViewById<CheckBox>(R.id.cbToday)
                var cbTomorrow = bottomSheetView.findViewById<CheckBox>(R.id.cbTomorrow)
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


                // Chose deadline date and time
                tvPickTimeSmall.setOnClickListener {

                    getDateTimeCalendar()

                    TimePickerDialog(this, this, hour, minute,true).show()
                    //bottomSheetDialog.dismiss()
                }

                // Add a new task
                btAdd.setOnClickListener {
                    var  date= ""
                    var today = LocalDateTime.now()
                    var tomorrow = today.plusDays(1)
                    if(cbToday.isChecked){
                        date = today.dayOfMonth.toString() +"/" + today.monthValue.toString() +"/" + today.year.toString()
                    }
                    if(cbTomorrow.isChecked){
                        if(date == ""){
                            date = date  + tomorrow.dayOfMonth.toString() +"/" + tomorrow.monthValue.toString() +"/" + tomorrow.year.toString()
                        }else{
                            date = date + " " + tomorrow.dayOfMonth.toString() +"/" + tomorrow.monthValue.toString() +"/" + tomorrow.year.toString()
                        }
                    }
                    ifNothingIsChecked()
                    // Check successful insert
                    var success = addTask(tittle.text.toString(), date)
                    if (success == 2) { // When not passed a date
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


            tvRoutineTask.setOnClickListener {
                // Hide bottom sheet for choose
                bottomSheetDialog.dismiss()

                clearSavedDates()
                taskType = "ROUTINE_TASK"
                val bottomSheetDialog = BottomSheetDialog(this@HomeActivity)

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
            // Show Bottom sheet
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }
    }

    // |||                |||
    // ||| FUNCTIONS HERE |||
    // vvv                vvv

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
        // Just for TASKs
        if(todo.taskType == "TASK"){

            if(tittleText.isEmpty() || descriptionText.isEmpty()){

            }else{

                var dayData = todo.deadline
                var time = todo.time
                if(savedYear != 0) {
                    dayData = savedDay.toString() + "/" + (savedMonth + 1).toString() + "/" + savedYear.toString()

                }
                if(savedHour !=24){
                    time = savedHour.toString() + ":" + savedMinute
                }
                todo.tittle = tittleText
                todo.description = descriptionText
                todo.priority = priority
                todo.deadline = dayData
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

        else if(todo.taskType == "ROUTINE_TASK"){
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

        else if(todo.taskType == "SMALL_TASK"){
            if(tittleText.isEmpty()){

            }else{

                var time = todo.time

                if(savedHour !=24){
                    time = savedHour.toString() + ":" + savedMinute
                }
                date = descriptionText
                todo.tittle = tittleText
                todo.description = descriptionText
                todo.priority = typeRT
                todo.deadline = date
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

    // Delete Task
    private fun delete(){
        val cal = Calendar.getInstance()
        cal.time = Date()

        cal.set(Calendar.HOUR_OF_DAY,0)
        cal.set(Calendar.MINUTE,0)
        cal.set(Calendar.SECOND,0)

        var today0000 = cal.time
        var today = Date()
        var deadlineDate:Date = today
        var sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")



        var todoList = data.getAllTodos()
        for (todo in todoList){
            if(todo.taskType == "TASK"){
                if (todo.state ==1){
                    var str = todo.deadline + " " + todo.time
                    deadlineDate = sdf.parse(str)
                    var daysDifference = (deadlineDate.time - today.time) / 86400000
                    var secondDifference = (deadlineDate.time - today.time ) / 10000

                    if (secondDifference < 0) {
                        adapterUncompleted?.deleteDoneToDos(todo)
                    }
                    else if (daysDifference < 1) {
                        adapter?.deleteDoneToDos(todo)
                    }
                    else if (daysDifference < 2 ) {
                        adapterTomorrow?.deleteDoneToDos(todo)
                    }
                    else {
                        adapterOther?.deleteDoneToDos(todo)
                    }

                    data.deleteTodoById(todo.id)
                }
            }

            if(todo.taskType == "SMALL_TASK"){
                if (todo.state ==1){
                    adapterSmallAndRoutine?.deleteDoneToDos(todo)

                    data.deleteTodoById(todo.id)
                }
            }
        }
        getTodos()
    }

    // Add Task
    private fun addTask(tittle:String,description:String):Int{
        // Just for TASKs
        // When we want to create TASK taskType == "TASK"
        var tittleText = tittle
        var descriptionText = description
        if(taskType == "TASK"){
            var todo:Todo
            if(tittleText.isEmpty() || descriptionText.isEmpty()){
            }else{
                if(savedYear != 0 && savedHour != 24 ) {
                    var dayData = savedDay.toString() +"/" + (savedMonth + 1).toString()  + "/" +  savedYear.toString()
                    var time = savedHour.toString() + ":" + savedMinute
                    todo = Todo(tittle = tittleText, description = descriptionText, deadline = dayData, time = time,
                        priority = priority, taskType = taskType, id= createId())
                    priority = ""
                    taskType = ""
                    val status = data.insertTodo(todo)

                    // check successful insert
                    if (status> -1){
                        //setAlarm(todo)
                        Toast.makeText(this, "Todo Added...", Toast.LENGTH_SHORT).show()
                        clearEditText()
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
        // Just for ROUTINE_TASKs
        // When we want to create ROUTINE_TASK taskType == "ROUTINE_TASK"
        else if(taskType == "ROUTINE_TASK"){
            val cal = Calendar.getInstance()
            cal.time = Date()
            if(tittleText.isEmpty() ){
            }else{
                Toast.makeText(this, savedHour.toString(), Toast.LENGTH_SHORT).show()
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
        // Just for SMALL_TASKs
        // When we want to create ROUTINE_TASK taskType == "ROUTINE_TASK"
        else if(taskType == "SMALL_TASK"){

            var date = description.split(" ")
            if(tittleText.isEmpty() ){
            }else{

                if(savedHour == 24 ){
                    savedHour = 23
                    savedMinute = "59"
                }
                for(i in date){

                    var time = savedHour.toString() + ":" + savedMinute
                    var deadline =  i
                    var todoRoutine = Todo(tittle = tittleText, description = "", time = time,
                        priority = typeRT, taskType = taskType, deadline = deadline, id= createId())

                    val status = data.insertTodo(todoRoutine)
                    // check successful insert
                    if (status> -1){
                        //setAlarm(todo)
                        Toast.makeText(this, "Todo Added...", Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(this, "Record not saved...", Toast.LENGTH_SHORT).show()
                    }

                }
                priority = ""
                taskType = ""
                getTodos()

                return 1 // Return 1 if everything is good


            }
            return 3 // Return 3 if tittle or description is empty
        }

        return 3 // Return 3 if taskType is none of those

    }

    // Get todos
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

        var deadlineDate:Date = today
        var sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val todoList = data.getAllTodos()
        Log.e("ppp", "${todoList.size}")
        var uncompleted:ArrayList<Todo> = ArrayList()
        var todays:ArrayList<Todo> = ArrayList()
        var tomorrows:ArrayList<Todo> = ArrayList()
        var others:ArrayList<Todo> = ArrayList()
        var smallAndRoutineTasks:ArrayList<Todo> = ArrayList()
        var smallForTomorrowArray:ArrayList<Todo> = ArrayList()
        var routineTaskMutableList = mutableListOf<Todo>()
        var allTasksForToday = mutableListOf<Todo>()
        var allTasksForTomorrow = mutableListOf<Todo>()
        var smallForTomorrow = mutableListOf<Todo>()


        for(todo in todoList) {
            if(todo.taskType == "SMALL_TASK"){

                if(todo.deadline.isNotEmpty()){

                    var str1 = todo.deadline+" " + todo.time
                    // Make a date type "dd/MM/yyyy HH:mm" from string
                    deadlineDate = sdf.parse(str1)
                    var daysDifference0000 = (deadlineDate.time  - today0000.time) / 60000

                    if(daysDifference0000 < 0 ){
                        data.deleteTodoById(todo.id)
                        break
                    }else if(daysDifference0000.toInt() < (23*60+59) ) {
                        if (todo.state == 0) {
                            allTasksForToday.add(todo)
                        }

                        routineTaskMutableList.add(todo)


                    }else if(daysDifference0000.toInt() <= 48*60 ){
                        if (todo.state == 0) {
                            allTasksForTomorrow.add(todo)
                        }
                        smallForTomorrow.add(todo)
                    }

                }else{
                    data.deleteTodoById(todo.id)
                }


                //var str = todo.deadline.split(" ")[1] + " " + todo.time


            }
            // Just for ROUTINE_TASKs
            if(todo.taskType == "ROUTINE_TASK"){
                // task.deadline looks like this dd/mm/yyyy r r r r
                // r - for routine days
                var str1 = todo.deadline.split(" ")
                var todoRoutineDays = ""
                var str = todo.deadline.split(" ")[0] + " 00:00"

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
                var daysDifference0000 = (today0000.time   - deadlineDate.time ) / 360000
                if(daysDifference0000 > 24){

                    getDateTimeCalendar()
                    todo.deadline = day.toString() + "/"+ (month+1).toString() + "/"+ year.toString()+ " " + todoRoutineDays
                    todo.state = 0
                    data.updateTodo(todo)
                }

                // Show only routine for today
                if(todoRoutineDays == "" || todoRoutineDays == " "){
                    data.deleteTodoById(todo.id)
                }

                for(day in todoRoutineDays.split(" ")){
                    var dayInWeekReal = 0
                    getDateTimeCalendar()
                    if(dayInWeek == 1){
                        dayInWeekReal = 7
                    }else{
                        dayInWeekReal = dayInWeek-1
                    }

                    if((dayInWeekReal).toString() == day){
                        if(todo.state ==0){

                            allTasksForToday.add(todo)
                        }
                        routineTaskMutableList.add(todo)

                    }
                    if((dayInWeek).toString() == day){
                        if(todo.state ==0) {
                            allTasksForTomorrow.add(todo)
                        }
                    }

                }
            }
            // Just for TASKs
            if(todo.taskType == "TASK"){
                var str = todo.deadline + " " + todo.time
                deadlineDate = sdf.parse(str)

                var daysDifference0000 = (deadlineDate.time - today0000.time ) / 86400000
                var secondDifference = (deadlineDate.time - today.time ) / 1000
                if (secondDifference <= 0) {
                    if(todo.state ==1){
                        data.deleteTodoById(todo.id)
                    }else{
                        uncompleted.add(todo)
                    }

                }
                else if (daysDifference0000 < 1) {
                    todays.add(todo)
                    if(todo.state ==0){
                        allTasksForToday.add(todo)
                    }

                }
                else if (daysDifference0000 < 2 ) {
                    tomorrows.add(todo)
                    if(todo.state ==0) {
                        allTasksForTomorrow.add(todo)
                    }
                }
                else {
                    others.add(todo)
                }
            }
            if(todo.taskType.isEmpty()){
                data.deleteTodoById(todo.id)
            }

        }

        if(uncompleted.isNotEmpty()){
            recyclerViewUncompleted.visibility = View.VISIBLE
            tvUncompleted.visibility = View.VISIBLE
        }else{
            recyclerViewUncompleted.visibility = View.GONE
            tvUncompleted.visibility = View.GONE
        }
        if(todays.isNotEmpty() || routineTaskMutableList.isNotEmpty()){

            recyclerViewSmallAndRoutineTasks.minimumHeight = (smallAndRoutineTasks.size*200)
            recyclerViewToday.visibility = View.VISIBLE
            noneTasks.visibility = View.GONE

        }else{
            recyclerViewToday.visibility = View.GONE
            noneTasks.visibility = View.VISIBLE
        }
        if(tomorrows.isNotEmpty() || smallForTomorrow.isNotEmpty()){
            recyclerViewTomorrow.visibility = View.VISIBLE
            recyclerViewSmallsTomorrow.visibility = View.VISIBLE
            tvTomorrow.visibility = View.VISIBLE
            llTomorrow.visibility = View.VISIBLE
        }else{
            recyclerViewTomorrow.visibility = View.GONE
            recyclerViewSmallsTomorrow.visibility = View.GONE
            tvTomorrow.visibility = View.GONE
            llTomorrow.visibility = View.GONE
        }
        if(others.isNotEmpty()){
            recyclerViewOther.visibility = View.VISIBLE
            tvOther.visibility = View.VISIBLE
            llOther.visibility = View.VISIBLE
        }else{
            recyclerViewOther.visibility = View.GONE
            tvOther.visibility = View.GONE
            llOther.visibility = View.GONE
        }



        smallAndRoutineTasks =sortByDateTimeReturnArraylist(routineTaskMutableList,"BY_TIME")
        smallForTomorrowArray =sortByDateTimeReturnArraylist(smallForTomorrow,"BY_TIME")
        var firstTomorrowsTask = sortByDateTimeReturnArraylist(allTasksForTomorrow,"BY_TIME")
        var todosForNotification = sortByDateTimeReturnArraylist(allTasksForToday,"BY_TIME")
        if(firstTomorrowsTask.isNotEmpty()) {
            todosForNotification.add(firstTomorrowsTask[0])
        }
        if(notificationIsSet==false){
            if(todosForNotification.isNotEmpty()){
                var item = -1
                val cal = Calendar.getInstance()
                cal.time = Date()
                getDateTimeCalendar()
                val today = Date()
                var deadlineDate:Date = today
                var daysDifference = -1.toLong()
                var todo1 = todosForNotification[0]
                for(todo in todosForNotification){

                    var sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                    var str = day.toString() + "/"+ (month+1).toString() + "/"+ year.toString()+ " " + todo.time
                    deadlineDate = sdf.parse(str)
                    daysDifference = (deadlineDate.time - today.time)

                    if (daysDifference > 0 ){
                        if(todo.state ==0){

                            todo1 = todo
                            setAlarm(todo1,daysDifference)
                            break
                        }

                    }
                }
            }
        }


        adapterOther?.addItem(others)
        adapterTomorrow?.addItem(tomorrows)
        adapter?.addItem(todays)
        adapterUncompleted?.addItem(uncompleted)
        adapterSmallAndRoutine?.addItem(smallAndRoutineTasks)
        adapterSmallTomorrow?.addItem(smallForTomorrowArray)

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

    // Clear Bottom sheets EditTexts
    private fun clearEditText(){
        tittle.setText("")
        description.setText("")
    }

    // Date and time functions
    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
        milisecond = cal.get(Calendar.MILLISECOND)
        dayInWeek = cal.get(Calendar.DAY_OF_WEEK)
    }
    private fun addRoutineDay(day: Int){
        if(day.toString() in daysRoutine) {
        }else{
            daysRoutine = daysRoutine + " "  + day.toString()
        }
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

    // Notification
    private fun setAlarm(todo:Todo,daysDifference:Long){



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val sp = PreferenceManager.getDefaultSharedPreferences(this)
            val notification = sp.getBoolean("notification", false)
            if(notification == false){
                var name: CharSequence = todo.id.toString()
                var description = "Chanel for Alarm manager for " + "task"
                var importance = NotificationManager.IMPORTANCE_HIGH
                var channel = NotificationChannel("hoo".toString(),name,importance).apply {
                    description = description
                }
                //channel.description = description
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)



                alarm = getSystemService(ALARM_SERVICE) as AlarmManager
                val intent =Intent(this,AlarmReciever::class.java)
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
                intent.putExtra("tittle", todo.tittle)
                intent.putExtra("description", todo.description)
                intent.putExtra("id", todo.id)

                pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
                var sec = System.currentTimeMillis()
                alarm.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,sec + daysDifference,
                    AlarmManager.INTERVAL_DAY,pendingIntent,
                )
            }
        }




    }

    // Initialization functions
    private fun initRecyclerView() {
        // Initialization all recycler views and adapters set
        recyclerViewToday.layoutManager = LinearLayoutManager(this)
        recyclerViewTomorrow.layoutManager = LinearLayoutManager(this)
        recyclerViewOther.layoutManager = LinearLayoutManager(this)
        recyclerViewUncompleted.layoutManager = LinearLayoutManager(this)
        recyclerViewSmallAndRoutineTasks.layoutManager = LinearLayoutManager(this)
        recyclerViewSmallsTomorrow.layoutManager = LinearLayoutManager(this)
        adapter = TodoAdapter()
        adapterTomorrow = TodoAdapter()
        adapterOther = TodoAdapter()
        adapterUncompleted = TodoAdapter()
        adapterSmallAndRoutine = TodoAdapterSmallAndRoutine()
        adapterSmallTomorrow = TodoAdapterSmallAndRoutine()
        recyclerViewToday.adapter = adapter
        recyclerViewTomorrow.adapter = adapterTomorrow
        recyclerViewOther.adapter = adapterOther
        recyclerViewUncompleted.adapter = adapterUncompleted
        recyclerViewSmallAndRoutineTasks.adapter = adapterSmallAndRoutine
        recyclerViewSmallsTomorrow.adapter = adapterSmallTomorrow
    }
    private fun initView(){
        var calendar = Calendar.getInstance()
        btHome = findViewById(R.id.btHome)
        btModifiRoutine = findViewById(R.id.btModifiRutine)
        btGoals = findViewById(R.id.btGoals)
        btAdd = findViewById(R.id.btAdd)
        btSettings = findViewById(R.id.settings)
        btDelete = findViewById(R.id.btDelete)
        recyclerViewToday = findViewById(R.id.rwListOfTodayTodos)
        recyclerViewTomorrow = findViewById(R.id.rwListOfTomorrowTodos)
        recyclerViewOther = findViewById(R.id.rwListOfOtherTodos)
        recyclerViewUncompleted = findViewById(R.id.rwListOfUncompletedTodos)
        recyclerViewSmallAndRoutineTasks = findViewById(R.id.rwListOfSmallAndRoutineTodos)
        recyclerViewSmallsTomorrow = findViewById(R.id.rwSmallsTomorrow)
        tvUncompleted = findViewById(R.id.tvUncompleted)
        tvToday = findViewById(R.id.tvToday)
        tvTomorrow = findViewById(R.id.tvTomorrow)
        tvOther = findViewById(R.id.tvOther)
        noneTasks = findViewById(R.id.tvNoneTasks)
        llTomorrow = findViewById(R.id.llTomorrow)
        llOther = findViewById(R.id.llOther)

        var todayDateView = findViewById<TextView>(R.id.tvTodayDate)
        var todayDay = findViewById<TextView>(R.id.day)
        var tomorrowDateView = findViewById<TextView>(R.id.tvTomorrowDate)
        getDateTimeCalendar()
        var dayOfWeek = ""
        if(dayInWeek == 1){
            dayOfWeek = "Sunday"
        }else if(dayInWeek == 2){
            dayOfWeek = "Monday"
        }else if(dayInWeek == 3){
            dayOfWeek = "Tuesday"
        }else if(dayInWeek == 4){
            dayOfWeek = "Wednesday"
        }else if(dayInWeek == 5){
            dayOfWeek = "Thursday"
        }else if(dayInWeek == 6){
            dayOfWeek = "Friday"
        }else if(dayInWeek == 7){
            dayOfWeek = "Saturday"
        }

        var months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        //if(dayInWeekReal == 1){day = "Monday" }

        //var sdf = SimpleDateFormat("EEEE")

        todayDay.text = "Today is " + dayOfWeek //DateFormatSymbols().getShortWeekdays()[(calendar.get(Calendar.DAY_OF_WEEK)) ]//sdf.format(Date())//calendar.get(Calendar.Day).toString()
        todayDateView.text =  months[calendar.get(Calendar.MONTH)]+ " " + day.toString()

        var today = LocalDateTime.now()
        var tomorrow = today.plusDays(1)

        tomorrowDateView.text = months[tomorrow.monthValue-1] + " " + tomorrow.dayOfMonth.toString()


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