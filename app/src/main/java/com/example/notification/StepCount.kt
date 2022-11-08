package com.example.notification

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService
import java.util.ArrayList

class StepCount(private val stepDetector: StepDetector): SensorEventListener {

    private val axisValues = ArrayList<Int>()

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
//        if(event!!.sensor.type == Sensor.TYPE_STEP_COUNTER &&
//            MyService.sensor != "측정 불가"){

            stepDetector.onStepDetected()
//            MyService.step++

//        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    interface StepDetector {
        fun onStepDetected()
    }

}