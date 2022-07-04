/*
 * SPDX-FileCopyrightText: 2021 Eric Neidhardt
 * SPDX-License-Identifier: MIT
 */
package org.neidhardt.rxlocation.services

import android.content.Context
import android.location.LocationManager
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
class AndroidLocationServiceTest {

	@Rule
	@JvmField
	val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
			android.Manifest.permission.ACCESS_FINE_LOCATION,
			android.Manifest.permission.ACCESS_COARSE_LOCATION,
			android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
	)

	private lateinit var context: Context
	private lateinit var unit: AndroidLocationService

	@Before
	fun setUp() {
		context = getInstrumentation().targetContext
		unit = AndroidLocationService(context)
	}

	@Test
	fun getLocationUpdates() {
		// action
		val result = unit.getLocationUpdates(
			1000,
			1f,
			LocationManager.GPS_PROVIDER
		).blockingFirst()
		// verify
		assertNotNull(result)
		assertNotNull(result.latitude)
		assertNotNull(result.longitude)
		assertEquals(result, unit.lastKnowLocation)
	}

}
