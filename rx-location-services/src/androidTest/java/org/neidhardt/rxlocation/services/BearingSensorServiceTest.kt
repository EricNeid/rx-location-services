package org.neidhardt.rxlocation.services

import android.content.Context
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*


@LargeTest
class BearingSensorServiceTest {

	private lateinit var context: Context
	private lateinit var unit: BearingSensorService

	@Before
	fun setUp() {
		this.context = InstrumentationRegistry.getInstrumentation().context
		this.unit = BearingSensorService(context)
	}

	@Test
	fun getBearingUpdatesFromMagneticAndAccelerometer() {
		// action
		val result = this.unit.getBearingUpdatesFromMagneticAndAccelerometer().blockingFirst()
		// verify
		assertNotNull(result)
		assertEquals(result, unit.lastKnownBearing)
	}

	@Test
	fun getBearingUpdatesFromRotation() {
		// action
		val result = this.unit.getBearingUpdatesFromRotation().blockingFirst()
		// verify
		assertNotNull(result)
		assertEquals(result, unit.lastKnownBearing)
	}
}