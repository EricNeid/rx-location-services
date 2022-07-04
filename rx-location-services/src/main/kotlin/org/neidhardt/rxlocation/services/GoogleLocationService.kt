/*
 * SPDX-FileCopyrightText: 2021 Eric Neidhardt
 * SPDX-License-Identifier: MIT
 */
package org.neidhardt.rxlocation.services

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.neidhardt.rxlocation.exceptions.MissingPermissionCoarseLocation
import org.neidhardt.rxlocation.exceptions.MissingPermissionFineLocation

/**
 * [GoogleLocationService] is a wrapper for FusedLocationProviderClient to use rx.
 *
 * @property context android context required for FusedLocationProviderClient, should be Application context.
 * @constructor creates a new instance that does nothing on start
 */
class GoogleLocationService(private val context: Context) {

	private val client = LocationServices.getFusedLocationProviderClient(context)

	/**
	 * [lastKnowLocation] returns the last location which was obtained from this repository.
	 * This location may be null, if no location was obtained before.
	 */
	var lastKnowLocation: Location? = null

	/**
	 * [getLastKnowLocationFromDevice] returns the last location obtained by any google
	 * location service on this device. It doest not retrieve a new location.
	 * To get a new location, it is usually better to call: getLocationUpdates(...).subscribe.first(...).
	 *
	 * It does not check if google play services are present on the device.
	 * It does check if booth permission [Manifest.permission.ACCESS_FINE_LOCATION]
	 * and [Manifest.permission.ACCESS_COARSE_LOCATION] are granted.
	 * If permission is missing, it emits error of either [MissingPermissionFineLocation]
	 * or [MissingPermissionCoarseLocation].
	 *
	 * @return single location update
	 */
	@Suppress("unused")
	@SuppressLint("MissingPermission")
	fun getLastKnowLocationFromDevice(): Single<Location> {

		return Single.create { emitter ->

			if (!isRequiredPermissionGranted(context)) {
				emitter.onError(getErrorForMissingPermission(context))
			} else {
				// request last know location from client with async task
				val asyncLocationTask = this.client.lastLocation
				asyncLocationTask.addOnSuccessListener { location ->
					if (location != null) {
						lastKnowLocation = location
						emitter.onSuccess(location)
					} else {
						emitter.onError(Throwable("Received location is null"))
					}
				}
				asyncLocationTask.addOnFailureListener { error ->
					emitter.onError(error)
				}
			}
		}
	}

	/**
	 * [getLocationUpdates] returns Flowable for continuous location updates.
	 * It does not check if google play services are present on the device.
	 * It does check if booth permission [Manifest.permission.ACCESS_FINE_LOCATION] and [Manifest.permission.ACCESS_COARSE_LOCATION]
	 * are granted. If permission is missing, it emits error of either [MissingPermissionFineLocation] or [MissingPermissionCoarseLocation].
	 *
	 * @param locationRequest to set update interval, precision, power usage, etc.
	 * @return location updates
	 */
	@Suppress("unused")
	@SuppressLint("MissingPermission")
	fun getLocationUpdates(locationRequest: LocationRequest): Flowable<Location> {

		return Flowable.create({ emitter ->

			// callback for location updates
			val locationUpdateCallback = object : LocationCallback() {
				override fun onLocationResult(locationResult: LocationResult) {
					locationResult.lastLocation.let {
						lastKnowLocation = it
						emitter.onNext(it)
					}
				}
			}

			// stop client, if observer unsubscribe
			emitter.setCancellable {
				client.removeLocationUpdates(locationUpdateCallback)
			}

			// star receiving updates
			if (!isRequiredPermissionGranted(context)) {
				emitter.onError(getErrorForMissingPermission(context))
			} else {
				// request location updates
				client.requestLocationUpdates(locationRequest, locationUpdateCallback, null)
			}

		}, BackpressureStrategy.LATEST)
	}
}
