package org.neidhardt.rxlocation.model

/**
 * Bearing represents a calculated user bearing.
 *
 * @param azimuth in degress: [0, 360]
 * @param accuracy see SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM and other constants for details.
 */
data class Bearing(val azimuth: Float, val accuracy: Int)
