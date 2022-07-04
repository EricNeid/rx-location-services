package org.neidhardt.rxlocation.utils

import android.location.Location
import io.reactivex.rxjava3.core.Flowable
import org.neidhardt.rxlocation.model.Speed
import java.util.concurrent.TimeUnit


/**
 * calculateSpeed map input stream of locations to stream of speed.
 * Locations are collected for a given duration and the speed is
 * calculated from these locations and the elapsed time.
 *
 * @param sampleTimeSec buffer locations for this time frame
 */
fun Flowable<Location>.mapToSpeed(sampleTimeSec: Long): Flowable<Speed> {
	return this.buffer(sampleTimeSec, TimeUnit.SECONDS)
		.map { locations ->
			if (locations.size < 2) {
				Speed(0f, sampleTimeSec * 1000, emptyList())
			} else {
				val d = locations.first().distanceTo(locations.last())
				val tMs = locations.last().time - locations.first().time
				val v = d / (tMs / 1000)
				Speed(v, tMs, locations)
			}
		}
}