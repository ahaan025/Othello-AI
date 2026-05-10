package othello;

/**
 * This interface defines the common behavior for all player types in Othello.
 * Both HumanPlayer and ComputerPlayer implement this interface, enabling
 * polymorphism.
 */
public interface Player {

    /**
     * Makes a move at the specified row and column position.
     */
    void makeMove(int row, int col);

    /**
     * Used by the Referee to check valid moves, highlight
     * available moves, and update turn labels.
     */
    int getColor();

    /**
     * Sets the referee reference for this player.  This allows
     * players to notify the referee when their move is complete.
     */
    void setReferee(Referee referee);

    /**
     * Used by the Referee to determine whether to enable mouse clicks
     * or start the Timeline (for computer moves with visual delay).
     */
    boolean isHuman();
}