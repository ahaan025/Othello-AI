package othello;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.util.ArrayList;

/**
 * This class manages the 8x8 Othello game board with a RED border.
 */
public class Board {

    private OthelloSquare[][] squares;
    private Pane boardPane;

    public Board() {
        this.boardPane = new Pane();
        this.boardPane.setPrefSize(
                (Constants.BOARD_SIZE + 2) * Constants.SQUARE_SIZE,
                (Constants.BOARD_SIZE + 2) * Constants.SQUARE_SIZE
        );

        this.drawBorder();

        this.squares = new OthelloSquare[Constants.BOARD_SIZE]
                [Constants.BOARD_SIZE];
        this.setupBoard();
    }
    public Board(Board original) {
        this.boardPane = null;

        this.squares = new OthelloSquare[Constants.BOARD_SIZE]
                [Constants.BOARD_SIZE];

        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                this.squares[row][col] = new OthelloSquare(row, col);

                if (!original.isEmpty(row, col)) {
                    int pieceColor = original.getPieceColor(row, col);
                    this.squares[row][col].setPieceColorLogical(pieceColor);
                }
            }
        }
    }

    /**
     * This method draws a grey border around the board by creating
     * rectangles along the edges.
     */
    private void drawBorder() {
        int totalSize = Constants.BOARD_SIZE + 2;

        for (int row = 0; row < totalSize; row++) {
            for (int col = 0; col < totalSize; col++) {
                boolean isBorder = (row == 0 || row == totalSize - 1 ||
                        col == 0 || col == totalSize - 1);
                if (isBorder) {
                    Rectangle borderSquare = new Rectangle(
                            col * Constants.SQUARE_SIZE,
                            row * Constants.SQUARE_SIZE,
                            Constants.SQUARE_SIZE,
                            Constants.SQUARE_SIZE
                    );
                    borderSquare.setFill(Constants.GREY_BORDER);
                    borderSquare.setStroke(Color.BLACK);
                    borderSquare.setStrokeWidth(1);
                    this.boardPane.getChildren().add(borderSquare);
                }
            }
        }
    }

    /**
     * This method creates all 64 OthelloSquare objects for the playable board.
     * Squares are offset by Constants.SQUARE_SIZE to account for the border.
     */
    private void setupBoard() {
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                OthelloSquare square = new OthelloSquare(row, col, this.boardPane);
                this.squares[row][col] = square;
            }
        }
    }

    /**
     * Helper method to add the first 4 initial pieces. Did not use constants due to ease of
     * setting up.
     */
    public void setupInitialPieces() {
        this.addPiece(3, 3, Constants.WHITE);
        this.addPiece(4, 4, Constants.WHITE);
        this.addPiece(3, 4, Constants.BLACK);
        this.addPiece(4, 3, Constants.BLACK);
    }

    /**
     * Method used across program to add a piece.
     */

    public void addPiece(int row, int col, int color) {
        if (row >= 0 && row < Constants.BOARD_SIZE &&
                col >= 0 && col < Constants.BOARD_SIZE) {
            this.squares[row][col].addPiece(color);
        }
    }

    /**
     * Checks if empty - works for both visual and copy boards.
     */
    public boolean isEmpty(int row, int col) {
        return this.squares[row][col].isEmpty();
    }

    /**
     * Gets piece color - works for both visual and copy boards.
     */
    public int getPieceColor(int row, int col) {
        return this.squares[row][col].getPieceColor();
    }

    /**
     * Method used to remove a piece from the grid - used with reset.
     */
    public void clear() {
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                this.squares[row][col].removePiece();
            }
        }
    }

    /**
     * Move validation - checks if placing a piece creates a sandwich
     */
    public boolean isValidMove(int row, int col, int color) {
        if (!this.isEmpty(row, col)) {
            return false;
        }
        int[][] directions = {
                {-1, 0}, {-1, 1}, {0, 1}, {1, 1},
                {1, 0}, {1, -1}, {0, -1}, {-1, -1}
        };
        for (int[] direction : directions) {
            int rowDir = direction[0];
            int colDir = direction[1];
            if (this.checkDirection(row + rowDir, col + colDir,
                    rowDir, colDir, color, false)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method used to check all directions and see the pieces, and their colour.
     * Used in board class to flip pieces.
     */
    private boolean checkDirection(int row, int col, int rowDir, int colDir,
                                   int color, boolean seenOpponent) {
        if (row < 0 || row >= Constants.BOARD_SIZE ||
                col < 0 || col >= Constants.BOARD_SIZE) {
            return false;
        }
        if (this.isEmpty(row, col)) {
            return false;
        }
        int pieceColor = this.getPieceColor(row, col);
        int opponentColor = (color == Constants.WHITE) ?
                Constants.BLACK : Constants.WHITE;
        if (pieceColor == opponentColor) {
            return this.checkDirection(row + rowDir, col + colDir,
                    rowDir, colDir, color, true);
        }
        if (pieceColor == color) {
            return seenOpponent;
        }
        return false;
    }

    /**
     * Used to enable clicks whenever it is a human players turn and game is not over.
     */
    public void enableClicks(Player currentPlayer) {
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                this.squares[row][col].setClickHandler(currentPlayer);
            }
        }
    }

    /**
     * Used to disable clicks when computers turn or game is over.
     */
    public void disableClicks() {
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                this.squares[row][col].removeClickHandler();
            }
        }
    }

    /**
     * Helper method to inspect all directions and flip the pieces accordingly.
     * Array in method and not constants as it's method-specific data, not a program-wide constant.
     */
    public void flipPieces(int row, int col, int color) {
        //All 8 directions: N, NE, E, SE, S, SW, W, NW
        int[][] directions = {
                {-1, 0},
                {-1, 1},
                {0, 1},
                {1, 1},
                {1, 0},
                {1, -1},
                {0, -1},
                {-1, -1}
        };
        //Check each direction and flip if sandwich exists
        for (int[] direction : directions) {
            int rowDir = direction[0];
            int colDir = direction[1];
            //Only flip if there's a valid sandwich in this direction
            if (this.checkDirection(row + rowDir,
                    col + colDir,
                    rowDir,
                    colDir,
                    color,
                    false)) {
                //Flip all pieces in this direction
                this.flipDirection(row + rowDir,
                        col + colDir,
                        rowDir,
                        colDir,
                        color);
            }
        }
    }

    /**
     * This recursive helper method flips pieces in one direction.
     * It starts at row, col and moves in the direction specified
     * by (rowDir, colDir), flipping opponent pieces until it reaches
     * a piece of the current player's color.
     */
    private void flipDirection(int row, int col, int rowDir, int colDir,
                               int color) {
        //Checking bounds
        if (row < 0 || row >= Constants.BOARD_SIZE ||
                col < 0 || col >= Constants.BOARD_SIZE) {
            return;
        }

        int pieceColor = this.getPieceColor(row, col);

        if (pieceColor == color) {
            return;
        }

        int opponentColor;

        if (color == Constants.WHITE) {
            opponentColor = Constants.BLACK;
        } else {
            opponentColor = Constants.WHITE;
        }

        if (pieceColor == opponentColor) {
            //Flip this piece to our color
            this.squares[row][col].addPiece(color);

            //Recursively flip the next piece in this direction
            this.flipDirection(row + rowDir,
                    col + colDir,
                    rowDir,
                    colDir,
                    color);
        }
    }
    /**
     * This method highlights all valid moves for the given color.
     * It iterates through every square on the board and highlights
     * those where a valid move can be made.
     */
    public void highlightValidMoves(int color) {
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                if (this.isValidMove(row, col, color)) {
                    this.squares[row][col].setHighlight(true);
                }
            }
        }
    }

    /**
     * This method clears all highlights from the board by resetting
     * all squares to their normal color.
     */
    public void clearHighlights() {
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                this.squares[row][col].setHighlight(false);
            }
        }
    }

    /**
     * Used to detect if a player has valid moves, used to forfeit players turn.
     */
    public boolean hasValidMoves(int color) {
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                if (this.isValidMove(row, col, color)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper method to check if board is full, used to detect end game.
     */
    public boolean isFull() {
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                //If find even one empty square, board is not full
                if (this.isEmpty(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method counts how many pieces of the given color are on
     * the board. Used for scoring and determining the winner.
     */
    public int countPieces(int color) {
        int count = 0;

        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                //Check if this square has a piece of the specified color
                if (!this.isEmpty(row, col) &&
                        this.getPieceColor(row, col) == color) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * This method evaluates the board from the given player's perspective by
     * calculating the difference between the weighted sum of the player's pieces
     * and the opponent's pieces. Uses strategic board weights where corners are
     * most valuable. Returns a positive value if the player has an advantage,
     * negative if the opponent has an advantage. Used by MiniMax algorithm.
     */
    public int evaluateBoard(int playerColor) {
        int playerScore = 0;
        int opponentScore = 0;

        //Find opponent color
        int opponentColor = (playerColor == Constants.WHITE) ?
                Constants.BLACK : Constants.WHITE;

        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                if (!this.isEmpty(row, col)) {
                    int pieceColor = this.getPieceColor(row, col);
                    int weight = Constants.BOARD_WEIGHTS[row][col];
                    if (pieceColor == playerColor) {
                        playerScore += weight;
                    } else if (pieceColor == opponentColor) {
                        opponentScore += weight;
                    }
                }
            }
        }
        return playerScore - opponentScore;
    }

    /**
     * Method to create a list of all valid moves possible. Used for MiniMax evaluation.
     */
    public ArrayList<Move> getAllValidMoves(int color) {
        ArrayList<Move> validMoves = new ArrayList<>();
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                if (this.isValidMove(row, col, color)) {
                    Move move = new Move(row, col, 0);
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    /**
     * Method Getter to return the pane
     */
    public Pane getPane() {
        return this.boardPane;
    }
}