package org.neidhardt.rxlocation.services

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import org.neidhardt.rxlocation.exceptions.MissingPermissionCoarseLocation
import org.neidhardt.rxlocation.exceptions.MissingPermissionFineLocation

/**
 * GoogleLocationService is a wrapper for FusedLocationProviderClient to use rx.
 *
 * @param context android context required for FusedLocationProviderClient, should be Application context.
 * @constructor creates a new instance that does nothing on start
 */
class GoogleLocationService(private val context: Context) {

	private val client = LocationServices.getFusedLocationProviderClient(context)

	/**
	 * lastKnowLocation returns the last location which was obtained from this repository.
	 * This location may be null, if no location was obtained before.
	 * @return [Location] or null
	 */
	var lastKnowLocation: Location? = null

	/**
	 * getLocation returns the first new location available.
	 * It does not check if google play services are present on the device.
	 * It does check if booth permission [Manifest.permission.ACCESS_FINE_LOCATION] and [Manifest.permission.ACCESS_COARSE_LOCATION]
	 * are granted. If permission is missing, it emits error of either [MissingPermissionFineLocation] or [MissingPermissionCoarseLocation].
	 *
	 * @return single location update
	 */
	@SuppressLint("MissingPermission")
	fun getLocation(): Single<Location> {

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
	 * getLocationUpdates returns Flowable for continuous location updates.
	 * It does not check if google play services are present on the device.
	 * It does check if booth permission [Manifest.permission.ACCESS_FINE_LOCATION] and [Manifest.permission.ACCESS_COARSE_LOCATION]
	 * are granted. If permission is missing, it emits error of either [MissingPermissionFineLocation] or [MissingPermissionCoarseLocation].
	 *
	 * @param locationRequest to set update interval, precision, power usage, etc.
	 * @return location updates
	 */
	@SuppressLint("MissingPermission")
	fun getLocationUpdates(locationRequest: LocationRequest): Flowable<Location> {

		return Flowable.create({ emitter ->

			// callback for location updates
			val locationUpdateCallback = object : LocationCallback() {
				override fun onLocationResult(locationResult: LocationResult?) {
					locationResult?.lastLocation?.let { location ->
						lastKnowLocation = location
						emitter.onNext(location)
					}
				}
			}

			// stop client, if observer unsubscribe
			emitter.setCancellable {
				this.client.removeLocationUpdates(locationUpdateCallback)
			}

			// star receiving updates
			if (!isRequiredPermissionGranted(context)) {
				emitter.onError(getErrorForMissingPermission(context))
			} else {
				// request location updates
				this.client.requestLocationUpdates(locationRequest, locationUpdateCallback, null)
			}

		}, BackpressureStrategy.LATEST)
	}
}

private fun isRequiredPermissionGranted(context: Context): Boolean {
	return context.hasPermissionFineLocation || context.hasPermissionCoarseLocation
}

private fun getErrorForMissingPermission(context: Context): Throwable {
	return if (!context.hasPermissionFineLocation) {
		MissingPermissionFineLocation("Fine location permission is missing, make sure to ask the user for it")
	} else if (!context.hasPermissionCoarseLocation) {
		MissingPermissionCoarseLocation("Coarse location permission is missing, make sure to ask the user for it")
	} else {
		Throwable("Required permission is missing")
	}
}

private val Context.hasPermissionFineLocation: Boolean
	get() = ContextCompat.checkSelfPermission(this,
			Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

private val Context.hasPermissionCoarseLocation: Boolean
	get() = ContextCompat.checkSelfPermission(this,
			Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
