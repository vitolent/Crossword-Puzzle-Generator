public class placedWord {
    wordKeeper word;
    int row;
    int col;
    boolean isVertical;

    public placedWord(wordKeeper w, int r, int c, boolean m) {
        this.word = w;
        this.row = r;
        this.col = c;
        this.isVertical = m;
    }
}
