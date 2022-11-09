package com.example.notification

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class StepCount(private val stepCounter: StepCounter): SensorEventListener {

    override fun onSensorChanged(p0: SensorEvent?) {
        stepCounter.stepCount()
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) { }

    interface StepCounter{
        fun stepCount()
    }

}