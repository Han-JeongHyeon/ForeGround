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
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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

class MyService : Service(), SensorEventListener{

    companion object {
        private const val TAG = "MyServiceTag"

        var i = 0

        var text = ""
    }

    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    var notificationManager: NotificationManager? = null
    var builder: NotificationCompat.Builder? = null

    var custom: RemoteViews? = null

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        createNotification()
        onResume()

        text = "측정 가능"
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun createNotification() {
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent
            .getActivity(applicationContext, 0, notificationIntent, PendingIntent.FLAG_MUTABLE)

        custom = RemoteViews(packageName, R.layout.custom_small)

        builder = NotificationCompat.Builder(this, "MY_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setContent(custom)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel_id = "MY_channel"
            val channel_name = "채널이름"
            val descriptionText = "설명글"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channel_id, channel_name, importance).apply {
                description = descriptionText
            }

            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager!!.createNotificationChannel(channel)

        }

        startForeground(1002, builder!!.build())

    }

    private fun onResume() {
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
            SensorManager.SENSOR_DELAY_FASTEST
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_STEP_COUNTER &&
            text != "측정 불가"){

            val intent = Intent()

            custom!!.setTextViewText(R.id.walk,"총 걸음 수 : $i")
            i++
            notificationManager!!.notify(1002, builder!!.build())

            intent.action = "walk"
            intent.putExtra("value", if (i == 0) 0 else i -1)
            sendBroadcast(intent)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()

        text = "측정 불가"
        custom!!.setTextViewText(R.id.walk,"측정 시작을 눌러주세요")
        notificationManager!!.notify(1002, builder!!.build())
    }
}