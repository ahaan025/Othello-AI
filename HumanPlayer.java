package othello;
/**
 * This class represents a human player in the Othello game. It implements
 * the Player interface and handles moves made by mouse clicks. When a square
 * is clicked, it validates the move, updates the board by placing a piece
 * and flipping sandwiched opponent pieces, then notifies the referee.
 */
public class HumanPlayer implements Player {
    private int color;
    private Board board;
    private Referee referee;
    /**
     * The constructor creates a human player with the specified color.
     * It stores a reference to the board for move validation and execution.
     */
    public HumanPlayer(int color, Board board) {
        this.color = color;
        this.board = board;
    }
    /**
     * This method sets the referee reference after the player is created.
     * Called by the Referee to establish two-way association.
     */
    @Override
    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    /**
     * This method is called when the player clicks on a square. It first
     * validates the move to ensure it's legal according to Othello rules.
     * If valid, it places the piece on the board, flips all sandwiched
     * opponent pieces, and notifies the referee that the move is complete
     * so turns can be switched. If invalid, the method returns silently
     * and the player can click again.
     */
    @Override
    public void makeMove(int row, int col) {
        if (!this.board.isValidMove(row, col, this.color)) {
            return;
        }
        this.board.addPiece(row, col, this.color);
        this.board.flipPieces(row, col, this.color);
        if (this.referee != null) {
            this.referee.moveCompleted();
        }
    }
    /**
     * Returns true since this is a human player. Used by the Referee
     * to determine whether to enable mouse clicks or use the Timeline.
     */
    @Override
    public boolean isHuman() {
        return true;
    }
    /**
     * Returns the player's color (Constants.WHITE or Constants.BLACK).
     * Used by the Referee to check valid moves and update labels.
     */
    @Override
    public int getColor() {
        return this.color;
    }
}