package org.neidhardt.rxlocation.services

import android.content.Context
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test


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
		this.context = InstrumentationRegistry.getInstrumentation().context
		this.unit = GoogleLocationService(context)
	}

	@Test
	fun precondition_googlePlayServices() {
		// action
		val googleApiAvailability = GoogleApiAvailability.getInstance()
		val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this.context)
		// verify
		assertEquals(ConnectionResult.SUCCESS, resultCode)
	}

	@Test
	fun getLastKnowLocationFromDevice() {
		// action
		val result = this.unit.getLastKnowLocationFromDevice().blockingGet()
		// verify
		assertNotNull(result)
		assertNotNull(result.latitude)
		assertNotNull(result.longitude)
		assertEquals(result, this.unit.lastKnowLocation)
	}

}
