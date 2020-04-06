package org.neidhardt.rxlocation.services

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.neidhardt.rxlocation.exceptions.MissingPermissionCoarseLocation
import org.neidhardt.rxlocation.exceptions.MissingPermissionFineLocation

/**
 * [AndroidLocationService] is a wrapper for stock android location manager to use rx.
 *
 * @property context android context required for FusedLocationProviderClient, should be Application context.
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
	 * @param locationRequest to set update interval, precision, power usage, etc.
	 * @return location updates
	 */
	@Suppress("unused")
	@SuppressLint("MissingPermission")
	fun getLocationUpdates(): Flowable<Location> {

		return Flowable.create({ emitter ->

			// callback for location updates
			val locationUpdateCallback = object : LocationListener {
				override fun onLocationChanged(location: Location?) {
					TODO("Not yet implemented")
				}

				override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
					TODO("Not yet implemented")
				}

				override fun onProviderEnabled(provider: String?) {
					TODO("Not yet implemented")
				}

				override fun onProviderDisabled(provider: String?) {
					TODO("Not yet implemented")
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
				// TODO start location updates
			}

		}, BackpressureStrategy.LATEST)
	}
}
