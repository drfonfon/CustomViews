package com.fonfon.customviews.rume

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.support.v4.graphics.ColorUtils
import android.util.AttributeSet
import android.view.View
import com.fonfon.customviews.R

class RuneView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
  : View(context, attrs, defStyleAttr) {

  var count = 10
    set(value) {
      var trueValue = value
      if (value < MIN) trueValue = MIN
      if (value > MAX) trueValue = MAX
      field = trueValue
      strokeWidth = (MAX / field).toFloat()
      linesPaint.strokeWidth = strokeWidth
      pointsPaint.strokeWidth = strokeWidth
      points = null
      invalidate()
    }

  private var points: Array<PointF>? = null
  private var cx = 0f
  private var cy = 0f
  private var r = 0f

  private val circlePaint = Paint().also {
    it.isAntiAlias = true
    it.color = circleColor
    it.style = Paint.Style.FILL
  }

  var circleColor = Color.TRANSPARENT
    set(value) {
      field = value
      circlePaint.color = value
      invalidate()
    }

  private val pointsPaint = Paint().also {
    it.isAntiAlias = true
    it.color = pointsColor
    it.style = Paint.Style.FILL
  }

  var pointsColor = Color.BLACK
    set(value) {
      field = value
      pointsPaint.color = value
      invalidate()
    }

  private val linesPaint = Paint().also {
    it.isAntiAlias = true
    it.color = Color.BLACK
    it.style = Paint.Style.STROKE
    it.strokeWidth = strokeWidth
    it.strokeCap = Paint.Cap.ROUND
    it.strokeJoin = Paint.Join.ROUND
  }

  private var strokeWidth = 1f
    set(value) {
      field = value
      linesPaint.strokeWidth = value
      invalidate()
    }
  var startColor = Color.RED
    set(value) {
      field = value
      invalidate()
    }
  var endColor = Color.GREEN
    set(value) {
      field = value
      invalidate()
    }

  private val path = Path()
  var style = Style.ARK
    set(value) {
      field = value
      invalidate()
    }

  var func: Function = Function()
    set(value) {
      field = value
      invalidate()
    }

  enum class Style {
    LINEAR, ARK
  }

  open class Function {
    internal open fun function(value: Int): Int {
      return 1
    }
  }

  init {
    val a = context.theme.obtainStyledAttributes(attrs, R.styleable.RuneView, 0, 0)
    try {
      count = a.getInteger(R.styleable.RuneView_rune_count, count)
      if (count < MIN) count = MIN
      if (count > MAX) count = MAX
      circleColor = a.getColor(R.styleable.RuneView_rune_circle_color, circleColor)
      pointsColor = a.getColor(R.styleable.RuneView_rune_points_color, pointsColor)
      startColor = a.getColor(R.styleable.RuneView_rune_start_color, startColor)
      endColor = a.getColor(R.styleable.RuneView_rune_end_color, endColor)
      style = Style.values()[a.getInteger(R.styleable.RuneView_rune_style, 0)]
    } finally {
      a.recycle()
    }

    strokeWidth = (MAX / count).toFloat()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val min = Math.min(widthMeasureSpec, heightMeasureSpec)
    super.onMeasure(min, min)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    if (points == null) initPoints(canvas)

    canvas.rotate(-90f, cx, cy)
    canvas.drawCircle(cx, cy, r, circlePaint)

    points?.let {
      for (i in 0 until count) {
        val res = function(i)
        linesPaint.color = ColorUtils.blendARGB(startColor, endColor, 1 / count.toFloat() * i)
        if (style == Style.LINEAR) {
          canvas.drawLine(cx + it[i].x, cy + it[i].y, cx + it[res].x, cy + it[res].y, linesPaint)
        } else {
          path.reset()
          path.moveTo(cx + it[i].x, cy + it[i].y)
          path.quadTo(cx, cy, cx + it[res].x, cy + it[res].y)
          canvas.drawPath(path, linesPaint)
        }
      }

      for (i in 0 until count) {
        canvas.drawCircle(cx + it[i].x, cy + it[i].y, strokeWidth, pointsPaint)
      }
    }
  }

  private fun initPoints(canvas: Canvas) {
    cx = (canvas.width / 2).toFloat()
    cy = (canvas.height / 2).toFloat()
    r = (canvas.width / 2 - strokeWidth * 2)

    points = Array(count) { i ->
      PointF(
          Math.round(r * Math.cos(Math.toRadians((MAX / count.toFloat() * i.toFloat()).toDouble()))).toFloat(),
          Math.round(r * Math.sin(Math.toRadians((MAX / count.toFloat() * i.toFloat()).toDouble()))).toFloat()
      )
    }
  }

  private fun function(value: Int): Int {
    var result = func.function(value)
    if (result < 0) result = 0
    return if (result > count - 1) result % count else result
  }

  companion object {
    const val MIN = 3
    const val MAX = 360
  }
}
