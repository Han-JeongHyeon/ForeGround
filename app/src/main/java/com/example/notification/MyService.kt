package com.example.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyService : Service(), SensorEventListener{

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        createNotification()
        onResume()
        Log.d(TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    var notificationManager: NotificationManager? = null
    var builder: NotificationCompat.Builder? = null
    private fun createNotification() {
        builder = NotificationCompat.Builder(this, "MY_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("만보기 어플")
            .setContentText("걸음 : $i")
            .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 오레오 버전 이후에는 알림을 받을 때 채널이 필요
            val channel_id = "MY_channel" // 알림을 받을 채널 id 설정
            val channel_name = "채널이름" // 채널 이름 설정
            val descriptionText = "설명글" // 채널 설명글 설정
            val importance = NotificationManager.IMPORTANCE_DEFAULT // 알림 우선순위 설정
            val channel = NotificationChannel(channel_id, channel_name, importance).apply {
                description = descriptionText
            }

            // 만든 채널 정보를 시스템에 등록
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager!!.createNotificationChannel(channel)

//            CoroutineScope(Dispatchers.Default).launch {
//                while (true) {
//                    notificationManager!!.notify(1002, builder!!.build())
//                    delay(1000)
//                    i++
//                    builder!!.setContentText("걸음 : $i")
//                }
//            }

        }

    }

    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private fun onResume() {
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
            SensorManager.SENSOR_DELAY_FASTEST
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_STEP_DETECTOR){

            CoroutineScope(Dispatchers.Default).launch {
                while (true) {
                    notificationManager!!.notify(1002, builder!!.build())
                    delay(1000)
                    i++
                    builder!!.setContentText("걸음 : $i")
                }
            }

//            notificationManager!!.notify(1002, builder!!.build())
//            i++
//            builder!!.setContentText("걸음 : $i")

        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
//        stopSelf()
        Toast.makeText(baseContext,"onDestroy",Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val TAG = "MyServiceTag"

        var i = 0

        // Notification
        private const val NOTI_ID = 1
    }
}