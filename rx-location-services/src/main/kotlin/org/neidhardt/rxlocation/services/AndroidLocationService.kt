/*
 * SPDX-FileCopyrightText: 2021 Eric Neidhardt
 * SPDX-License-Identifier: MIT
 */
package org.neidhardt.rxlocation.services

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import org.neidhardt.rxlocation.exceptions.MissingPermissionCoarseLocation
import org.neidhardt.rxlocation.exceptions.MissingPermissionFineLocation
import org.neidhardt.rxlocation.exceptions.ProviderDisabled

/**
 * [AndroidLocationService] is a wrapper for stock android location manager to use rx.
 *
 * @property context android context, should be Application context.
 * @constructor creates a new instance that does nothing on start
 */
class AndroidLocationService(private val context: Context) {

	private val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

	/**
	 * [lastKnowLocation] returns the last location which was obtained from this repository.
	 * This location may be null, if no location was obtained before.
	 */
	var lastKnowLocation: Location? = null

	/**
	 * [getLocationUpdates] returns Flowable for continuous location updates.
	 * It does not check if google play services are present on the device.
	 * It does check if booth permission [Manifest.permission.ACCESS_FINE_LOCATION] and [Manifest.permission.ACCESS_COARSE_LOCATION]
	 * are granted. If permission is missing, it emits error of either [MissingPermissionFineLocation] or [MissingPermissionCoarseLocation].
	 *
	 * @param updateIntervalMs minimum time interval between location updates, in milliseconds
	 * @param minDistance minimum distance between location updates, in meters
	 * @return location updates
	 */
	@Suppress("unused")
	@SuppressLint("MissingPermission")
	fun getLocationUpdates(updateIntervalMs: Long, minDistance: Float, provider: String): Flowable<Location> {
		return Flowable.create({ emitter ->

			// callback for location updates
			val locationUpdateCallback = object : LocationListener {
				override fun onLocationChanged(location: Location?) {
					location?.let {
						lastKnowLocation = it
						emitter.onNext(it)
					}
				}

				override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
				}

				override fun onProviderEnabled(provider: String?) {
				}

				override fun onProviderDisabled(provider: String?) {
					emitter.onError(ProviderDisabled("Provider ${provider ?: "null"} was disabled"))
				}
			}

			// stop client, if observer unsubscribe
			emitter.setCancellable {
				locationManager.removeUpdates(locationUpdateCallback)
			}

			// star receiving updates
			if (!isRequiredPermissionGranted(context)) {
				emitter.onError(getErrorForMissingPermission(context))
			} else {
				// request location updates
				try {
					locationManager.requestLocationUpdates(
							provider,
							updateIntervalMs,
							minDistance,
							locationUpdateCallback,
							null
					)
				} catch (e: Exception) {
					emitter.onError(e)
				}
			}

		}, BackpressureStrategy.LATEST)
	}
}
