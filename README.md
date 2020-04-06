# About

RX-location-services is a simple wrapper around some of Androids Location APIs. Currently supported:

* GoogleLocationService - using GoogleFusedLocationProvider for Location updates
* AndroidLocationService - using LocationManager for Location updates
* BearingSensorService - using accelerometer and magnetic sensor for bearing updates

## Gradle

```gradle
implementation 'org.neidhardt:rx-location-services:0.4.0'
```

## Usage

```kotlin
// create service instances in application
val locationService: GoogleLocationService by lazy { GoogleLocationService(this.applicationContext) }

val bearingService: BearingSensorService by lazy { BearingSensorService(this.applicationContext) }
```

```kotlin
// do not forget to dispose subscription
this.subscriptions.add(locationService.getLocationUpdates()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe({ location ->
        myLocationConsumer.onLocationChanged(location)
    }, { error ->
        // handle error
    }))

this.subscriptions.add(bearingService.getBearingUpdatesFromRotation()
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
