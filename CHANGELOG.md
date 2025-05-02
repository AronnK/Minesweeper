# Changelog

## [1.0.1] - 2024-01-17

### Added
- Initial release of Minesweeper game
- Three difficulty levels: Easy, Medium, and Hard
- Timer functionality to track game duration
- Color-coded numbers for adjacent mine counts
- Right-click flag placement system
- Win/lose detection with game statistics

### Code Structure
- Modularized code into three main components:
  - `GameConstants.java`: Central configuration and constants
  - `GameBoard.java`: Game logic and state management
  - `Minesweeper.java`: UI and event handling

### Features
- Configurable grid size (currently set to 10x10)
- Difficulty-based mine percentages:
  - Easy: 10% mines
  - Medium: 15% mines
  - Hard: 20% mines
- Responsive UI with:
  - Timer display
  - Difficulty level indicator
  - Color-coded numeric hints
  - Mine flagging system

### Technical Improvements
- Proper encapsulation of game state in GameBoard class
- Centralized constants management
- Improved code organization and maintainability
- Enhanced UI responsiveness and user feedback
- Standardized color scheme for numeric hints