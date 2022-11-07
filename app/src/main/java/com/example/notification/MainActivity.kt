package com.example.notification

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    var text: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btn)

        text = findViewById(R.id.stepCountView)

        val intent = Intent(this@MainActivity, MyService::class.java)

        startService(intent)

        text!!.text = " ${(MyService().getInt())}"

        btn.setOnClickListener {
            text!!.text = " ${(MyService().getInt())}"
        }

    }

}