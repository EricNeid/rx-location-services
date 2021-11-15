<!--
SPDX-FileCopyrightText: 2021 Eric Neidhardt
SPDX-License-Identifier: CC-BY-4.0
-->
<!-- markdownlint-disable MD022 MD032 MD024-->
# Changelog
All notable changes to this project will be documented in this file.

## Unreleased

### Changed (Breaking)
* Update to rxjava3 and rxkotlin3

## v0.7.0 - 2021-09-30
### Changed
* Dokka for javadoc generation increased to version 1.5.30

## v0.6.1 - 2021-03-30
### Changed (Breaking)
* First release to maven central
* groupId has changed from org.neidhardt to com.github.ericneid

## v0.5.0 - 2020-04-06
### Added
* AndroidLocationService to get location updates via stock LocationManager
### Changed
* Update to google play services 17.0.0
### Fixed
* lastKnownBearing not updated when using rotation sensor

## v0.4.0 - 2020-03-11
### Added
* Bearing calculation using rotation sensor
### Changed
* Renamed getBearingUpdates -> getBearingUpdatesFromMagneticAndAccelerometer
* Azimuth from bearing is wrapped in Bearing class, sensor accuracy is also provided  

## v0.3.0 - 2020-03-06
### Added
* Helper to calculate average angle, useful to obtain more smooth values
### Fixed
* Fixed lastKnownBearing was never set

## v0.2.0 - 2020-03-02
### Changed
* Update rx libraries to latest
* Rename getLocation to getLastKnowLocationFromDevice

## v0.1.0 - 2020-02-11
### Added
* Initial commit
* rx implementation to retrieve bearing
* rx implementation to retrieve position updates
