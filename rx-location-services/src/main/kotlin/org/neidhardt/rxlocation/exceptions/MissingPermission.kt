package org.neidhardt.rxlocation.exceptions

/**
 * [MissingPermissionFineLocation] indicates that permission Manifest.permission.ACCESS_FINE_LOCATION
 * is missing and required for the operation.
 *
 * @param message - detailed explanation of error cause
 */
class MissingPermissionFineLocation(message: String?) : Throwable(message, null)

/**
 * [MissingPermissionFineLocation] indicates that permission Manifest.permission.ACCESS_COARSE_LOCATION
 * is missing and required for the operation.
 *
 * @param message - detailed explanation of error cause
 */
class MissingPermissionCoarseLocation(message: String?) : Throwable(message, null)