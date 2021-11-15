/*
 * SPDX-FileCopyrightText: 2021 Eric Neidhardt
 * SPDX-License-Identifier: MIT
 */
package org.neidhardt.rxlocation.services

import android.content.Context
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test


@LargeTest
class BearingSensorServiceTest {

	private lateinit var context: Context
	private lateinit var unit: BearingSensorService

	@Before
	fun setUp() {
		context = getInstrumentation().targetContext
		unit = BearingSensorService(context)
	}

	@Test
	fun getBearingUpdatesFromMagneticAndAccelerometer() {
		// action
		val result = unit.getBearingUpdatesFromMagneticAndAccelerometer().blockingFirst()
		// verify
		assertNotNull(result)
		assertEquals(result.azimuth, unit.lastKnownBearing)
	}

	@Test
	fun getBearingUpdatesFromRotation() {
		// action
		val result = unit.getBearingUpdatesFromRotation().blockingFirst()
		// verify
		assertNotNull(result)
		assertEquals(result.azimuth, unit.lastKnownBearing)
	}
}