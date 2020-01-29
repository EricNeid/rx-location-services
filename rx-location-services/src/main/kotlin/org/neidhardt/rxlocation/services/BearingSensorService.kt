package org.neidhardt.rxlocation.services

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.neidhardt.rxlocation.exceptions.MissingSensor

/**
 * Created by neid_ei (eric.neidhardt@dlr.de)
 * on 06.05.2019.
 */
class BearingSensorService(context: Context) {

	private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

	/**
	 * lastKnownBearing returns the last bearing which was calculated by any call for bearing updates.
	 */
	var lastKnownBearing: Float? = null

	/**
	 * getBearingUpdates returns Flowable for continuous bearing updates.
	 * It uses the accelerometer and the magnetic field sensor.
	 * If one of the required sensors is not available, [MissingSensor] is emitted.
	 * A bearing of 0.0 or 360.0 means magnetic north.
	 *
	 * @return bearing updates in degree
	 */
	fun getBearingUpdates(): Flowable<Float> {

		return Flowable.create({ emitter ->

			// both sensor are required to calculate bearing
			val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
			val accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

			// arrays to store sensor reading, used to calculate bearing
			val accelerometerReading = FloatArray(3)
			val magnetometerReading = FloatArray(3)
			var isAccelerometerReadingAvailable = false
			var isMagnetometerReadingAvailable = false

			// check if sensor is available
			if (rotationSensor == null) {
				emitter.onError(MissingSensor("Rotation sensor could not be accessed"))
			}
			if (accelerationSensor == null) {
				emitter.onError(MissingSensor("Acceleration sensor could not be accessed"))
			}

			// get updates from sensors
			val accelerationSensorsCallback = object : SensorEventListener {
				override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

				override fun onSensorChanged(event: SensorEvent?) {
					// store sensor reading
					if (event != null) {
						System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
						isAccelerometerReadingAvailable = true
					} else {
						emitter.onError(MissingSensor("Received acceleration sensor event is null"))
					}

					// if both reading are available, calculate bearing
					if (isAccelerometerReadingAvailable && isMagnetometerReadingAvailable) {
						emitter.onNext(calculateAzimuthDegree(accelerometerReading, magnetometerReading))
					}
				}
			}
			val rotationSensorUpdateCallback = object : SensorEventListener {
				override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

				override fun onSensorChanged(event: SensorEvent?) {
					// store sensor reading
					if (event != null) {
						System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
						isMagnetometerReadingAvailable = true
					} else {
						emitter.onError(Throwable("Received rotation sensor event is null"))
					}

					// if both reading are available, calculate bearing
					if (isAccelerometerReadingAvailable && isMagnetometerReadingAvailable) {
						emitter.onNext(calculateAzimuthDegree(accelerometerReading, magnetometerReading))
					}
				}
			}

			// unregister from sensors after unsubscribe
			emitter.setCancellable {
				this.sensorManager.unregisterListener(accelerationSensorsCallback)
				this.sensorManager.unregisterListener(rotationSensorUpdateCallback)
			}

			// start receiving updates
			this.sensorManager.registerListener(
					accelerationSensorsCallback,
					accelerationSensor,
					SensorManager.SENSOR_DELAY_GAME
			)

			// start receiving updates
			this.sensorManager.registerListener(
					rotationSensorUpdateCallback,
					rotationSensor,
					SensorManager.SENSOR_DELAY_GAME
			)

		}, BackpressureStrategy.LATEST)
	}

	private fun calculateAzimuthDegree(
			accelerometerReading: FloatArray,
			magnetometerReading: FloatArray
	): Float {
		val rotationMatrix = FloatArray(9)
		SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)

		val orientation = FloatArray(3)
		SensorManager.getOrientation(rotationMatrix, orientation)
		val azimuthInRadians = orientation[0]
		return (Math.toDegrees(azimuthInRadians.toDouble()) + 360).toFloat() % 360 // azimuth in degree
	}
}