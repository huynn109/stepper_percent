package com.huynn109.stepperpercent

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.slider.Slider
import timber.log.Timber


class StepperPercent : RelativeLayout {

    companion object {
        const val TAG = "StepperPercent"
        const val START_STEP = 0
    }

    // Root view
    private var rootView: ConstraintLayout? = null

    // Progress bar view
    private lateinit var linearProgressIndicator: StepperSlider

    // Linear layout main view
    private var linearMainLayout: LinearLayout? = null

    // Color of indicator and line
    private var defaultColor: Int = 0
    private var activeColor: Int = 0
    private var inActiveColor: Int = 0

    // Size of indicator
    private var stepSize: Int = 0

    private var isDisableDrag: Boolean = true

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        handleAttributes(attrs)
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        handleAttributes(attrs)
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.main_layout, this)
        Timber.plant(Timber.DebugTree())
        rootView = findViewById(R.id.root_view)
        linearProgressIndicator = findViewById(R.id.progress_indicator)
        linearMainLayout = findViewById(R.id.linear_main)
    }

    fun value(v: Float) {
        linearProgressIndicator.value = v
        Handler(Looper.getMainLooper()).postDelayed({
            linearProgressIndicator.showLabelUntilNextTouchUp()
            linearProgressIndicator.resetColorWithNewValue()
        }, 300)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun disableTouch(isEnable: Boolean) {
        linearProgressIndicator.setOnTouchListener { _, _ -> isEnable }
    }

    private fun handleAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepperPercent, 0, 0)

        try {
            stepSize = typedArray.getDimensionPixelSize(R.styleable.StepperPercent_step_size, 80)
            defaultColor = typedArray.getColor(
                R.styleable.StepperPercent_default_color,
                Color.parseColor("#cccccc")
            )
            activeColor = typedArray.getColor(
                R.styleable.StepperPercent_active_color,
                Color.parseColor("#cccccc")
            )
            inActiveColor = typedArray.getColor(
                R.styleable.StepperPercent_inactive_color,
                Color.parseColor("#cccccc")
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        typedArray.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setSteps(vararg drawableResources: Pair<Int, Int>) {
        linearProgressIndicator.setDotsDrawables(drawableResources)
    }
}

@SuppressLint("ClickableViewAccessibility")
fun Slider.showLabelUntilNextTouchUp() {
    // sent a touch event to cause the label for the slider to show.
    // a side effect of calling onTouchEvent is that it will cause
    // the slider to change the progress value; we save the original
    // progress value before calling onTouchEvent, then restore the
    // value after calling onTouchEvent.
    val originalValue = value
    onTouchEvent(
        MotionEvent.obtain(0L, 0L, MotionEvent.ACTION_DOWN, 0f, 0f, 0)
    )
    value = originalValue
}
