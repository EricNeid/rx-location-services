package org.neidhardt.rxlocation.model

import android.location.Location

data class Speed(
	val speedMs: Float,
	val sampleTimeMs: Long,
	val locations: List<Location>
)