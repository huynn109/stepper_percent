package com.huynn109.stepperslider

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.Rect
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

class PopupIndicator(
    context: Context,
) {
//    private val mWindowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    var isShowing = false
        private set
    private var mPopupView: Floater?

    private val mDrawingLocation = IntArray(2)
    var screenSize = Point()

    private fun measureFloater() {
        val specWidth = MeasureSpec.makeMeasureSpec(screenSize.x, MeasureSpec.EXACTLY)
        val specHeight = MeasureSpec.makeMeasureSpec(screenSize.y, MeasureSpec.AT_MOST)
        mPopupView!!.measure(specWidth, specHeight)
    }

    fun showIndicator(parent: View, touchBounds: Rect) {
        if (isShowing) {
            return
        }
//        val windowToken = parent.windowToken
//        if (windowToken != null) {
//            val p = createPopupLayout(windowToken)
//            p.gravity = Gravity.TOP or Gravity.START
//            updateLayoutParamsForPosition(parent, p, touchBounds.top)
            isShowing = true
            translateViewIntoPosition(touchBounds.centerX())
//            invokePopup(p)
//        }
    }

    fun move(x: Int) {
        if (!isShowing) {
            return
        }
        translateViewIntoPosition(x)
    }

    fun dismiss() {
        mPopupView = null
    }

    fun dismissComplete() {
        if (isShowing) {
            isShowing = false
            try {
//                mWindowManager.removeViewImmediate(mPopupView)
            } finally {
            }
        }
    }

    private fun updateLayoutParamsForPosition(
        anchor: View,
        p: WindowManager.LayoutParams,
        yOffset: Int
    ) {
        val displayMetrics = anchor.resources.displayMetrics
        screenSize[displayMetrics.widthPixels] = displayMetrics.heightPixels
        measureFloater()
        val measuredHeight = mPopupView!!.measuredHeight
        val paddingBottom: Int = mPopupView?.mMarker?.paddingBottom ?: 0
        anchor.getLocationInWindow(mDrawingLocation)
        p.x = 0
        p.y = mDrawingLocation[1] - measuredHeight + yOffset + paddingBottom
        p.width = screenSize.x
        p.height = measuredHeight
    }

    private fun translateViewIntoPosition(x: Int) {
        mPopupView!!.setFloatOffset(x + mDrawingLocation[0] + (35/2))
    }

    private fun invokePopup(p: WindowManager.LayoutParams) {
//        mWindowManager.addView(mPopupView, p)
    }

    private fun createPopupLayout(token: IBinder): WindowManager.LayoutParams {
        val p = WindowManager.LayoutParams()
        p.gravity = Gravity.START or Gravity.TOP
        p.width = ViewGroup.LayoutParams.MATCH_PARENT
        p.height = ViewGroup.LayoutParams.MATCH_PARENT
        p.format = PixelFormat.TRANSLUCENT
        p.flags = computeFlags(p.flags)
        p.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
        p.token = token
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        return p
    }

    private fun computeFlags(curFlags: Int): Int {
        var curFlags = curFlags
        curFlags = curFlags and (WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM).inv()
        curFlags = curFlags or WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES
        curFlags = curFlags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        curFlags = curFlags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        curFlags = curFlags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        return curFlags
    }

    fun setValue(v: Int) {
        mPopupView?.setValue(v)
    }

    private inner class Floater(
        context: Context,
    ) :
        FrameLayout(context) {
        val mMarker: TextView = TextView(context)
        private var mOffset = 0
        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            measureChildren(widthMeasureSpec, heightMeasureSpec)
            val widthSize = MeasureSpec.getSize(widthMeasureSpec)
            val heightSie: Int = mMarker.measuredHeight
            setMeasuredDimension(widthSize, heightSie)
        }

        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
            val centerDiffX: Int = mMarker.measuredWidth / 2
            val offset = mOffset - centerDiffX
            mMarker.layout(
                offset,
                0,
                offset + mMarker.measuredWidth,
                mMarker.measuredHeight
            )
        }

        fun setFloatOffset(x: Int) {
            mOffset = x
            val centerDiffX: Int = mMarker.measuredWidth / 2
            val offset = x - centerDiffX
            mMarker.offsetLeftAndRight(offset - mMarker.left)
            invalidate()
        }

        @SuppressLint("SetTextI18n")
        fun setValue(v: Int) {
            mMarker.text = "$v%"
        }

        init {
            mMarker.text = "0%"
            mMarker.gravity = Gravity.CENTER
            mMarker.textSize = 10f
            mMarker.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            mMarker.background = ContextCompat.getDrawable(context, R.drawable.ic_bubble)
            val params = LayoutParams(
                27f.px(context.resources.displayMetrics),
                20f.px(context.resources.displayMetrics),
                Gravity.LEFT or Gravity.TOP
            )
            addView(
                mMarker,
                params
            )
            mMarker.setPadding(0, 0, 0, 3f.px(context.resources.displayMetrics))
        }
    }

    init {
        mPopupView = Floater(context)
    }
}

fun Float.px(m: DisplayMetrics): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, m).toInt()


