import java.util.ArrayList;
import java.util.Scanner;

/**
 * starts the crossword puzzle creation workflow.
 * 
 * What it does:
 * Asks for user input for number of words, words with corresponding clues, and title
 * Selects the anchor word 
 * Initiates the backtracking algorithm
 */
public class generateNewPuzzle {

    // static should come **before** void
    public static void generatePuzzle() {
        Scanner sc = new Scanner(System.in);

        int numWords;
        // ensure input is a valid number
        while (true) {
            System.out.println("How many words?");
            System.out.print("> ");
            try {
                numWords = Integer.parseInt(sc.nextLine().trim());
                if (numWords > 0) {
                    break;
                } else {
                    System.out.println("Invalid Input. Enter a number greater than 0\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input. Enter a valid integer\n");
            }
        }

        // Array list to store all words inputted
        ArrayList<wordKeeper> allWords = new ArrayList<>();
        // Array list to keep track of placed words in grid
        ArrayList<placedWord> placedWordList = new ArrayList<>();

        // Inputs words and clues; stores them as wordKeeper objects
        for (int i = 0; i < numWords; i++) {
            System.out.print("Enter Word #" + (i + 1) + ": ");
            String wordInput = sc.nextLine().toUpperCase();
            System.out.print("Enter a clue: ");
            String clueInput = sc.nextLine();
            allWords.add(new wordKeeper(wordInput, i + 1, clueInput));
        }

        System.out.print("Enter a Title for your puzzle > ");
        String title = sc.nextLine().trim();
        while (title.isEmpty()) {
            System.out.print("Title cannot be empty. Enter a title > ");
            title = sc.nextLine().trim();
        }

        // Array list to keep track of unused words
        ArrayList<wordKeeper> unusedWords = new ArrayList<>(allWords);

        // Selects word to be the anchor
        wordKeeper anchor = anchorSelector.findAnchor(allWords);
        System.out.println("\nAnchor Word: " + anchor.word);

        // Determines grid size either minimum of 15x15 or length of anchor word
        int size = Math.max(15, anchor.word.length());

        // Initialize grid with determined size
        Grid grid = new Grid(size, size);

        // For anchor word, placed at center horizontally
        placedWord anchorPW = new placedWord(anchor,
                (grid.getRows() - anchor.word.length()) / 2,
                (grid.getCols() - anchor.word.length()) / 2,
                false);

        placedWordList.add(anchorPW);
        unusedWords.remove(anchor);
        grid.wordPlacer(anchorPW, placedWordList);

        // Backtracking algorithm to place words
        boolean success = backtrack.placeWordsBacktracking(unusedWords, placedWordList, grid);

        System.out.println();
        if (success) {
            System.out.println("All words placed successfully");
            System.out.println("Final Grid Size: " + grid.getRows() + " x " + grid.getCols());
            puzzleManager.savePuzzle(title, grid.getBoard(), placedWordList, allWords);
        } else {
            System.out.println("Failed to place all words");
            System.out.println();
            System.out.println("Make sure your words have identical letters and try again");
        }

        // Print placed words
        for (placedWord pw : placedWordList) {
            System.out.println("Word Placed: " + pw.word.word);
            System.out.println("Clue: " + pw.word.getClue());
            System.out.println("ID: " + pw.word.id);
        }
        
        grid.displayGrid();
    }
}
