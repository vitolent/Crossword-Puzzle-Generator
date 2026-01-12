## **Summary**

This is a Java project that automatically generates crossword puzzles from user-inputted words and clues. Users can create puzzles, save them, and play them through an interactive graphical interface. The system uses a backtracking algorithm to intelligently place words on a grid, ensuring they intersect properly without conflicts.

---

## System Overview

**What it does:**

- Takes a list of words and clues from the user
- Automatically generates a valid crossword puzzle layout
- Saves puzzles to files for later use
- Provides an interactive GUI for solving puzzles
- Validates solutions and tracks progress

**Main Features:**

- Automatic word placement using algorithms
- Dynamic grid sizing (expands/shrinks as needed)
- Word intersection detection
- Arrow key navigation in the player interface
- Save/load functionality for puzzles

---

## Architecture

The project is organized into 5 packages:

### 1. **crosswordPuzzle.core**

Basic building blocks - the grid, words, and their placements

### 2. **crosswordPuzzle.algorithm**

The brain of the application - handles puzzle generation logic

### 3. **crosswordPuzzle.io**

File operations - saving and loading puzzles

### 4. **crosswordPuzzle.ui**

Graphical interface for playing puzzles

### 5. **crosswordPuzzle.misc**

Helper utilities like clue printing

---

## Data Structure

### Core Data Classes

**wordKeeper**

- Stores: word text, unique ID, clue, character array
- Purpose: Represents a single word in the puzzle

**placedWord**

- Stores: wordKeeper object, row, column, orientation (vertical/horizontal)
- Purpose: Represents a word placed on the grid with its position

**Grid (char[][] board)**

- 2D character array
- '-' represents empty cells
- Letters represent placed words
- Dynamically resizes to fit words

### File Storage

Puzzles are saved as `.dat` files in the `savedPuzzles` folder using Java serialization:

- Grid (char[][])
- List of placed words
- List of all words with clues

---

## Core Programs

### Grid.java

**Purpose:** Manages the crossword grid and all grid operations

**Key Methods:**

- `wordPlacer()` - Places a word on the grid
- `removeWord()` - Removes a word (used during backtracking)
- `expandToFit()` - Grows the grid when words don't fit
- `trimGrid()` - Removes extra empty space around the puzzle
- `containsLetter()` - Checks if a letter exists on the grid
- `hasIllegalSideAdjacency()` - Ensures words don't touch sides
- `checkBoundary()` - Validates word boundaries

**How it works:** The grid starts at minimum 15x15 or depending on the anchor word length and expands as needed. It grid rules like words can't touch sides and must have proper spacing.

---

### generateNewPuzzle.java

**Purpose:** Main workflow controller for creating new puzzles

**Process:**

1. Asks user for number of words
2. Collects words and clues
3. Asks for puzzle title
4. Selects anchor word (best starting word)
5. Places anchor word at center horizontally
6. Calls backtracking algorithm to place remaining words
7. Saves the completed puzzle

---

### backtrack.java

**Purpose:** Implements the backtracking algorithm to place all words

**Algorithm Flow:**

1. If all words are placed, success. Trim grid and finish (acts as the base case)
2. Select next word to place (using `nextWordSelector`)
3. Generate all possible placements (using `placementEvaluator`)
4. Try each placement:
    - Place word on grid
    - Recursively try to place remaining words
    - If successful → Done!
    - If failed → Remove word and try next placement
5. If no placements work → Backtrack to previous word

---

### crosswordGUI.java

**Purpose:** Interactive graphical interface for playing puzzles

**Features:**

- Grid with single-character input cells
- Arrow key navigation between cells
- Word numbers displayed in cells
- Scrollable clues panel (ACROSS and DOWN)
- "Check Puzzle" button to verify solution
- Automatic uppercase conversion

**Components:**

- Main grid (JTextField arrays)
- Clues panel (scrollable list)
- Check button (validates solution)

---

## Utility Programs

### puzzleManager.java

**Purpose:** Handles saving and loading puzzles

**Methods:**

- `savePuzzle()` - Serializes puzzle data to .dat file
- `loadPuzzle()` - Deserializes puzzle from file
- `listSavedPuzzles()` - Shows all available puzzles

**File Naming:** Title with underscores (e.g., "My Puzzle" → "My_Puzzle.dat")

---

### cluePrinter.java

**Purpose:** Prints clues organized by direction

**Output Format:**

```
=== ACROSS ===
1. | First clue here
2. | Another clue here

=== DOWN ===
2. | Vertical clue here
3. | Another vertical clue
```

---

## Key Algorithms

### 1. Anchor Word Selection (anchorSelector.java)

**Goal:** Choose the best starting word

**Strategy:**

1. Count frequency of each letter across all words
2. Score each word by summing its letter frequencies
3. Word with highest score = anchor word
4. If tie, prefer longer word

**Why:** Words with common letters are easier to connect with other words

---

### 2. Next Word Selection (nextWordSelector.java)

**Goal:** Choose which word to place next

**Strategy:**

1. For each unused word, count how many of its letters exist on the grid
2. Word with most matches = next word to place
3. If tie, prefer longer word

**Why:** Words that share more letters with the grid are suitable for backtracking, leaving more options for future words

---

### 3. Placement Validation (placementEvaluator.java)

**Goal:** Find all valid positions for a word

**Process:**

1. Try every cell position (row, column)
2. Try both orientations (horizontal, vertical)
3. Check constraints for each candidate

**Constraints:**

- Must overlap at least one existing letter
- Can't conflict with existing letters (different letter in same cell)
- Can't touch other words on the sides
- Must have empty space before/after the word

**Optimization:** Sorts valid placements by number of overlaps (more overlaps = better)

---

## Logic Flow

### Creating New Puzzle

```
User Input
    ↓
Enter number of words
    ↓
Enter each word + clue
    ↓
Enter puzzle title
    ↓
System selects anchor word
    ↓
Anchor placed at center
    ↓
Backtracking places remaining words
    ↓
Grid trimmed to final size
    ↓
Puzzle saved to file
    ↓
Success message displayed
```

### Playing a Puzzle

```
Load saved puzzle
    ↓
Display grid + clues
    ↓
User fills in letters
    ↓
Arrow keys to navigate
    ↓
Click "Check Puzzle"
    ↓
System validates solution
    ↓
Show success/failure message
```

### Backtracking Algorithm Flow

```
Start with unused words list
    ↓
All words placed? → YES → Success!
    ↓ NO
Select next best word
    ↓
Generate valid placements
    ↓
Try first placement
    ↓
Recursively place remaining words
    ↓
Success? → YES → Done!
    ↓ NO
Remove word, try next placement
    ↓
All placements failed? → Backtrack to previous word
```

---

## Quick Start Guide

**To Create a Puzzle:**

1. Run `generateNewPuzzle.generatePuzzle()`
2. Enter number of words
3. Enter each word and its clue
4. Enter a title
5. Puzzle is automatically generated and saved

**To Play a Puzzle:**

1. Load puzzle using `puzzleManager.loadPuzzle(title)`
2. Create GUI: `new crosswordGUI(grid, placedWords)`
3. Fill in answers using keyboard
4. Use arrow keys to navigate
5. Click "Check Puzzle" to verify

---

## Technical Notes

- **Language:** Java
- **GUI Framework:** Swing
- **Storage:** Java Serialization (.dat files)
- **Grid Representation:** 2D char array
- **Empty Cell Symbol:** '-' character
- **Minimum Grid Size:** 15x15
- **Cell Size (GUI):** 45x45 pixels