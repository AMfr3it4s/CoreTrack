package com.example.coretrack.camera

import android.graphics.ImageFormat
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer
import kotlin.math.roundToInt

/**
 * Extract average red channel from ImageProxy.
 */
fun processImageProxy(imageProxy: ImageProxy): Double {
    // 1) Ensure the format is YUV_420_888
    if (imageProxy.format != ImageFormat.YUV_420_888) {
        return 0.0
    }

    val width = imageProxy.width
    val height = imageProxy.height

    // Planes: Y, U, V
    val yPlane = imageProxy.planes[0]
    val uPlane = imageProxy.planes[1]
    val vPlane = imageProxy.planes[2]

    val yBuffer = yPlane.buffer
    val uBuffer = uPlane.buffer
    val vBuffer = vPlane.buffer

    // Row strides and pixel strides
    val yRowStride = yPlane.rowStride
    val yPixelStride = yPlane.pixelStride

    val uRowStride = uPlane.rowStride
    val uPixelStride = uPlane.pixelStride

    val vRowStride = vPlane.rowStride
    val vPixelStride = vPlane.pixelStride

    // We'll accumulate sum of R values and count how many samples we take
    var sumRed = 0L
    var count = 0

    // 2) For performance, skip some pixels (e.g., step by 4)
    val rowStep = 4
    val colStep = 4

    for (row in 0 until height step rowStep) {
        for (col in 0 until width step colStep) {
            // ----- Y sample -----
            val yIndex = row * yRowStride + col * yPixelStride
            val Y = (yBuffer.getSafe(yIndex)?.toInt() ?: 128) and 0xFF

            // ----- U sample -----
            // U is subsampled by 2 in both dimensions in YUV_420
            val uRow = (row / 2)
            val uCol = (col / 2)
            val uIndex = uRow * uRowStride + uCol * uPixelStride
            val U = (uBuffer.getSafe(uIndex)?.toInt() ?: 128) and 0xFF

            // ----- V sample -----
            val vRow = (row / 2)
            val vCol = (col / 2)
            val vIndex = vRow * vRowStride + vCol * vPixelStride
            val V = (vBuffer.getSafe(vIndex)?.toInt() ?: 128) and 0xFF

            // 3) Convert YUV → R (ignoring G/B)
            //  Y ranges [16..235], U/V range [0..255], etc.
            val r = (1.164f * (Y - 16) + 1.596f * (V - 128)).roundToInt()
                .coerceIn(0, 255)

            sumRed += r
            count++
        }
    }

    // 4) Average red channel
    return if (count > 0) sumRed.toDouble() / count else 0.0
}

/**
 * Safe index fetch for ByteBuffer at position [pos].
 * Returns null if pos is out of bounds.
 */
private fun ByteBuffer.getSafe(pos: Int): Byte? {
    return if (pos in 0 until limit()) {
        this.get(pos)
    } else null
}
