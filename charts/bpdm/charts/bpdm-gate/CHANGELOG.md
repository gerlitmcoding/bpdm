# Changelog

All notable changes to this project will be documented in this file.

The format is based on Keep a Changelog (https://keepachangelog.com/en/1.0.0/),

## [4.1.0] - 2023-11-03

### Changed

- increase to app version 4.1.0

## [4.0.1] - 2023-08-28

### Changed

- increase to app version 4.0.1

## [4.0.0] - 2023-08-18

### Changed

- increase to app version 4.0.0
- add missing license headers to ingress templates
- change default registry for image to dockerhub

### Added

- postgres chart dependency for persistence

## [3.3.0] - 2023-03-17

### Changed

- increase to app version 3.2.0

## [3.2.0] - 2023-03-16

### Changed

- Startup, Readiness and Liveness probes can now be fully configured over the values

## [3.1.0] - 2023-03-08

### Changed

- increase to app version 3.1.0

## [3.0.6] - 2023-02-24

### Changed

- increase to app version 3.0.3

## [3.0.5] - 2023-02-16

### Changed

- support app version 3.0.2

### Fixed

- fixed bug causing missing apiVersion on Ingress resource
- fixed port of startup probe
- fixed liveness probe endpoint

## [3.0.4] - 2022-01-27

### Added

- LICENSE file
- README file
- Copyright headers
- CHANGELOG file

## [3.0.3] - 2022-01-25

### Changed

- AppVersion to 3.0.1

## [3.0.2] - 2022-01-23

## Changed

- Image now being pulled from catenax-ng/tx-bpdm by default.

## [3.0.1] - 2022-01-20

## Changed

- AppVersion to 3.0.0