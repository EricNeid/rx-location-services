package org.neidhardt.rxlocation.services

import com.google.android.gms.location.LocationRequest

/**
 * Created by neid_ei (eric.neidhardt@dlr.de)
 * on 02.05.2019.
 */
object LocationRequests {

	fun precise(updateRateMillis: Long): LocationRequest {
		return LocationRequest().apply {
			this.interval = updateRateMillis
			this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
		}
	}

	fun balanced(updateRateMillis: Long): LocationRequest {
		return LocationRequest().apply {
			this.interval = updateRateMillis
			this.priority = LocationRequest.PRIORITY_LOW_POWER
		}
	}

	fun cheap(updateRateMillis: Long): LocationRequest {
		return LocationRequest().apply {
			this.interval = updateRateMillis
			this.priority = LocationRequest.PRIORITY_LOW_POWER
		}
	}
}