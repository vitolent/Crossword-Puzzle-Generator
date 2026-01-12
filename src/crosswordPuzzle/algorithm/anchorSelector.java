package crosswordPuzzle.algorithm;

import java.util.*;
import crosswordPuzzle.core.*;

/**
 * responsible for choosing the anchor word
 * 
 * selection strategy:
 *  - for every word, counts the frequency of each letter in all words
 *  - scores each word by summing the frequencies of its letters
 *  - highest score = anchor word
 *  - if tie, longer word preferred
 */

public class anchorSelector {

    /**
     * method to find anchor word from list of wordKeeper objects
     * 
     * @param listOfW - lisf of wordKeeper objects
     * @return the wordKeeper object selected as anchor word
     */
    static wordKeeper findAnchor(ArrayList<wordKeeper> listOfW) {
        // Create a frequency map to count occurrences of each letter
        // Key: Character, Value: Integer (Frequency)
        Map<Character, Integer> letterFrequency = new HashMap<>();

        // Count letter frequencies across all words
        for (wordKeeper w : listOfW) {
            for (char ch : w.letters) {
                letterFrequency.put(ch, letterFrequency.getOrDefault(ch, 0) + 1);
                // getOrDefault searches for a letter (ch) in the map and returns its accompanying value
                // if a letter is not found, it returns a default value which is 0 in this case
                // + 1 always to count occurences of each char 
            }
        }

        wordKeeper best = null;
        int bestScore = -1;


        for (wordKeeper w : listOfW) {
            int score = 0;

            // Tallies all letter frequences fo each word
            for (char ch : w.letters) {
                score += letterFrequency.get(ch);
            }

            // checks and updates wordKeeper best with the word with higher score
            // if tie, selects longer word
            if (score > bestScore || (score == bestScore && w.word.length() > best.word.length())) {
                best = w;
                bestScore = score;
            }
        }
        return best;

    }
}
