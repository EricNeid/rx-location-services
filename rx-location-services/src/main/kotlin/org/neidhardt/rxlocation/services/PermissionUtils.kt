package org.neidhardt.rxlocation.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import org.neidhardt.rxlocation.exceptions.MissingPermissionCoarseLocation
import org.neidhardt.rxlocation.exceptions.MissingPermissionFineLocation

internal val Context.hasPermissionFineLocation: Boolean
	get() = ContextCompat.checkSelfPermission(this,
			Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

internal val Context.hasPermissionCoarseLocation: Boolean
	get() = ContextCompat.checkSelfPermission(this,
			Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

internal fun getErrorForMissingPermission(context: Context): Throwable {
	return if (!context.hasPermissionFineLocation) {
		MissingPermissionFineLocation("Fine location permission is missing, make sure to ask the user for it")
	} else if (!context.hasPermissionCoarseLocation) {
		MissingPermissionCoarseLocation("Coarse location permission is missing, make sure to ask the user for it")
	} else {
		Throwable("Required permission is missing")
	}
}

internal fun isRequiredPermissionGranted(context: Context): Boolean {
	return context.hasPermissionFineLocation || context.hasPermissionCoarseLocation
}
