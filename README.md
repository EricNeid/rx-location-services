# rx-location-services

A simple wrapper around some of Androids Location APIs. Currently only GoogleFusedLocationProvider 
is included.

**Gradle**

```gradle
implementation 'org.neidhardt:rx-location-services:0.0.5'
```

**Usage**

```kotlin

// do not forget to dispose subscription
this.subscriptions.add(this.locationRepository.getLocationUpdates()
	.observeOn(AndroidSchedulers.mainThread())
	.subscribe({ location ->
		myLocationConsumer?.onLocationChanged(location, this)
	}, { error ->
		// handle error
	}))

```