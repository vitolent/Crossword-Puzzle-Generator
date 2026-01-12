package crosswordPuzzle.algorithm;

import java.util.ArrayList;
import crosswordPuzzle.core.*;

/**
 * responsible for selecting the next word to place
 * 
 * selection strategy:
 *  - foreach unusedWord object, counts how many of its letters exist on the current grid
 *  - word with highest count gets selected
 *  - if tie, longer word preferred
 */

public class nextWordSelector {
    
    /**
     * method to select next word to place based on intersection potential with current grid state
     * 
     * @param unusedWords
     * @param grid
     * @return the wordKeeper object 
     */ 
    public static wordKeeper selectNextWord(ArrayList<wordKeeper> unusedWords, Grid grid) {
        wordKeeper best = null;
        int bestScore = -1;

        // evaluate each unused word and calculate intersection score
        for (wordKeeper w : unusedWords) {
            int score = countGridIntersections(w, grid);

            // updates best for every word that has a higher score
            // picks longer word if tie occurs
            if (score > bestScore || 
                (score == bestScore && (best == null || w.word.length() > best.word.length()))) {
                bestScore = score;
                best = w;
            }
        }
        return best;
    }

    /**
     * method to count letters in wordKeeper that exist on the grid
     * 
     * @param w - wordKeeper object
     * @param grid
     * @return - number of letters in w that exist on the grid
     */
    private static int countGridIntersections(wordKeeper w, Grid grid) {
        int count = 0;
        for (char ch : w.letters) {
            if (grid.containsLetter(ch)) {
                count++;
            }
        }
        return count;
    }
}
