# Crossword Puzzle Generator V4

## overview
Uses backtracking algorithm.
It selects an anchor word, places it on the grid horizontally.
recursively attempts to place remaining words while expanding the grid dynamically if needed.
The program ensure each word intersects with at least one existing word on the grid.

## compile and execute
to compile:
javac -d bin src/*.java

to execute:
java -cp bin crossWordV4

## example input/output

    Crossword Puzzle Generator V4
    How many words?
    > 5
    Enter Word #1: coincidentally
    Enter Word #2: trigonometry
    Enter Word #3: xylophone
    Enter Word #4: megalovania
    Enter Word #5: guitar

    Anchor Word: COINCIDENTALLY

    All words placed successfully
    Final Grid Size: 15 x 15
    Words Placed: COINCIDENTALLY
    Words Placed: TRIGONOMETRY
    Words Placed: MEGALOVANIA
    Words Placed: XYLOPHONE
    Words Placed: GUITAR
    -  X  -  -  -  -  -  -  -  -  -  -  -  -  -  
    -  Y  -  -  -  G  -  -  -  -  -  -  -  -  -  
    -  L  -  -  -  U  -  M  -  -  -  -  -  -  -  
    C  O  I  N  C  I  D  E  N  T  A  L  L  Y  -  
    -  P  -  -  -  T  -  G  -  R  -  -  -  -  -  
    -  H  -  -  -  A  -  A  -  I  -  -  -  -  -  
    -  O  -  -  -  R  -  L  -  G  -  -  -  -  -  
    -  N  -  -  -  -  -  O  -  O  -  -  -  -  -  
    -  E  -  -  -  -  -  V  -  N  -  -  -  -  -  
    -  -  -  -  -  -  -  A  -  O  -  -  -  -  -  
    -  -  -  -  -  -  -  N  -  M  -  -  -  -  -  
    -  -  -  -  -  -  -  I  -  E  -  -  -  -  -  
    -  -  -  -  -  -  -  A  -  T  -  -  -  -  -  
    -  -  -  -  -  -  -  -  -  R  -  -  -  -  -  
    -  -  -  -  -  -  -  -  -  Y  -  -  -  -  -  

## limitations
- Minimum grid size is 15X15
- words must follow placement constraints
- Program does not yet create word clues or numbering
- only prints final puzzle pattern to console
- no GUI, console-based only

## Classes and methods

### `crossWordV4`
Main class that handles **user input**, calls other classes, and **prints the final puzzle**.

**Key Methods:**
- `main(String[] args)` — Entry point; handles word input, initializes the grid, selects anchor, runs backtracking, and prints the final puzzle.


### `wordKeeper`
Class to **keep track of all words**.  
Stores the **word**, a **generated ID**, and a **character array** of its letters.

**Constructor:**
- `wordKeeper(String word, int id)` — Initializes the word, assigns a unique ID, and stores the letters in a `char[]` array.




