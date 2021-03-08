package dev.immathan.charter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View

class ChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#AA888888")
    }

    private val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#AAec4646")
        strokeWidth = 5.px.toFloat()
    }

    private val curvePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#AA32e0c4")
        style = Paint.Style.STROKE
        strokeWidth = 3.px.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        drawChart(canvas)
        drawValues(canvas)
    }

    private fun drawValues(canvas: Canvas) {
        val points = mutableListOf<DataPoint>()
        getDummyValue().forEachIndexed { index, value ->
            val diffWidth = width.toFloat() / 10
            val diffHeight = height.toFloat() / 10
            points.add(DataPoint(diffWidth * index, height.toFloat() - diffHeight * value))
        }
        val path = Path()
        points.forEachIndexed { index, dataPoint ->
            if (index == 0) {
                path.moveTo(dataPoint.x, dataPoint.y)
                return@forEachIndexed
            }
            val controlPoint1 = PointF((points[index].x + points[index - 1].x) / 2, points[index - 1].y)
            val controlPoint2 = PointF((points[index].x + points[index - 1].x) / 2, points[index].y)
            path.cubicTo(controlPoint1.x,controlPoint1.y, controlPoint2.x, controlPoint2.y, dataPoint.x, dataPoint.y)
        }
        canvas.drawPath(path, curvePaint)
        points.forEach {
            canvas.drawPoint(it.x, it.y, axisPaint)
        }
    }

    private fun drawChart(canvas: Canvas) {
        drawXAxis(canvas)
        drawYAxis(canvas)
    }

    private fun drawXAxis(canvas: Canvas) {
        val totalLines = 10
        val diffHeight = height.toFloat() / totalLines.toFloat()

        // draw axis and value lines
        for (i in 0..totalLines) {
            canvas.drawLine(
                0f,
                diffHeight * i,
                width.toFloat(),
                diffHeight * i,
                linePaint
            )
        }
    }

    private fun drawYAxis(canvas: Canvas) {
        val totalLines = 10
        val diffWidth = width.toFloat() / totalLines.toFloat()

        // draw axis and value lines
        for (i in 0..totalLines) {
            canvas.drawLine(
                diffWidth * i,
                0f,
                diffWidth * i,
                height.toFloat(),
                linePaint
            )
        }
    }
}

fun getDummyValue(): MutableList<Int> {
    return mutableListOf<Int>().apply {
        for (i in 0..10) {
            add((1..10).random())
        }
    }
}

data class DataPoint(val x: Float, val y: Float)