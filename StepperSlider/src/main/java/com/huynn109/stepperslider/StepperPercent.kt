package com.huynn109.stepperslider

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
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.slider.Slider
import kotlin.math.roundToInt


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
    private var tvBubble: TextView? = null

    // Color of indicator and line
    private var defaultColor: Int = 0
    private var activeColor: Int = 0
    private var inActiveColor: Int = 0

    // Size of indicator
    private var stepSize: Int = 0

    private var isDisableDrag: Boolean = true

    private var mBubbleIndicator: PopupIndicator? = null

    val result: MutableList<Pair<Int, Int>> = mutableListOf()

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
        rootView = findViewById(R.id.root_view)
        linearProgressIndicator = findViewById(R.id.progress_indicator)
        linearMainLayout = findViewById(R.id.linear_main)
        tvBubble = findViewById(R.id.tvBubble)
        setColor()
        disableTouch(true)
    }

    @SuppressLint("SetTextI18n")
    fun value(v: Float, stage: Int = 1) {
        val processTmp = getStage(v, stage)
        Handler(Looper.getMainLooper()).postDelayed({
            linearProgressIndicator.progress = processTmp.toInt()
            linearProgressIndicator.resetColorWithNewValue(result)
            tvBubble?.text = "${v.roundToInt()}%"
            if (v > 90f || v < 10f) tvBubble?.visibility = View.GONE
            else tvBubble?.visibility = View.VISIBLE
            tvBubble?.x =
                linearProgressIndicator.thumb.bounds.centerX().toFloat() - ((tvBubble?.width
                    ?: 0f).toFloat() / 3f)
        }, 300)
    }

    private fun getStage(v: Float, stage: Int): Float {
        val percentStage = 100 / (result.size - 1)
        val tmp = when {
            v <= 15f -> 2f
            v >= 90f -> -1f
            else -> 0f
        }
        return ((v * percentStage) / 100f) + (percentStage * (stage - 1)) + tmp
    }

    @SuppressLint("ClickableViewAccessibility")
    fun disableTouch(isDisable: Boolean) {
        linearProgressIndicator.setOnTouchListener { _, _ ->
            isDisable
        }
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

    private fun setColor() {
        linearProgressIndicator.setActiveColor(activeColor)
        linearProgressIndicator.setInActiveColor(inActiveColor)
    }

    private fun setStepList(drawableResources: List<Pair<Int, Int>>) {
        linearProgressIndicator.setDotsDrawables(drawableResources)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setSteps(drawableResources: List<Int>) {
        val stagePercent = 100 / (drawableResources.size - 1)
        drawableResources.forEachIndexed { index, t ->
            result.add(
                Pair(
                    t,
                    if (index == drawableResources.size - 1) 100 else stagePercent * index
                )
            )
        }
        setStepList(result)
    }
}

@SuppressLint("ClickableViewAccessibility")
fun Slider.showLabelUntilNextTouchUp() {
    val originalValue = value
    onTouchEvent(
        MotionEvent.obtain(0L, 0L, MotionEvent.ACTION_DOWN, 0f, 0f, 0)
    )
    value = originalValue
}
