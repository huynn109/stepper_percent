package com.huynn109.stepperslider

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import kotlin.math.max

class StepperSlider : androidx.appcompat.widget.AppCompatSeekBar {
    companion object {
        private const val DEFAULT_ICON_SIZE = 40
        private const val DEFAULT_ICON_RADIUS = 35f
        private const val DEFAULT_ICON_BORDER_WIDTH = 6f
    }

    private var dotsDrawablesTmp: MutableList<Pair<Int, Int>> = mutableListOf()

    private lateinit var paintCircleActive: Paint

    private lateinit var paintCircleInActive: Paint

    private var dotsPositions: MutableList<Int> = mutableListOf()

    private var dotBitmap: Bitmap? = null

    private var dotBitmaps: MutableList<Bitmap?> = mutableListOf()

    private var dotDrawable: Drawable? = null

    private var dotsResources: MutableList<Int> = mutableListOf()

    private var dotActives: MutableList<Boolean> = mutableListOf()

    private var activeColor: Int = Color.parseColor("#FCB931")

    private var inActiveColor: Int = Color.parseColor("#CCCCCC")

    private var backgroundCircle: Int = Color.WHITE

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    fun setActiveColor(activeColor: Int) {
        this.activeColor = activeColor
        this.progressTintList =
            colorStateListOf(intArrayOf(-android.R.attr.state_active) to activeColor)
        this.thumbTintList =
            colorStateListOf(intArrayOf(-android.R.attr.state_active) to activeColor)
    }

    fun setInActiveColor(inActiveColor: Int) {
        this.inActiveColor = inActiveColor
        this.progressBackgroundTintList =
            colorStateListOf(intArrayOf(-android.R.attr.state_active) to inActiveColor)
    }

    private fun colorStateListOf(vararg mapping: Pair<IntArray, Int>): ColorStateList {
        val (states, colors) = mapping.unzip()
        return ColorStateList(states.toTypedArray(), colors.toIntArray())
    }

    @SuppressLint("Recycle")
    private fun init() {
        paintCircleActive = Paint().apply {
            isAntiAlias = true
            color = activeColor
            strokeWidth = 4f
            style = Paint.Style.STROKE
        }
        paintCircleInActive = Paint().apply {
            isAntiAlias = true
            color = inActiveColor
            strokeWidth = 4f
            style = Paint.Style.STROKE
        }
        setPadding(35 + 5, 0, 35 + 5, 0)
        thumb.mutate().alpha = 0
        splitTrack =false
    }

    fun setDots(dots: MutableList<Int>) {
        dotsPositions = dots
        invalidate()
    }

    fun setDotsDrawable(dotsResource: Int) {
        dotDrawable = ContextCompat.getDrawable(context, dotsResource)
        dotBitmap = getBitmapFromVector(context, dotsResource, inActiveColor)
        invalidate()
    }

    fun setDotsDrawables(dotsDrawables: List<Pair<Int, Int>>) {
        this.dotsDrawablesTmp.clear()
        this.dotActives.clear()
        this.dotsResources.clear()
        this.dotsPositions.clear()
        this.dotBitmaps.clear()
        dotsDrawables.forEachIndexed { index, pair ->
            this.dotsDrawablesTmp.add(pair)
            this.dotActives.add(pair.second <= (progress + 5))
            this.dotsResources.add(pair.first)
            this.dotsPositions.add(pair.second)
            this.dotBitmaps.add(
                getBitmapFromVector(
                    context,
                    pair.first,
                    if (dotActives[index]) activeColor else inActiveColor
                )
            )
        }
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = measuredWidth - paddingLeft - paddingRight
        val step = width / 100.toFloat()
        for (i in 0 until dotsPositions.size) {
            val positionOfPoint = dotsPositions[i]
            val paintActiveOrInActive =
                if (dotActives[i]) paintCircleActive else paintCircleInActive
            val borderColorActiveOrInActive = if (dotActives[i]) activeColor else inActiveColor
            drawCircle(canvas, step, positionOfPoint, paintActiveOrInActive)
            fillCircleStrokeBorder(
                canvas,
                step * positionOfPoint + (paddingLeft),
                0f + (max(this.height, height) / 2),
                DEFAULT_ICON_RADIUS,
                backgroundCircle,
                DEFAULT_ICON_BORDER_WIDTH,
                borderColorActiveOrInActive,
                paintActiveOrInActive
            )

            val iconBitmap = dotBitmaps[i]
            drawIcon(canvas, iconBitmap, step, positionOfPoint)
        }
    }

    private fun drawIcon(
        canvas: Canvas,
        iconBitmap: Bitmap?,
        step: Float,
        position: Int
    ) {
        iconBitmap?.let {
            canvas.drawBitmap(
                it,
                step * position + (paddingLeft) - (iconBitmap.width / 2.toFloat()),
                0f + ((height - iconBitmap.height) / 2),
                null
            )
        }
    }

    private fun drawCircle(canvas: Canvas, step: Float, position: Int, paint: Paint) {
        canvas.drawCircle(
            step * position + (paddingLeft),
            0f + (max(this.height, height) / 2),
            DEFAULT_ICON_RADIUS,
            paint
        )
    }

    private fun fillCircleStrokeBorder(
        c: Canvas, cx: Float, cy: Float, radius: Float,
        circleColor: Int, borderWidth: Float, borderColor: Int, p: Paint,
    ) {
        p.color = circleColor
        val saveStyle = p.style
        p.style = Paint.Style.FILL
        c.drawCircle(cx, cy, radius, p)
        if (borderWidth > 0) {
            p.color = borderColor
            p.style = Paint.Style.STROKE
            val saveStrokeWidth = p.strokeWidth
            p.strokeWidth = borderWidth
            c.drawCircle(cx, cy, radius - borderWidth / 2, p)
            p.strokeWidth = saveStrokeWidth
        }
        p.color = borderColor
        p.style = saveStyle
    }

    private fun getBitmapFromVector(context: Context, drawableId: Int, color: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val width: Float = (drawable?.intrinsicWidth ?: DEFAULT_ICON_SIZE).toFloat()
        val height: Float = (drawable?.intrinsicHeight ?: DEFAULT_ICON_SIZE).toFloat()
        val widthScale = DEFAULT_ICON_SIZE / width
        val heightScale = DEFAULT_ICON_SIZE / height
        val scale = widthScale.coerceAtLeast(heightScale)
        val newWidth = (scale * width).coerceAtLeast(1f)
        val newHeight = (scale * height).coerceAtLeast(1f)
        val bitmap: Bitmap = if (newWidth < newHeight) {
            Bitmap.createBitmap(newHeight.toInt(), newHeight.toInt(), Bitmap.Config.ARGB_8888)
        } else {
            Bitmap.createBitmap(newWidth.toInt(), newWidth.toInt(), Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(bitmap)
        drawable?.apply {
            setBounds(0, 0 - 2, newWidth.toInt(), newHeight.toInt() - 2)
            setTint(color)
            draw(canvas)
        }

        return bitmap
    }

    fun resetColorWithNewValue(result: MutableList<Pair<Int, Int>>) {
        setDotsDrawables(result)
    }
}
