# Changelog
All notable changes will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.5.0] - 2023-11-08
### Added
- Maven compatibility
- The ability to reflect sprites across the x and y axes (flip sprites backwards or upside-down)

### Changed
- The JBurst class is now statically focused

### Fixed
- Null errors when images couldn't be found
- Sprites jumping around during transformations

## [0.4.0] - 2023-10-14
### Added
- Deprecation warnings for risky or useless Swing functions left over in JBurstSprite

### Fixed
- Occasional frame jumping

### Removed
- JBurstGroup

### Security
- Removed the ability to tamper with JBurst

## [0.3.0] - 2023-09-19
### Added
- Internal sprite management system

### Removed
- The need for an external `while(true)` loop
- Several classes that served little purpose

## [0.2.1] - 2023-08-31
### Added
- The ability to rotate sprites

### Fixed
- Graphics objects never being disposed, preventing potential memory leaks
- Sprite transformation system (Should be more accurate)

## [0.2.0] - 2023-8-23
### Added
- JSON support for texture atlases
- Callbacks within the animation classes that are called each frame and when an animation finishes
- Simple graphic construction using `makeGraphic()`
- The ability to write to a sprite's graphics
- The ability to scale sprites (grow or shrink them)

### Fixed
- A previously overlooked issue where the sprite boundaries were way out of proportion

## [0.1.0] - 2023-07-08
### Added
- Custom sprites with animation support
- Sparrow texture atlas support