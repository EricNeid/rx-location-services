package org.neidhardt.rxlocation.utils

import io.reactivex.Observable

/**
 * batchBearings tries to smooth emitted bearing by collecting [batchSize] values in a buffer
 * and returns the arithmetic mean of the buffered values.
 */
fun Observable<Float>.batchBearingsMean(batchSize: Int): Observable<Float> {
	return this.buffer(batchSize).map { batch ->
		var sum = 0f
		batch.forEach { sum += it }
		sum / batch.size
	}
}