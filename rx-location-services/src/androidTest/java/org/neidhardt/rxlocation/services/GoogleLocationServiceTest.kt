/**
 * SPDX-FileCopyrightText: 2021 Eric Neidhardt
 * SPDX-License-Identifier: MIT
 */
package org.neidhardt.rxlocation.services

import android.content.Context
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neidhardt.rxlocation.LocationRequests


@LargeTest
class GoogleLocationServiceTest {

	@Rule
	@JvmField
	val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
			android.Manifest.permission.ACCESS_FINE_LOCATION,
			android.Manifest.permission.ACCESS_COARSE_LOCATION,
			android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
	)

	private lateinit var context: Context
	private lateinit var unit: GoogleLocationService

	@Before
	fun setUp() {
		context = getInstrumentation().targetContext
		unit = GoogleLocationService(context)
	}

	@Test
	fun precondition_googlePlayServices() {
		// action
		val googleApiAvailability = GoogleApiAvailability.getInstance()
		val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
		// verify
		assertEquals(ConnectionResult.SUCCESS, resultCode)
	}

	@Test
	fun getLocationUpdates() {
			// action
			val result = unit.getLocationUpdates(LocationRequests.balanced(1000))
					.observeOn(AndroidSchedulers.mainThread())
					.blockingFirst()
			// verify
			assertNotNull(result)
			assertNotNull(result.latitude)
			assertNotNull(result.longitude)
			assertEquals(result, unit.lastKnowLocation)
	}

}
