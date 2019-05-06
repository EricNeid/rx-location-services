package org.neidhardt.rxlocation.utils

import io.reactivex.Flowable
import io.reactivex.Observable

/**
 * batchBearings tries to smooth emitted bearing by collecting [batchSize] values in a buffer
 * and returns the arithmetic mean of the buffered values.
 */
fun Flowable<Float>.batchBearingsMean(batchSize: Int): Flowable<Float> {
	return this.buffer(batchSize).map { batch ->
		var sum = 0f
		batch.forEach { sum += it }
		sum / batch.size
	}
}