package crosswordPuzzle.core;

import java.io.Serializable;

public class placedWord implements Serializable{
    private static final long serialVersionUID = 1L;

    public wordKeeper word;
    public int row;
    public int col;
    public boolean isVertical;

    public placedWord(wordKeeper w, int r, int c, boolean m) {
        this.word = w;
        this.row = r;
        this.col = c;
        this.isVertical = m;
    }
}
