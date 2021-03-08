package dev.immathan.charter

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.animation.doOnEnd

class ChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var drawingPath: Path = Path()
    private val pointValues = mutableListOf<DataPoint>()

    private val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#AA888888")
    }

    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#AAec4646")
        strokeWidth = 5.px.toFloat()
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#AA32e0c4")
        style = Paint.Style.STROKE
        strokeWidth = 3.px.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        refresh()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        drawChart(canvas)
        canvas.drawPath(drawingPath, linePaint)
        pointValues.forEach {
            canvas.drawCircle(it.x, it.y, 5.px.toFloat(), pointPaint)
        }
    }

    fun refresh() {
        val values = getDummyValue()
        val dataPoints = getDataPoints(values)
        drawingPath = Path()
        pointValues.clear()
        val linePathMeasure = PathMeasure(prepareLinePath(dataPoints), false)
        setupPathDrawing(linePathMeasure) {
            pointValues.addAll(dataPoints)
        }
    }

    private fun getDataPoints(values: List<Int>): List<DataPoint> {
        val points = mutableListOf<DataPoint>()
        values.forEachIndexed { index, value ->
            val diffWidth = width.toFloat() / 10
            val diffHeight = height.toFloat() / 10
            points.add(DataPoint(diffWidth * index, height.toFloat() - diffHeight * value))
        }
        return points
    }

    private fun prepareLinePath(points: List<DataPoint>): Path {
        val path = Path()
        points.forEachIndexed { index, dataPoint ->
            if (index == 0) {
                path.moveTo(dataPoint.x, dataPoint.y)
                return@forEachIndexed
            }
            val controlPoint1 = PointF((points[index].x + points[index - 1].x) / 2, points[index - 1].y)
            val controlPoint2 = PointF((points[index].x + points[index - 1].x) / 2, points[index].y)
            path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, dataPoint.x, dataPoint.y)
        }
        return path
    }

    private fun setupPathDrawing(pathMeasure: PathMeasure, onEnd: () -> Unit) {
        val pathAnimator = ValueAnimator.ofFloat(0f, pathMeasure.length)
        pathAnimator.duration = 1500

        pathAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            pathMeasure.getSegment(0f, value, drawingPath, true)
            invalidate()
        }
        pathAnimator.doOnEnd {
            onEnd()
        }
        pathAnimator.start()
    }

    private fun drawChart(canvas: Canvas) {
        drawXAxis(canvas)
        drawYAxis(canvas)
    }

    private fun drawXAxis(canvas: Canvas) {
        val totalLines = 10
        val diffHeight = height.toFloat() / totalLines.toFloat()

        // draw axis line
        for (i in 0..totalLines) {
            canvas.drawLine(
                0f,
                diffHeight * i,
                width.toFloat(),
                diffHeight * i,
                axisPaint
            )
        }
    }

    private fun drawYAxis(canvas: Canvas) {
        val totalLines = 10
        val diffWidth = width.toFloat() / totalLines.toFloat()

        // draw axis line
        for (i in 0..totalLines) {
            canvas.drawLine(
                diffWidth * i,
                0f,
                diffWidth * i,
                height.toFloat(),
                axisPaint
            )
        }
    }
}

fun getDummyValue(): List<Int> {
    return mutableListOf<Int>().apply {
        for (i in 0..10) {
            add((1..10).random())
        }
    }
}

data class DataPoint(val x: Float, val y: Float)