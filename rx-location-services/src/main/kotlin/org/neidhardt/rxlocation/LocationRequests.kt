package org.neidhardt.rxlocation

import com.google.android.gms.location.LocationRequest


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