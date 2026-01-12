package crosswordPuzzle.io;

import java.io.*;
import java.util.ArrayList;
import crosswordPuzzle.core.*;

/**
 * handles file input output operations for saving, loading, and listing crossword puzzles
 * generated grid, placedWords, and wordList objects are serialized to a .dat file for saving 
 * reversely, saved files can be deserialized to be loaded and used in the program (specifically for playing/solving) 
 */
public class puzzleManager {

    // Folder where puzzles are saved
    private static final String PUZZLE_FOLDER = "savedPuzzles";

    static {
        // Create folder if it doesn't exist
        File folder = new File(PUZZLE_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * Saves a puzzle (grid + placed words + word list) to a binary file
     */
    public static void savePuzzle(String title, char[][] grid, ArrayList<placedWord> placedWords, ArrayList<wordKeeper> wordList) {

        // constructs file path 
        String fileName = PUZZLE_FOLDER + File.separator + title.replaceAll("\\s+", "_") + ".dat";

        // ObjectOutputStream enables objects to be written to files
        // writes grid, PlacedWords object list, wordList objectList into the created file path
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(grid);
            oos.writeObject(placedWords);
            oos.writeObject(wordList);
            System.out.println("Puzzle saved successfully as " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving puzzle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads a puzzle from a file given its title
     * @return Object array: [0] -> char[][] grid, [1] -> ArrayList<placedWord>, [2] -> ArrayList<wordKeeper>
     */
    public static Object[] loadPuzzle(String title) {

        String fileName = PUZZLE_FOLDER + File.separator + title.replaceAll("\\s+", "_") + ".dat";

        // deserializes saved objects.  must be in order of how it was written/saved
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            char[][] solutionGrid = (char[][]) ois.readObject();
            ArrayList<placedWord> placedWordList = (ArrayList<placedWord>) ois.readObject();
            ArrayList<wordKeeper> wordKeeperList = (ArrayList<wordKeeper>) ois.readObject();

            return new Object[]{solutionGrid, placedWordList, wordKeeperList};
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading puzzle: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lists all saved puzzles in the puzzle folder
     */
    public static String[] listSavedPuzzles() {
        File folder = new File(PUZZLE_FOLDER);
        String[] files = folder.list((dir, name) -> name.endsWith(".dat"));
        if (files == null) return new String[0];
        for (int i = 0; i < files.length; i++) {
            files[i] = files[i].replaceAll("_", " ").replace(".dat", "");
        }
        return files;
    }
}
