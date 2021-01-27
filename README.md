# About

RX-location-services is a simple wrapper around some of Androids Location APIs. Currently supported:

* GoogleLocationService - using GoogleFusedLocationProvider for Location updates
* AndroidLocationService - using LocationManager for Location updates
* BearingSensorService - using accelerometer and magnetic sensor for bearing updates

It uses rxJava2 in it's responses.

## Gradle

```gradle
implementation 'org.neidhardt:rx-location-services:0.5.1'
```

## Usage

```kotlin
// create service instances in application
// you can use lazy loading, because applicationContext is only available after onCreate
val locationServiceGoogle: GoogleLocationService by lazy { GoogleLocationService(applicationContext) }

val locationServiceAndroid: AndroidLocationService by lazy { AndroidLocationService(applicationContext) }

val bearingService: BearingSensorService by lazy { BearingSensorService(applicationContext) }
```

```kotlin
// do not forget to dispose subscription
subscriptions.add(locationServiceGoogle.getLocationUpdates()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe({ location ->
        myLocationConsumer.onLocationChanged(location)
    }, { error ->
        // handle error
    }))

subscriptions.add(bearingService.getBearingUpdatesFromRotation()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe({ bearing ->
        myBearingConsumer.onBearingChanged(bearing.azimuth)
    }, { error ->
        // handle error
    }))
```

## Question or comments

Please feel free to open a new issue:
<https://github.com/EricNeid/rx-location-services/issues>
