package org.neidhardt.rxlocation.exceptions

/**
 * MissingSensor indicates that one of the required sensors for this operation could not be accessed.
 * This is a rare error an could mean that the phone is missing the required hardware.
 *
 * @param message - detailed explanation of error cause
 */
class MissingSensor(message: String?) : Throwable(message, null)