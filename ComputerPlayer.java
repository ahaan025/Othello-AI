package othello;

import java.util.ArrayList;

/**
 * This class represents a computer player that uses the MiniMax algorithm
 * to play Othello at three intelligence levels. Level 1 looks one move ahead,
 * Level 2 looks two moves ahead, and Level 3 looks three moves ahead. The
 * algorithm recursively evaluates future board states to choose optimal moves.
 */
public class ComputerPlayer implements Player {

    private int color;
    private Board board;
    private Referee referee;
    private int intelligence;

    /**
     * The constructor creates a computer player with the specified color
     * and intelligence level
     */
    public ComputerPlayer(int color, Board board, int intelligence) {
        this.color = color;
        this.board = board;
        this.intelligence = intelligence;
    }

    /**
     * Sets the referee reference for two-way association. Called by
     * the Referee after player creation.
     */
    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    /**
     * Calculates and executes the best move using the MiniMax algorithm.
     * The row and col parameters are unused for ComputerPlayer
     */
    @Override
    public void makeMove(int row, int col) {
        Move bestMove = this.getBestMove(this.board, this.intelligence, this.color);

        if (bestMove != null) {
            int moveRow = bestMove.getRow();
            int moveCol = bestMove.getCol();

            this.board.addPiece(moveRow, moveCol, this.color);
            this.board.flipPieces(moveRow, moveCol, this.color);
        }

        if (this.referee != null) {
            this.referee.moveCompleted();
        }
    }

    /**
     * This recursive method implements the MiniMax algorithm. It evaluates
     * all possible moves to the specified intelligence depth, assuming the
     * opponent also plays optimally. Returns the Move with the highest value
     * for the current player. Base case at intelligence 1 directly evaluates
     * the board.
     */
    private Move getBestMove(Board board, int intelligence, int currentColor) {
        int opponentColor = (currentColor == Constants.WHITE) ?
                Constants.BLACK : Constants.WHITE;

        //Game over check
        if (board.isFull() ||
                (!board.hasValidMoves(currentColor) && !board.hasValidMoves(opponentColor))) {

            int currentScore = board.countPieces(currentColor);
            int opponentScore = board.countPieces(opponentColor);

            if (currentScore > opponentScore) {
                return new Move(Constants.INVALID_POSITION,
                        Constants.INVALID_POSITION,
                        Constants.MINIMAX_WIN_VALUE);
            } else if (currentScore < opponentScore) {
                return new Move(Constants.INVALID_POSITION,
                        Constants.INVALID_POSITION,
                        Constants.MINIMAX_LOSS_VALUE);
            } else {
                return new Move(Constants.INVALID_POSITION,
                        Constants.INVALID_POSITION,
                        Constants.MINIMAX_TIE_VALUE);
            }
        }

        ArrayList<Move> validMoves = board.getAllValidMoves(currentColor);

        //No valid moves - forfeit case
        if (validMoves.isEmpty()) {
            if (intelligence == 1) {
                return new Move(Constants.INVALID_POSITION,
                        Constants.INVALID_POSITION,
                        Constants.MINIMAX_FORFEIT_VALUE);
            } else {
                //Recursively get opponent's move and negate value
                Move opponentMove = this.getBestMove(board, intelligence - 1, opponentColor);

                if (opponentMove != null) {
                    opponentMove.setValue(-opponentMove.getValue());
                }
                return opponentMove;
            }
        }

        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        //Base case: intelligence = 1
        if (intelligence == 1) {
            for (Move move : validMoves) {
                Board testBoard = new Board(board);
                testBoard.addPiece(move.getRow(), move.getCol(), currentColor);
                testBoard.flipPieces(move.getRow(), move.getCol(), currentColor);

                int boardValue = testBoard.evaluateBoard(currentColor);
                move.setValue(boardValue);

                if (boardValue > bestValue) {
                    bestValue = boardValue;
                    bestMove = move;
                }
            }
            return bestMove;
        }

        //Recursive case: intelligence = 2/3
        for (Move move : validMoves) {
            Board testBoard = new Board(board);
            testBoard.addPiece(move.getRow(), move.getCol(), currentColor);
            testBoard.flipPieces(move.getRow(), move.getCol(), currentColor);

            Move opponentBestMove = this.getBestMove(testBoard,
                    intelligence - 1,
                    opponentColor);

            int moveValue;
            if (opponentBestMove != null) {
                moveValue = -opponentBestMove.getValue();
            } else {
                moveValue = Constants.MINIMAX_OPPONENT_FORFEIT_VALUE;
            }
            move.setValue(moveValue);

            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }
        return bestMove;
    }

    /**
     * Returns false since this is a computer player.
     */
    @Override
    public boolean isHuman() {
        return false;
    }

    /**
     * Returns the players color.
     */
    @Override
    public int getColor() {
        return this.color;
    }
}