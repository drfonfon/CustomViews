package com.fonfon.customviews.hexagonProgress

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import com.fonfon.customviews.R
import com.fonfon.customviews.R.string.count
import com.fonfon.customviews.rume.RuneView

class HexagonProgress @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  private var paint = Paint().apply {
    color = color
    isAntiAlias = true
    style = Paint.Style.STROKE
    strokeWidth = strokeWidth
  }

  var color = Color.YELLOW
    set(value) {
      field = value
      paint.color = value
      invalidate()
    }

  var strokeWidth = 4f
    set(value) {
      field = value
      paint.strokeWidth = value
      invalidate()
    }

  private val polygon = Path()
  private val center = PointF()
  private var radius = 0f
    set(value) {
      field = value
      size = radius
    }

  private var size = radius
    set(value) {
      field = value
      invalidate()
    }
  private var curEdge = 0
  private var anim: ValueAnimator = getAnimator()
  private var isOut = false

  init {
    val a = context.theme.obtainStyledAttributes(attrs, R.styleable.HexagonProgress, 0, 0)
    try {
      val color = a.getColor(R.styleable.HexagonProgress_hp_color, Color.YELLOW)
      this.color = color
      paint.color = color

      val strokeWidth = a.getColor(R.styleable.HexagonProgress_hp_stroke_width, 4)
      this.strokeWidth = strokeWidth.toFloat()
      paint.strokeWidth = strokeWidth.toFloat()
    } finally {
      a.recycle()
    }
  }


  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val min = Math.min(widthMeasureSpec, heightMeasureSpec)
    super.onMeasure(min, min)
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    val w = right - left
    val h = bottom - top
    center.set((w / 2).toFloat(), (h / 2).toFloat())
    radius = Math.min(w, h) / 2 - paint.strokeWidth
    if (anim.isStarted) {
      stop()
      start()
    }
  }

  fun start() {
    size = radius
    curEdge = 0
    anim = getAnimator()
    anim.start()
  }

  fun stop() {
    anim.cancel()
  }

  private fun getAnimator() = ValueAnimator.ofFloat(radius, -radius).also {
    it.duration = 300
    it.addUpdateListener { size = it.animatedValue as Float }
    it.repeatCount = ValueAnimator.INFINITE
    it.repeatMode = ValueAnimator.REVERSE
    it.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationRepeat(animation: Animator) {
        if (isOut) {
          curEdge = getNextIndex(curEdge, 1)
          isOut = false
        } else {
          isOut = true
        }
      }
    })
  }

  override fun onDraw(canvas: Canvas) = canvas.drawPath(createHexagon(), paint)

  private fun createHexagon() = polygon.also {
    it.reset()
    hexCorner(0).apply { it.moveTo(x, y) }
    for (i in 1 until SIDES) hexCorner(i).apply { it.lineTo(x, y) }
    it.close()
  }

  private fun hexCorner(i: Int) = PointF().also {
    var index = i
    var s = radius
    if (index == curEdge || index == getNextIndex(curEdge, 1)) {
      if (size < 0) index = getNextIndex(i, if (i == curEdge) 4 else 2)
      s = Math.abs(size)
    }
    val angleDeg = 60 * index - 30
    val angleRad = Math.PI / 180 * angleDeg
    it.x = (center.x + s * Math.cos(angleRad)).toFloat()
    it.y = (center.y + s * Math.sin(angleRad)).toFloat()
  }

  private fun getNextIndex(index: Int, addition: Int): Int {
    var result = index + addition
    if (result < SIDES) return result
    do {
      result -= SIDES
    } while (result > SIDES)
    return result
  }

  companion object {
    const val SIDES = 6
  }
}
