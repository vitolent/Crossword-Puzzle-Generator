import java.io.Serializable;

/** 
 * Class to keep track of all words
 * stores the word, generated id, and char array of its letters
 */

public class wordKeeper implements Serializable{
    private static final long serialVersionUID = 1L;

    int id;
    String word;
    char[] letters;
    String clue;

    public wordKeeper(String word, int id, String clue) {
        this.word = word;
        this.id = id;
        this.letters = word.toCharArray();
        this.clue = clue;
        
    }

    public String getWord(){
        return word;
    }

    public String getClue() {
        return clue;
    }
}
