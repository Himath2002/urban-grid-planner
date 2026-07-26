# Changelog

All notable changes are documented here. Versions follow [Semantic Versioning](https://semver.org/).

## [Unreleased]

## [1.0.0] - 2026-07-27

### Added

- strict UTF-8 grid loader with bounded input and contextual validation;
- immutable plans, estimates, placements, and city results;
- uniform, random, and centre-weighted planning strategies;
- composable heritage, height, flood-risk, and contamination rules;
- deterministic ASCII terrain, selection, and city-build views;
- focused tests across parsing, domain policy, strategy, orchestration, and rendering;
- Java 21 Gradle build, compiler linting, PMD, CI, and dependency automation;
- architecture, security, contribution, and usage documentation.

### Changed

- separated console, parser, domain, policy, strategy, service, and presentation concerns;
- replaced stringly typed foundations with a validated enum;
- removed UI and logging dependencies from domain classes;
- consolidated city-building output behind a typed result boundary.

### Removed

- generated grids, bundled binary tooling, build caches, IDE metadata, and legacy scoring configuration.

[Unreleased]: https://github.com/Himath2002/urban-grid-planner/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/Himath2002/urban-grid-planner/releases/tag/v1.0.0
