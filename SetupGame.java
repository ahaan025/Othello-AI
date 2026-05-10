package othello;

import javafx.scene.control.Label;

/**
 * SetupGame class - top-level logic class that manages game initialization.
 */
public class SetupGame {

    private Board board;
    private Referee referee;
    private Player whitePlayer;
    private Player blackPlayer;
    private Label statusLabel;
    private Label scoreLabel;
    private boolean gameInProgress;

    /**
     * Constructor creates the board, labels, and sets up initial pieces.
     */
    public SetupGame() {
        this.board = new Board();
        this.scoreLabel = new Label("White: 2 Black: 2");
        this.statusLabel = new Label("");
        this.gameInProgress = false;
        this.board.setupInitialPieces();
        this.board.disableClicks();
    }

    /**
     * Returns the status label (shows turn or game over message).
     */
    public Label getStatusLabel() {
        return this.statusLabel;
    }

    /**
     * Returns the score label so Controls can add it to the pane.
     */
    public Label getScoreLabel() {
        return this.scoreLabel;
    }

    /**
     * Called by Controls when Apply Settings is pressed.
     * Creates players based on modes and starts the game.
     */
    public void setPlayers(int whiteMode, int blackMode) {
        int previousPlayerColor = -1;
        boolean wasGameOver = false;

        if (this.gameInProgress && this.referee != null) {
            wasGameOver = this.referee.getIsGameOver();
            if (wasGameOver) {
                this.board.clear();
                this.board.setupInitialPieces();
            } else {
                previousPlayerColor = this.referee.getCurrentPlayer().getColor();
            }

            this.referee.stopGame();
        } else {
            this.board.clear();
            this.board.setupInitialPieces();
        }
        this.board.clearHighlights();

        //Create white player
        if (whiteMode == 0) {
            this.whitePlayer = new HumanPlayer(Constants.WHITE, this.board);
        } else {
            this.whitePlayer = new ComputerPlayer(Constants.WHITE, this.board, whiteMode);
        }

        //Create black player
        if (blackMode == 0) {
            this.blackPlayer = new HumanPlayer(Constants.BLACK, this.board);
        } else {
            this.blackPlayer = new ComputerPlayer(Constants.BLACK, this.board, blackMode);
        }

        //Create another referee with new players
        this.referee = new Referee(this.whitePlayer,
                this.blackPlayer,
                this.board,
                this.scoreLabel,
                this.statusLabel);

        //If game wasnt over and we had a previous color then just restore that turn
        if (!wasGameOver && previousPlayerColor != -1) {
            this.referee.setCurrentPlayer(previousPlayerColor);
        }

        this.gameInProgress = true;
        this.referee.startGame();
    }

    /**
     * Resets the game to initial state and STOPS the game.
     * User must press Apply Settings to start a new game.
     */
    public void reset() {
        if (this.referee != null && this.gameInProgress) {
            this.referee.stopGame();
        }
        this.gameInProgress = false;
        this.board.clear();
        this.board.clearHighlights();
        this.board.disableClicks();
        this.board.setupInitialPieces();

        this.scoreLabel.setText("White: 2 Black: 2");
        this.statusLabel.setText("");
        this.referee = null;
        this.whitePlayer = null;
        this.blackPlayer = null;
    }

    /**
     * Returns the Board so PaneOrganizer can add it to the scene.
     */
    public Board getBoard() {
        return this.board;
    }
}