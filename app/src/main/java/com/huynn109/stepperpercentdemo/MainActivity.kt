package com.huynn109.stepperpercentdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huynn109.stepperslider.StepperSlider
import com.huynn109.stepperslider.StepperPercent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stepperPercent = findViewById<StepperPercent>(R.id.stepperPercent)
        stepperPercent.apply {
            setSteps(
                Pair(R.drawable.ic_baseline_adb_24, 0),
                Pair(R.drawable.ic_baseline_adb_24, 28),
                Pair(R.drawable.ic_baseline_adb_24, 56),
                Pair(R.drawable.ic_baseline_adb_24, 84),
                Pair(R.drawable.ic_baseline_adb_24, 100)
            )
            value(35f)
            disableTouch(false)
        }
    }
}
