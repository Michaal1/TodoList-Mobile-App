package com.example.todolist

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReciever: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context,MainActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT)
        var id = intent.getIntExtra("id",0).toString()
        val builder = NotificationCompat.Builder(context!!, "hoo")
            .setSmallIcon(R.mipmap.ic_app_icon4)
            .setContentTitle(intent.getStringExtra("tittle"))
            .setContentText(intent.getStringExtra("description"))
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        Toast.makeText(context,intent.getStringExtra("tittle") + id, Toast.LENGTH_SHORT).show()
        val NotificationManager = NotificationManagerCompat.from(context)
        NotificationManager.notify(123,builder.build())

    }

}