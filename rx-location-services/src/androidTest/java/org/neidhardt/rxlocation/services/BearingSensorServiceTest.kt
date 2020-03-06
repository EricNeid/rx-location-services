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
	fun getBearingUpdates() {
		// action
		val result = this.unit.getBearingUpdates().blockingFirst()
		// verify
		assertNotNull(result)
		assertEquals(result, unit.lastKnownBearing)
	}
}