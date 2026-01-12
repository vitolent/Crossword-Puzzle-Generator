package crosswordPuzzle.algorithm;

import java.util.ArrayList;
import crosswordPuzzle.core.*;


/**
 * Responsible for generating and validating possible placements for a given word
 * 
 *  - Generate candidate placements for a given word
 *  - Check validity of each placement against constraints
 *  - Sort valid placements by number of overlaps with existing grid letters
 */
public class placementEvaluator {
    
    /**
     * Systematically explores and tries every placement and orientation for a given word on the current grid
     * calls `IsValidPlacement` to validate whether candidate placement passes constraints
     * sorts list by most overlap count  
     * 
     * @param w - selected word to be checked for placements
     * @param grid - crossword grid
     * @return - list of candidate placements sorted by number of overlaps with existing grid letters 
     */
    static ArrayList<placedWord> generatePlacements(wordKeeper w, Grid grid) {        
        ArrayList<placedWord> candidates = new ArrayList<>();
        int rows = grid.getRows(), cols = grid.getCols();

        // iterate through each cell in grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // horizontal orientation
                placedWord wordH = new placedWord(w, r, c, false);
                if (isValidPlacement(wordH, grid)) candidates.add(wordH);

                // vertical orientation
                placedWord wordV = new placedWord(w, r, c, true);
                if (isValidPlacement(wordV, grid)) candidates.add(wordV);
            }
        }

        // sort candidates by most overlaps with existing grid letters
        candidates.sort((a, b) -> countOverlaps(b, grid) - countOverlaps(a, grid));
        return candidates;
    }

    /**
     * method that validates candidate placement through constraints
     * 
     * 
     * constraints:
     * - must have at least one overlap with the current grid
     * - rejects candidate placements where a cell in grid is occupied by a different letter
     * - must not have word adjacency (words toching side by side)
     * - boundaries from start to end must be empty (avoids words to be unintentionally combined)
     * 
     * @param pw - word object being checked for constraints
     * @param grid - current grid as reference
     * @return
     */
    static boolean isValidPlacement(placedWord pw, Grid grid) {
        int len = pw.word.letters.length;
        boolean hasOverlap = false;

        for (int i = 0; i < len; i++) {
            int r = pw.isVertical ? pw.row + i : pw.row;
            int c = pw.isVertical ? pw.col : pw.col + i;

            // if word placement length goes out of bounds, skip
            // prevents out of bounds error
            if (!grid.inBounds(r, c)) continue;

            char current = grid.getCell(r, c);
            char letter = pw.word.letters[i];

            // conflict: cell occupied by a different letter
            if (current != '-' && current != letter) return false;

            // overlap check
            if (current == letter) hasOverlap = true;

            // adjacency check
            if (current == '-' && grid.hasIllegalSideAdjacency(r, c, pw.isVertical)) return false;
        }

        // boundary cells before start and after end must be empty
        if (!grid.checkBoundary(pw)) return false;

        // word must overlap at least one letter
        if (!hasOverlap) return false;

        return true;
    }

    /**
     * Count how many letters of the placed word overlap with existing letters on the grid
     */
    static int countOverlaps(placedWord pw, Grid grid) {
        int overlaps = 0;
        for (int i = 0; i < pw.word.letters.length; i++) {
            int r = pw.isVertical ? pw.row + i : pw.row;
            int c = pw.isVertical ? pw.col : pw.col + i;

            // bounds guard
            if (r < 0 || r >= grid.getRows() || c < 0 || c >= grid.getCols()) continue;

            if (grid.getCell(r, c) == pw.word.letters[i]) overlaps++;
        }
        return overlaps;
    }
}
