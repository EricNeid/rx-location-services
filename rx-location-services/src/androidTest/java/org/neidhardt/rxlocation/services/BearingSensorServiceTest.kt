package org.neidhardt.rxlocation.services

import android.content.Context
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by neid_ei (eric.neidhardt@dlr.de)
 * on 06.05.2019.
 */
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
	}
}