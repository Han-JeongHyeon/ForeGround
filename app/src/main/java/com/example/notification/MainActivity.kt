package com.example.notification

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    var i = 0
    var text: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text = findViewById(R.id.stepCountView)

        val intent = Intent(this@MainActivity, MyService::class.java)
        startService(intent)

//        val btn = findViewById<Button>(R.id.resetButton)
//
//        btn.setOnClickListener {
//            Log.d("TAG", "$i")
//        }

    }

//    private val sensorManager by lazy {
//        getSystemService(Context.SENSOR_SERVICE) as SensorManager
//    }
//
//    override fun onResume() {
//        super.onResume()
//        /* ### registerListener로 사용할 센서 등록 ###
//         첫 번째 인자 : 센서값을 받을 SensorEventListener
//         두 번째 인자 : 센서 종류 지정 getDefaultSensor
//         세 번째 인자 : 센서 값을 얼마나 자주 받을지 지정
//           - SENSOR_DELAY_FASTAST : 가능한 가장 자주 센서값을 얻습니다.
//           - SENSOR_DELAY_GAME : 게임에 적합한 정도로 센서값을 얻습니다.
//           - SENSOR_DELAY_NORMAL : 화면방향이 전환될 때 적합한 정도로 센서값을 얻습니다.
//           - SENSOR_DELAY_UI : 사용자 인터페이스를 표시하기에 적합한 정도로 센서값을 얻습니다.
//        */
//        sensorManager.registerListener(
//            this,
//            sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
//            SensorManager.SENSOR_DELAY_FASTEST
//        )
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onSensorChanged(event: SensorEvent?) {
//        if(event!!.sensor.type == Sensor.TYPE_STEP_DETECTOR){
//            text!!.text = (text!!.text.toString().toInt() + 1).toString()
//            Log.d("TAG", "onSensorChanged: ")
//        }
//    }
//
//    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
//    }

}