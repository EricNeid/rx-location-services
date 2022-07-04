package org.neidhardt.rxlocation.utils

import android.location.Location
import io.reactivex.rxjava3.core.Flowable
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import java.util.concurrent.TimeUnit

class MapToSpeedTest {

	@Test
	fun calculateSpeed() {
		// arrange
		val locations = Flowable.just(
			Location("").apply {
				latitude = 52.42495467861161
				longitude = 13.751868388122048
				time = 0
			},
			Location("").apply {
				latitude = 52.42007093085878
				longitude = 13.754118661585549
				time = 1000
			},
		).delay(1, TimeUnit.SECONDS)
		// action
		val result = locations
			.mapToSpeed(3)
			.blockingFirst()
		// verify
		assertNotNull(result)
		assertEquals(2, result.locations.size)
		assertEquals(1000, result.sampleTimeMs)
		assertEquals(564.591, result.speedMs.toDouble(), 0.1)
	}
}