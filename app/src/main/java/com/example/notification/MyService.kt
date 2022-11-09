package com.example.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

class MyService : Service(){

    private var steps = 0

    private var customViewSmall: RemoteViews? = null

    private var pendingIntent: PendingIntent? = null

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        getPendingIntent()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        steps = intent!!.getIntExtra("steps", 0)

        customViewSmall = RemoteViews(packageName, R.layout.custom_small)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.sneaker_element)
            .setCustomContentView(customViewSmall)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notification: Notification = builder.build()

        customViewSmall!!.setTextViewText(R.id.walk,"걸음 수 : $steps")

        startForeground(notificationId, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun getPendingIntent(){
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK

        pendingIntent = PendingIntent
            .getActivity(applicationContext, 0, notificationIntent, PendingIntent.FLAG_MUTABLE)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "만보기"
            val descriptionText = "만보기 앱입니다."
            val importance = NotificationManager.IMPORTANCE_LOW

            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

    }

}