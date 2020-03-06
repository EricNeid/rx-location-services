package org.neidhardt.rxlocation.utils

import org.junit.Test

import org.junit.Assert.*

class BearingUtilsKtTest {

	@Test
	fun calcAverageAngle() {
		assertEquals(12f, calcAverageAngle(listOf(12f)))

		assertEquals(190f, calcAverageAngle(listOf(180f, 200f)))

		assertEquals(0f, calcAverageAngle(listOf(340f, 20f)))

		assertEquals(5f, calcAverageAngle(listOf(340f, 30f)))

		assertEquals(350f, calcAverageAngle(listOf(340f, 340f, 360f, 0f)))

		assertEquals(355f, calcAverageAngle(listOf(350f, 15f, 340f)))
	}
}