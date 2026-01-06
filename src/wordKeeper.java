/** 
 * Class to keep track of all words
 * stores the word, generated id, and char array of its letters
 */

public class wordKeeper {
    int id;
    String word;
    char[] letters;

    public wordKeeper(String word, int id) {
        this.word = word;
        this.id = id;
        this.letters = word.toCharArray();
        
    }
}
