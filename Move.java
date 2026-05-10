package othello;

/**
 * This class represents a potential move in Othello. It stores the
 * row and column of the move, as well as its strategic value as
 * determined by the board evaluation function. Used by MiniMax
 * algorithm to compare and return the best move.
 */
public class Move {

    private int row;
    private int col;
    private int value;

    /**
     * Constructor creates a move with specified position and value.
     */
    public Move(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    /**
     * Returns the row of this move.
     */
    public int getRow() {
        return this.row;
    }
    /**
     * Returns the column of this move.
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Returns the strategic value of this move.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Sets the value of this move. Used when updating move values
     * during MiniMax recursion.
     */
    public void setValue(int value) {
        this.value = value;
    }
}
