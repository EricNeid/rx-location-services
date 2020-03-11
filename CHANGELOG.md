<!-- markdownlint-disable MD022 MD032 MD024-->
# Changelog
All notable changes to this project will be documented in this file.

## Unreleased

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
