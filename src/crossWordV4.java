import java.util.*;

public class crossWordV4 {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("Crossword Puzzle Generator V4");
        
        System.out.println("How many words?");
        System.out.print("> ");
        int numWords = sc.nextInt();
        sc.nextLine();

        // Array list to list all words in wordKeeper objects
        ArrayList<wordKeeper> allWords = new ArrayList<>();
        
        // Array list to keep track placedWord objects
        ArrayList<placedWord> placedWordList = new ArrayList<>();

        // Inputs words and stores them as wordKeeper objects  
        for (int i = 0; i < numWords; i++) {
            System.out.print("Enter Word #" + (i + 1) + ": ");
            String wordInput = sc.nextLine().toUpperCase();
            allWords.add(new wordKeeper(wordInput, i + 1));
        }
        sc.close();

        // Array list to keep track unused words
        ArrayList<wordKeeper> unusedWords = new ArrayList<>(allWords);

        // selects word to be the anchor
        wordKeeper anchor = anchorSelector.findAnchor(allWords);
        System.out.println();
        System.out.println("Anchor Word: " + anchor.word);

        // intitialize grid size either minimum of 15x15 or length of anchor word
        int size = Math.max(15, anchor.word.length());

        // initialize grid with determined size
        Grid grid = new Grid(size, size);

        // for anchor word, placed at center horizontally
        // PW = placed word, meaning selected to be placed on grid
        placedWord anchorPW = new placedWord(anchor, (grid.getRows() - anchor.word.length()) / 2, (grid.getCols() - anchor.word.length()) / 2, false);

        placedWordList.add(anchorPW);
        unusedWords.remove(anchor);
        grid.wordPlacer(anchorPW, placedWordList);
        
        // backtracking algorithm to place words
        boolean success = backtrack.placeWordsBacktracking(unusedWords, placedWordList, grid);
        
        System.out.println();
        if (success) {
            System.out.println("All words placed successfully");
            System.out.println("Final Grid Size: " + grid.getRows() + " x " + grid.getCols());
        } else {
            System.out.println("Failed to place all words");
            
        }

        for (placedWord pw : placedWordList) {
            System.out.println("Words Placed: " + pw.word.word);
        }

        grid.displayGrid();


    }
}
