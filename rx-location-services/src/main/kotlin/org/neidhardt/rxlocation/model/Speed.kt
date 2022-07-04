package org.neidhardt.rxlocation.model

import android.location.Location

/**
 * Speed represents a calculated speed value. It contains information
 * on how the calculation was conduced.
 *
 * @param speedMs in m/s
 * @param sampleTimeMs is the elapsed time between the first and the last location
 * @param locations are the android locations that were used for speed calculation
 */
data class Speed(
	val speedMs: Float,
	val sampleTimeMs: Long,
	val locations: List<Location>
)