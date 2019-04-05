package org.neidhardt.rxlocation.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*
import de.dlr.ts.apptemplate.permissions.hasPermissionFineLocation
import de.dlr.ts.apptemplate.repositories.LocationRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * GoogleLocationService
 */
class GoogleLocationService(private val context: Context) : LocationRepository {

	private val client = LocationServices.getFusedLocationProviderClient(context)

	/**
	 * lastKnowLocation returns the last location which was obtained from this repository.
	 * This location may be null, if no location was obtained before.
	 * @return Location or null
	 */
	override var lastKnowLocation: Location? = null

	/**
	 * getLastKnowLocation returns the first new location available.
	 * It does not check if google play services are present on the device.
	 * It does check if booth permission Manifest.permission.ACCESS_FINE_LOCATION and Manifest.permission.ACCESS_COARSE_LOCATION
	 * are granted. If permission is missing, it emits error of either {@link MissingPermissionFineLocation} or {@link MissingPermissionCoarseLocation}.
	 *
	 * @return Single<Location>
	 */
	override fun getLastKnowLocation(): Single<Location> {

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
	 * getLocationUpdates returns Observable for continuous location updates.
	 * It does not check if google play services are present on the device.
	 * It does check if booth permission Manifest.permission.ACCESS_FINE_LOCATION and Manifest.permission.ACCESS_COARSE_LOCATION
	 * are granted. If permission is missing, it emits error of either {@link MissingPermissionFineLocation} or {@link MissingPermissionCoarseLocation}.
	 * @return Flowable<Location>
	 */
	override fun getLocationUpdates(): Flowable<Location> {

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
				this.client.requestLocationUpdates(LocationRequest(), locationUpdateCallback, null)
			}

		}, BackpressureStrategy.LATEST)
	}
}

private fun isRequiredPermissionGranted(context: Context) {
	return context.hasPermissionFineLocation || context.hasPermissionCoarseLocation
}

private fun getErrorForMissingPermission(context: Context): Throwable {
	return if (!this.context.hasPermissionFineLocation) {
		MissingPermissionFineLocation("Fine location permission is missing, make sure to ask the user for it")
	} else if (!this.context.hasPermissionCoarseLocation) {
		MissingPermissionCoarseLocation("Coarse location permission is missing, make sure to ask the user for it")
	}
}

private val Context.hasPermissionFineLocation: Boolean
	get() = ContextCompat.checkSelfPermission(this,
			Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

private val Context.hasPermissionCoarseLocation: Boolean
	get() = ContextCompat.checkSelfPermission(this,
			Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED