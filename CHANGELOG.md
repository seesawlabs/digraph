# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [0.3.0] - 2017-06-16
### Added
- Added the Digraph record and `make-digraph` construction function
- Improved the `::edge` spec by adding `::source-node` and `::target-node` specs to the tuple. This allows users to override each of these
generators separately if desired.

### Changed
- General code simplification and improvements.

## [0.2.0] - 2017-06-15
### Changed
- Renamed all occurrences of "vertex/vertices" with "node/nodes" for more direct semantics to network applications

## [0.1.0] - 2017-06-15
### Added
- First release!
