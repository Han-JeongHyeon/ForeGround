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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var stepCount: StepCount? = null
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null

    var steps = 0

    var sensorOn = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        stepCount = StepCount(object: StepCount.StepCounter{
            override fun stepCount() {
                steps++
                binding.stepCount.text = "$steps"
                startForegroundService(steps)
            }
        })

        binding.start.setOnClickListener {
            sensorOn = !sensorOn
            if (sensorOn) {
                binding.start.text = "측정 가능"
                startForegroundService(steps)
            } else {
                binding.start.text = "측정 중지"
                stopForegroundService()
            }
        }

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 0)
            }
        }

    }

    private fun startForegroundService(steps: Int) {
        val serviceIntent = Intent(this@MainActivity, MyService::class.java)

        sensorManager!!.registerListener(stepCount, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        serviceIntent.putExtra("steps", steps)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    private fun stopForegroundService(){
        val serviceIntent = Intent(this@MainActivity, MyService::class.java)

        sensorManager!!.unregisterListener(stepCount)
        stopService(serviceIntent)
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