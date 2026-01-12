package crosswordPuzzle.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import crosswordPuzzle.core.*;

public class cluePrinter {

    public static void printClues(ArrayList<placedWord> placedWordList) {
        ArrayList<placedWord> across = new ArrayList<>();
        ArrayList<placedWord> down = new ArrayList<>();

        for (placedWord pw : placedWordList) {
            if (pw.isVertical) down.add(pw);
            else across.add(pw);
        }

        // Sort by row, then col
        Comparator<placedWord> byPosition = Comparator
                .comparingInt((placedWord pw) -> pw.row)
                .thenComparingInt(pw -> pw.col);

        Collections.sort(across, byPosition);
        Collections.sort(down, byPosition);

        System.out.println("=== ACROSS ===");
        for (placedWord pw : across) {
            System.out.printf("%2d. | %s\n", pw.word.id, pw.word.getClue());
        }

        System.out.println("\n=== DOWN ===");
        for (placedWord pw : down) {
            System.out.printf("%2d. | %s\n", pw.word.id, pw.word.getClue());
        }
    }
}
