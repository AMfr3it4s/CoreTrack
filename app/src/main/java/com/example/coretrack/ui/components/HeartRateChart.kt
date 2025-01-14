package com.example.coretrack.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

/**
 * A simple line chart for real-time data.
 */
@Composable
fun HeartRateChart(
    data: List<Double>,
    modifier: Modifier = Modifier,
    currentBPM: Int? = null
) {
    val maxPoints = 50
    val displayedData = if (data.size > maxPoints) data.takeLast(maxPoints) else data
    val maxValue = (displayedData.maxOrNull() ?: 0.0).coerceAtLeast(1.0)

    Box(modifier = modifier) {
        Canvas(modifier = modifier) {
            val chartWidth = size.width
            val chartHeight = size.height
            val stepX = chartWidth / (displayedData.size - 1).coerceAtLeast(1)
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.RED
                strokeWidth = 4f
                style = android.graphics.Paint.Style.STROKE
                isAntiAlias = true
            }

            // Draw line
            for (i in 0 until displayedData.size - 1) {
                val x1 = i * stepX
                val x2 = (i + 1) * stepX

                val y1 = chartHeight - (displayedData[i] / maxValue) * chartHeight
                val y2 = chartHeight - (displayedData[i + 1] / maxValue) * chartHeight

                drawContext.canvas.nativeCanvas.drawLine(
                    x1.toFloat(),
                    y1.toFloat(),
                    x2.toFloat(),
                    y2.toFloat(),
                    paint
                )
            }
        }

        Row(
            modifier = Modifier
                .align(alignment = Alignment.BottomStart)
                .padding(16.dp)              // some padding from the edge
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Heart icon",
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(8.dp)) // gap between icon and text
            Text(
                text = "$currentBPM BPM",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

