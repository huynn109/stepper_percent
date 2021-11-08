package com.huynn109.stepperpercentdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huynn109.stepperslider.StepperPercent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val stepperPercent = findViewById<StepperPercent>(R.id.stepperPercent)
        stepperPercent.apply {
            setSteps(
                listOf(
                    R.drawable.ic_baseline_adb_24,
                    R.drawable.ic_baseline_adb_24,
                    R.drawable.ic_baseline_adb_24,
                    R.drawable.ic_baseline_adb_24,
                )
            )
            value(100f, 3)
        }
    }
}
