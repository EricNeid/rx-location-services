package org.neidhardt.rxlocation.utils

import io.reactivex.Flowable
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by neid_ei (eric.neidhardt@dlr.de)
 * on 06.05.2019.
 */
class BearingUtilsKtTest {

	@Test
	fun batchBearingsMean() {
		// arrange
		val testBearings: Flowable<Float> = Flowable.just(0f, 1f, 2f, 4f, 6f, 8f)
		// action
		val resultFirst = testBearings.batchBearingsMean(3).blockingFirst()
		val resultSecond = testBearings.batchBearingsMean(3).blockingLast()
		// verify
		assertEquals(1f, resultFirst)
		assertEquals(6f, resultSecond)
	}
}