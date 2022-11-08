package com.example.notification

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    var stepCount: TextView? = null

    private var stepCounter: StepCount? = null
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    private var steps = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        sensorManager.registerListener(
//            this,
//            sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
//            SensorManager.SENSOR_DELAY_FASTEST
//        )

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val stop = findViewById<Button>(R.id.stop)
        val start = findViewById<Button>(R.id.start)
        stepCount = findViewById<TextView>(R.id.stepCount)

        val broadcast = MyBroadcastReceiver()
        val filter = IntentFilter()

        filter.addAction(getString(R.string.walk))

        registerReceiver(broadcast, filter)

        stepCount!!.text = " ${intent.getIntExtra("value", 0)}"

        val intent = Intent(this@MainActivity, MyService::class.java)

        start.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        stop.setOnClickListener {
            stopService(intent)
        }

        stepCounter = StepCount(object : StepCount.StepDetector {
            @SuppressLint("SetTextI18n")
            override fun onStepDetected() {
                steps++
                Log.d("TAG", "$steps")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
            }
        })

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 0)
            }
        }

    }

    inner class MyBroadcastReceiver : BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action == "walk")
                stepCount!!.text = " ${intent.getIntExtra("value", 0)}"
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            0 -> {
                if (grantResults.isNotEmpty()){
                    var isAllGranted = true
                    for (grant in grantResults) {
                        if (grant != PackageManager.PERMISSION_GRANTED) {
                            isAllGranted = false
                            break
                        }
                    }

                    if (isAllGranted) {

                    }
                    else {
                        if(!ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.ACTIVITY_RECOGNITION)){
                            //팝업 띄어서 권한 설정하기
                        } else {

                        }
                    }
                }
            }
        }
    }

}