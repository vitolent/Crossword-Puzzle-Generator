import java.util.ArrayList;

public class Grid {
    private char[][] board;
    private int rows;
    private int cols;

    // constructor for the board object, starts with a 15x15 grid
    public Grid(int r, int c) {
        this.rows = r;
        this.cols = c;
        board = new char[rows][cols];

        // Initialize grid with empty characters as "-"
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = '-';
            }
        }
    }

    public char[][] getBoard(){
        return board;
    }

    public void displayGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
        }
    }

    public void wordPlacer(placedWord pw, ArrayList<placedWord> placedWordList) {
        expandToFit(pw, placedWordList);

        for (int i = 0; i < pw.word.letters.length; i++) {
            int r = pw.isVertical ? pw.row + i : pw.row;
            int c = pw.isVertical ? pw.col : pw.col + i;
            board[r][c] = pw.word.letters[i];
        }
    }

    // for backtracking
    public void removeWord(placedWord pw, ArrayList<placedWord> placedWordList) {
        for (int i = 0; i < pw.word.letters.length; i++) {
            int r = pw.isVertical ? pw.row + i : pw.row;
            int c = pw.isVertical ? pw.col : pw.col + i;

            boolean shared = false;
            for (placedWord other : placedWordList) {
                if (other == pw) continue;
                for (int j = 0; j < other.word.letters.length; j++) {
                    int or = other.isVertical ? other.row + j : other.row;
                    int oc = other.isVertical ? other.col : other.col + j;
                    if (or == r && oc == c) shared = true;
                }
            }

            if (!shared) {
                board[r][c] = '-';
            }
        }
    }


    // checks if a letter exists in the grid
    public boolean containsLetter(char ch) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == ch) {
                    return true;
                }
            }
        }
        return false;
    } 
    
    // returns number of rows and columns
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }

    // gets the character at a specific cell
    public char getCell(int r, int c) { 
        return board[r][c]; 
    }

    // word placement adjacency check
    // ensure placed words do not touch  
    public boolean hasIllegalSideAdjacency(int r, int c, boolean vertical) {
        if (vertical) {
            if (c > 0 && getCell(r, c - 1) != '-') return true;
            if (c < cols - 1 && getCell(r, c + 1) != '-') return true;
        } else {
            if (r > 0 && getCell(r - 1, c) != '-') return true;
            if (r < rows - 1 && getCell(r + 1, c) != '-') return true;
        }
        return false;
    }

    // method to check if given row and column are within grid bounds
    // ensures no out of bounds errors
    public boolean inBounds(int r, int c) { 
        return r >= 0 && r < rows && c >= 0 && c < cols; 
    }

    // check boundary cells before and after the word placement
    // prevents words to incorrectly combine
    public boolean checkBoundary(placedWord pw) {
        int len = pw.word.letters.length;
        int rStartPrev = pw.isVertical ? pw.row - 1 : pw.row;
        int cStartPrev = pw.isVertical ? pw.col : pw.col - 1;
        if (inBounds(rStartPrev, cStartPrev) && getCell(rStartPrev, cStartPrev) != '-') return false;

        int rEndNext = pw.isVertical ? pw.row + len : pw.row;
        int cEndNext = pw.isVertical ? pw.col : pw.col + len;
        if (inBounds(rEndNext, cEndNext) && getCell(rEndNext, cEndNext) != '-') return false;

        return true;
    }

    // Expand grid only in the direction needed
    public void expandToFit(placedWord pw, ArrayList<placedWord> placedWordList) {
        int endRow = pw.isVertical ? pw.row + pw.word.letters.length - 1 : pw.row;
        int endCol = pw.isVertical ? pw.col : pw.col + pw.word.letters.length - 1;

        int rowOffset = 0;
        int colOffset = 0;

        int newRows = rows;
        int newCols = cols;

        // Expand downward/rightward if needed
        if (endRow >= rows) newRows = endRow + 1;
        if (endCol >= cols) newCols = endCol + 1;

        // Expand upward/leftward if needed
        if (pw.row < 0) {
            rowOffset = -pw.row;
            newRows = rows + rowOffset;
        }
        if (pw.col < 0) {
            colOffset = -pw.col;
            newCols = cols + colOffset;
        }

        // Enforce minimum size
        if (newRows < 15) newRows = 15;
        if (newCols < 15) newCols = 15;

        // Nothing to do
        if (newRows == rows && newCols == cols && rowOffset == 0 && colOffset == 0) return;

        // Create new board
        char[][] newBoard = new char[newRows][newCols];
        for (int r = 0; r < newRows; r++)
            for (int c = 0; c < newCols; c++)
                newBoard[r][c] = '-';

        // Copy old board into new board at proper offset
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newBoard[r + rowOffset][c + colOffset] = board[r][c];
            }
        }

        // Shift coordinates of all previously placed words if we expanded upward/leftward
        if (rowOffset != 0 || colOffset != 0) {
            for (placedWord w : placedWordList) {
                w.row += rowOffset;
                w.col += colOffset;
            }
            // Shift candidate word too
            pw.row += rowOffset;
            pw.col += colOffset;
        }

        // Apply new board
        board = newBoard;
        rows = newRows;
        cols = newCols;
    }



    // trim extra empty rows and columns from the grid
    public void trimGrid() {
        int minR = rows, maxR = 0, minC = cols, maxC = 0;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (board[r][c] != '-') {
                    minR = Math.min(minR, r);
                    maxR = Math.max(maxR, r);
                    minC = Math.min(minC, c);
                    maxC = Math.max(maxC, c);
                }

        int height = maxR - minR + 1;
        int width = maxC - minC + 1;
        int size = Math.max(height, width);

        if (size < 15) size = 15; // enforce minimum

        char[][] newBoard = new char[size][size];
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                newBoard[r][c] = '-';

        int offsetR = (size - height) / 2;
        int offsetC = (size - width) / 2;

        for (int r = minR; r <= maxR; r++)
            for (int c = minC; c <= maxC; c++)
                newBoard[r - minR + offsetR][c - minC + offsetC] = board[r][c];

        board = newBoard;
        rows = size;
        cols = size;
    }

    public void syncPlacedWords(ArrayList<placedWord> placedWordList) {
    for (placedWord pw : placedWordList) {
        // Look for the first letter of the word on the grid
        char firstLetter = pw.word.letters[0];
        boolean found = false;

        for (int r = 0; r < rows && !found; r++) {
            for (int c = 0; c < cols && !found; c++) {
                if (board[r][c] == firstLetter) {
                    // Check orientation match
                    boolean matchesVert = true;
                    boolean matchesHorz = true;

                    for (int i = 0; i < pw.word.letters.length; i++) {
                        // Vertical check
                        if (r + i >= rows || board[r + i][c] != pw.word.letters[i]) matchesVert = false;
                        // Horizontal check
                        if (c + i >= cols || board[r][c + i] != pw.word.letters[i]) matchesHorz = false;
                    }

                    if (matchesVert) {
                        pw.row = r;
                        pw.col = c;
                        pw.isVertical = true;
                        found = true;
                    } else if (matchesHorz) {
                        pw.row = r;
                        pw.col = c;
                        pw.isVertical = false;
                        found = true;
                    }
                }
            }
        }
    }
}


}
