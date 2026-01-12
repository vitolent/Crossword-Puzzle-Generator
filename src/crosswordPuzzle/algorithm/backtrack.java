package crosswordPuzzle.algorithm;

import java.util.ArrayList;
import crosswordPuzzle.core.*;
/**
 * implements the backtracking algorithm to place words on the grid
 * 
 * algorithm:
 *  - calls nextWordSelector to select the next word to place
 *  - calls placementEvaluator to generate possible placements for the selected word
 *  - attempts each possible placement recursively
 *  - backtracks if a placement leads to a dead end
 */
public class backtrack {
    
    /** 
     * Recursively places words on the crossword grid
     * 
     * @param unusedWords - list of words yet to be placed
     * @param placedWordList - list of words already placed
     * @param grid - current state of the grid
     * @return true if all words are placed successfully, false otherwise
     */
    public static boolean placeWordsBacktracking (
        ArrayList<wordKeeper> unusedWords, 
        ArrayList<placedWord> placedWordList, 
        Grid grid ) {
            
            if (unusedWords.isEmpty()) {
                grid.trimGrid();
                grid.syncPlacedWords(placedWordList);
                return true;
            }

            // select next word to place
            wordKeeper nextWord = nextWordSelector.selectNextWord(unusedWords, grid);

            // generate list of possible placements for the selected word 
            ArrayList<placedWord> possiblePlacements = placementEvaluator.generatePlacements(nextWord, grid);
            
            // tries each possible placement for the current selected word
            for (placedWord pw : possiblePlacements) {
                grid.wordPlacer(pw, placedWordList);
                placedWordList.add(pw);
                unusedWords.remove(nextWord);

                // recursive call
                if (placeWordsBacktracking(unusedWords, placedWordList, grid)) return true;

                // removes word placement 
                grid.removeWord(pw, placedWordList);
                placedWordList.remove(pw);
                unusedWords.add(nextWord);
            }
            // signal failure to caller and trigger backtracking
            return false;

        }
}
