package othello;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

/**
 * OthelloSquare class - represents one square on the board.
 * This is a "smart square" that can contain a piece.
 * Supports both visual boards (with JavaFX) and logical-only boards
 * (for MiniMax algorithm with no visual components).
 */
public class OthelloSquare {

    private Rectangle rectangle;
    private Circle piece;
    private int row;
    private int col;
    private Pane pane;
    private int logicalPieceColor; //-1 = empty, 0 = WHITE, 1 = BLACK

    /**
     * VISUAL CONSTRUCTOR - Creates a square with visual components.
     * Used for the main game board that players see.
     */
    public OthelloSquare(int row, int col, Pane pane) {
        this.row = row;
        this.col = col;
        this.pane = pane;
        this.logicalPieceColor = -1;
        this.rectangle = new Rectangle(
                (col + 1) * Constants.SQUARE_SIZE,
                (row + 1) * Constants.SQUARE_SIZE,
                Constants.SQUARE_SIZE,
                Constants.SQUARE_SIZE
        );
        this.rectangle.setFill(Constants.BOARD_COLOR);
        this.rectangle.setStroke(Constants.BORDER_COLOR);
        this.rectangle.setStrokeWidth(1);
        this.pane.getChildren().add(this.rectangle);
    }

    /**
     * LOGICAL-ONLY CONSTRUCTOR - Creates a square with NO visual components.
     * Used for copy boards in MiniMax algorithm.
     */
    public OthelloSquare(int row, int col) {
        this.row = row;
        this.col = col;
        this.pane = null;
        this.rectangle = null;
        this.piece = null;
        this.logicalPieceColor = -1;
    }

    /**
     * Adds a piece to this square. Works for both visual and logical boards.
     * For visual boards, creates a Circle. For logical boards, only updates state.
     */
    public void addPiece(int color) {
        if (this.pane == null) {
            this.logicalPieceColor = color;
        }else {
            this.removePiece();
            double centerX = (this.col + 1) * Constants.SQUARE_SIZE +
                    Constants.SQUARE_SIZE / 2;
            double centerY = (this.row + 1) * Constants.SQUARE_SIZE +
                    Constants.SQUARE_SIZE / 2;

            this.piece = new Circle(centerX, centerY, Constants.PIECE_RADIUS);

            if (color == Constants.WHITE) {
                this.piece.setFill(Constants.WHITE_COLOR);
            } else {
                this.piece.setFill(Constants.BLACK_COLOR);
            }

            this.pane.getChildren().add(this.piece);
            this.logicalPieceColor = color;
        }
    }

    /**
     * Removes the piece from this square.
     * Works for both visual and logical boards.
     */
    public void removePiece() {
        if (this.piece != null && this.pane != null) {
            this.pane.getChildren().remove(this.piece);
            this.piece = null;
        }
        this.logicalPieceColor = -1;
    }

    /**
     * Checks if this square is empty. Works for both visual and logical boards.
     * For logical boards, checks the logical color. For visual boards, checks
     * if the piece exists.
     */
    public boolean isEmpty() {
        if (this.pane == null) {
            return this.logicalPieceColor == -1;
        }
        return this.piece == null;
    }

    /**
     * Gets the color of the piece. Works for both visual and logical boards.
     * For logical boards, returns the stored color. For visual boards,
     * determines color from the Circle's fill.
     */
    public int getPieceColor() {
        if (this.pane == null) {
            return this.logicalPieceColor;
        }

        if (this.piece == null) {
            return -1;
        }
        Color fill = (Color) this.piece.getFill();
        if (fill.equals(Constants.WHITE_COLOR)) {
            return Constants.WHITE;
        } else {
            return Constants.BLACK;
        }
    }

    /**
     * Sets the logical piece color without visual updates.
     * Used when copying boards for MiniMax.
     */
    public void setPieceColorLogical(int color) {
        this.logicalPieceColor = color;
    }

    /**
     * Sets up click handling for this square
     * When clicked, calls the player's makeMove method with this square's position.
     */
    public void setClickHandler(Player player) {
        if (this.rectangle != null) {
            this.rectangle.setOnMouseClicked(
                    e -> player.makeMove(this.row, this.col));
        }
    }

    /**
     * Removes click handling
     * Used when it's the computer's turn or when game is over.
     */
    public void removeClickHandler() {
        if (this.rectangle != null) {
            this.rectangle.setOnMouseClicked(null);
        }
    }
    /**
     * Sets or removes highlight on this square
     * Highlights indicate valid moves for human players.
     */
    public void setHighlight(boolean highlighted) {
        if (this.rectangle != null) {
            if (highlighted) {
                this.rectangle.setFill(Constants.HIGHLIGHT_COLOR);
            } else {
                this.rectangle.setFill(Constants.BOARD_COLOR);
            }
        }
    }
}