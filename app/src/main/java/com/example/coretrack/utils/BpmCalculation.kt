package com.example.coretrack.utils

/**
 * Calculate BPM from the collected sensor data over time.
 */
fun calculateBPM(data: List<Pair<Long, Double>>): Int {
    if (data.size < 3) return 0

    // Sort by timestamp, in case data is out of order
    val sortedData = data.sortedBy { it.first }
    val startTime = sortedData.first().first
    val endTime = sortedData.last().first
    val durationSec = (endTime - startTime) / 1000.0
    if (durationSec <= 0) return 0

    // Compute a threshold at 80% of max in the data
    val maxValue = sortedData.maxOfOrNull { it.second } ?: 0.0
    val threshold = maxValue * 0.8

    // Count peaks
    var beats = 0
    for (i in 1 until sortedData.size - 1) {
        val prevVal = sortedData[i - 1].second
        val currVal = sortedData[i].second
        val nextVal = sortedData[i + 1].second
        if (currVal > prevVal && currVal > nextVal && currVal > threshold) {
            beats++
        }
    }

    // BPM = (beats / durationInSeconds) * 60
    return ((beats / durationSec) * 60).toInt()
}
