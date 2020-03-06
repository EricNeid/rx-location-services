package org.neidhardt.rxlocation.utils

import kotlin.math.abs

/**
 * calcAverageAngle calculates the average angle from a list of values.
 * Determine the average angle is not simple due to values "wrap around" at 0°.
 *
 * @param values list of angles to calculate the average from
 * @return average angle
 */
fun calcAverageAngle(values: List<Float>): Float {
	if (values.isEmpty()) {
		throw UnsupportedOperationException("Cannot calculate average of empty list")
	}

	var avgHeading: Float
	var sum1 = 0f

	for (i in 1..values.lastIndex) {
		var angle: Float

		// avgHeading
		val diff1 = values.first() - values[i]
		val diff2 = abs(360f - abs(diff1))
		if (abs(diff1) < diff2) {
			angle = -1 * diff1
		} else {
			angle = diff2
			if (diff1 < 0) {
				angle *= -1
			}
		}
		sum1 += angle
	}

	avgHeading = values.first() + sum1 / values.size
	if (avgHeading >= 360) {
		avgHeading -= 360
	}

	return avgHeading
}
