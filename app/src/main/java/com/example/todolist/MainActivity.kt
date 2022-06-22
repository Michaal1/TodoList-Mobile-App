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
import android.os.Handler
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
// ||| MAIN ACTIVITY HERE |||
// vvv                    vvv


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_top)
        },1500)
    }
}