package com.huynn109.stepperpercentdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huynn109.stepperpercent.StepperSlider
import com.huynn109.stepperpercent.StepperPercent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stepperPercent = findViewById<StepperPercent>(R.id.stepperPercent)
        stepperPercent.apply {
            setSteps(
                Pair(R.drawable.ic_home_location, 0),
                Pair(R.drawable.ic_home_info, 28),
                Pair(R.drawable.ic_upload_image, 56),
                Pair(R.drawable.ic_preview_checkout, 84),
                Pair(R.drawable.ic_done, 100)
            )
            value(35f)
            disableTouch(false)
        }
    }
}
