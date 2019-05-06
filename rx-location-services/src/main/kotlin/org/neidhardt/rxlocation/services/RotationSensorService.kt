package org.neidhardt.rxlocation.services

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * Created by neid_ei (eric.neidhardt@dlr.de)
 * on 06.05.2019.
 */
class RotationSensorService(context: Context) {

	private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
	private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)

	fun getRotationUpdates(): Flowable<Int> {

		return Flowable.create({ emitter ->

			val sensorUpdateCallback = object : SensorEventListener {
				override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

				override fun onSensorChanged(event: SensorEvent?) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}
			}

			emitter.setCancellable {
				this.sensorManager.unregisterListener(sensorUpdateCallback)
			}

			this.sensorManager.registerListener(
					sensorUpdateCallback,
					sensor,
					SensorManager.SENSOR_DELAY_GAME
			)

		}, BackpressureStrategy.BUFFER)
	}

}