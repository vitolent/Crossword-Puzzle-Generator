import java.util.ArrayList;

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
     * method to select next word to place based on intersection score
     * 
     * @param unusedWords
     * @param grid
     * @return the wordKeeper object selected as the next word to place
     */ 
    public static wordKeeper selectNextWord(ArrayList<wordKeeper> unusedWords, Grid grid) {
        wordKeeper best = null;
        int bestScore = -1;

        // for every unplaced word, count how many of its letters have a match with the letters current grid
        // word with highest count gets selected and returned
        for (wordKeeper w : unusedWords) {
            int score = countGridIntersections(w, grid);

            // If scores tie, prefer the longer word
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
