/*
 * SPDX-FileCopyrightText: 2021 Eric Neidhardt
 * SPDX-License-Identifier: MIT
 */
package org.neidhardt.rxlocation

import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority


object LocationRequests {

	fun precise(updateRateMillis: Long): LocationRequest {
		return LocationRequest.create().apply {
			interval = updateRateMillis
			priority = Priority.PRIORITY_HIGH_ACCURACY
		}
	}

	fun balanced(updateRateMillis: Long): LocationRequest {
		return LocationRequest.create().apply {
			interval = updateRateMillis
			priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
		}
	}

	fun cheap(updateRateMillis: Long): LocationRequest {
		return LocationRequest.create().apply {
			interval = updateRateMillis
			priority = Priority.PRIORITY_LOW_POWER
		}
	}
}