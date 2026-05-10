package othello;

import javafx.scene.paint.Color;

/**
 * Constants class - stores game constants for the whole program
 */
public class Constants {
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    public static final int CONTROLS_PANE_WIDTH = 250;
    public static final Color GREY_BORDER = Color.RED;
    public static final Color HIGHLIGHT_COLOR = Color.LIGHTGREY;
    public static final int BOARD_SIZE = 8;
    public static final int SQUARE_SIZE = 60;
    public static final int MINIMAX_WIN_VALUE = 10000;
    public static final int MINIMAX_LOSS_VALUE = -10000;
    public static final int MINIMAX_FORFEIT_VALUE = -5000;
    public static final int MINIMAX_OPPONENT_FORFEIT_VALUE = 5000;
    public static final int MINIMAX_TIE_VALUE = 0;

    // Invalid move sentinel values
    public static final int INVALID_POSITION = -1;

    public static final int[][] BOARD_WEIGHTS = {
            {200, -70, 30, 25, 25, 30, -70, 200},
            {-70, -100, -10, -10, -10, -10, -100, -70},
            {30, -10, 2, 2, 2, 2, -10, 30},
            {25, -10, 2, 2, 2, 2, -10, 25},
            {25, -10, 2, 2, 2, 2, -10, 25},
            {30, -10, 2, 2, 2, 2, -10, 30},
            {-70, -100, -10, -10, -10, -10, -100, -70},
            {200, -70, 30, 25, 25, 30, -70, 200}
    };

    public static final int COMPUTER_MOVE_DELAY = 200;

    public static final Color WHITE_COLOR = Color.WHITE;
    public static final Color BLACK_COLOR = Color.BLACK;
    public static final Color BOARD_COLOR = Color.GREEN;
    public static final Color BORDER_COLOR = Color.DARKGREEN;
    public static final int PIECE_RADIUS = 25;
}
