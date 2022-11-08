package com.example.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
* 서비스 종료 함수 만들기
* notification 꾸미기
* */

class MyService : Service(){

    companion object {
        private const val TAG = "MyServiceTag"

        var step = 0
        var sensor = ""
        var channelId = 0
    }

    private var notificationManager: NotificationManager? = null
    private var builder: NotificationCompat.Builder? = null

    private var customViewSmall: RemoteViews? = null
    private var customViewBig: RemoteViews? = null

    var pendingIntent: PendingIntent? = null

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        createNotification()

//        sensor = "측정 가능"
//        if (step != 0) step--
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        builder = NotificationCompat.Builder(this, "MY_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setCustomContentView(customViewSmall)
            .setCustomBigContentView(customViewBig)

        startForeground(channelId, builder!!.build())

        notificationManager!!.notify(channelId, builder!!.build())

        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("RemoteViewLayout")
    private fun createNotification() {
//        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
//        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
//                Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//        pendingIntent = PendingIntent
//            .getActivity(applicationContext, 0, notificationIntent, PendingIntent.FLAG_MUTABLE)
//
//        customViewSmall = RemoteViews(packageName, R.layout.custom_small)
//        customViewBig = RemoteViews(packageName, R.layout.custom_big)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "MY_channel"
            val channelName = "만보기"
            val descriptionText = "만보기 앱입니다."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = descriptionText
            }

            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager!!.createNotificationChannel(channel)
        }

//        customViewSmall!!.setTextViewText(R.id.walk,"걸음 수 : ${step}")
//        customViewBig!!.setTextViewText(R.id.walk,"걸음 수 : ${step}")
//
//        notificationManager!!.notify(channelId, builder!!.build())

//        val intent = Intent()
//
//        intent.action = getString(R.string.walk)
//        intent.putExtra("value", if (step == 0) 0 else step - 1)
//
//        sendBroadcast(intent)


    }

//    override fun onDestroy() {
//        super.onDestroy()
//
//        sensor = "측정 불가"
//
//        customViewSmall!!.setTextViewText(R.id.walk,"측정 시작을 눌러주세요")
//        customViewBig!!.setTextViewText(R.id.walk,"측정 시작을 눌러주세요")
//
//        notificationManager!!.notify(channelId, builder!!.build())
//    }
}