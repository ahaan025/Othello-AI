package othello;

import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * This class acts as the referee for the game. It manages
 * turn-taking, handles forfeits, detects game over,
 * and updates the labels.
 */
public class Referee {

    private Player player1;
    private Player player2;
    private Timeline timeline;

    private Player currentPlayer;
    private Board board;
    private boolean isGameOver;
    private boolean gameStopped;

    private Label scoreLabel;
    private Label statusLabel;

    /**
     * Constructor is used to initialize all parameters seen below and start the keyframes.
     */

    public Referee(Player player1, Player player2, Board board,
                   Label scoreLabel, Label statusLabel) {

        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
        this.currentPlayer = player1;
        this.isGameOver = false;
        this.gameStopped = false;

        this.scoreLabel = scoreLabel;
        this.statusLabel = statusLabel;

        player1.setReferee(this);
        player2.setReferee(this);

        KeyFrame kf = new KeyFrame(
                Duration.millis(Constants.COMPUTER_MOVE_DELAY),
                e -> this.handleComputerMove()
        );
        this.timeline = new Timeline(kf);
        this.timeline.setCycleCount(1);
    }

    /**
     * This method starts the game by updating labels, highlighting
     * valid moves, and triggering the appropriate player action.
     */
    public void startGame() {
        this.gameStopped = false;

        this.updateScoreLabel();
        this.statusLabel.setText("");
        this.updateStatusLabel();


        if (!this.board.hasValidMoves(this.currentPlayer.getColor())) {
            if (this.currentPlayer == this.player1) {
                this.currentPlayer = this.player2;
            } else {
                this.currentPlayer = this.player1;
            }

            this.updateStatusLabel();

            if (!this.board.hasValidMoves(this.currentPlayer.getColor())) {
                this.endGame();
                return;
            }
        }
        if (this.isComputerPlayer(this.currentPlayer)) {
            //Computer player logic
            this.board.clearHighlights();
            this.board.disableClicks();
            this.timeline.play();
        } else {
            //Human player logic
            this.board.highlightValidMoves(this.currentPlayer.getColor());
            this.board.enableClicks(this.currentPlayer);
        }

    }

    /**
     * Stops the game completely. Called when Reset or Apply Settings
     * is pressed during an active game.
     */
    public void stopGame() {
        this.gameStopped = true;
        this.isGameOver = true;
        this.board.disableClicks();
        this.board.clearHighlights();
        if (this.timeline != null) {
            this.timeline.stop();
        }
    }

    /**
     * This method is called by a player when they complete their move.
     * It updates scores, clears highlights, switches players, checks for
     * forfeits, and checks if the game is over.
     */
    public void moveCompleted() {
        if (this.isGameOver || this.gameStopped) {
            return;
        }
        this.updateScoreLabel();
        this.board.clearHighlights();
        if (this.checkGameOver()) {
            return;
        }
        if (this.currentPlayer == this.player1) {
            this.currentPlayer = this.player2;
        } else {
            this.currentPlayer = this.player1;
        }
        this.updateStatusLabel();
        if (!this.board.hasValidMoves(this.currentPlayer.getColor())) {

            if (this.currentPlayer == this.player1) {
                this.currentPlayer = this.player2;
            } else {
                this.currentPlayer = this.player1;
            }

            this.updateStatusLabel();

            if (!this.board.hasValidMoves(this.currentPlayer.getColor())) {
                this.endGame();
                return;
            }
        }
        if (this.isComputerPlayer(this.currentPlayer)) {
            this.board.clearHighlights();
            this.board.disableClicks();
            this.timeline.play();
        } else {
            this.board.highlightValidMoves(this.currentPlayer.getColor());
            this.board.enableClicks(this.currentPlayer);
        }

    }

    /**
     * This method updates the score label with current piece counts.
     */
    private void updateScoreLabel() {
        int whiteCount = this.board.countPieces(Constants.WHITE);
        int blackCount = this.board.countPieces(Constants.BLACK);

        this.scoreLabel.setText("White: " + whiteCount +
                " Black: " + blackCount);
    }

    /**
     * This method updates the turn label to show whose turn it is.
     */
    private void updateStatusLabel() {
        if (this.currentPlayer.getColor() == Constants.WHITE) {
            this.statusLabel.setText("White to Move");
        } else {
            this.statusLabel.setText("Black to Move");
        }
    }


    /**
     * This method checks if the game is over by checking if the
     * board is completely full. Returns true if game is over.
     */
    private boolean checkGameOver() {
        if (this.board.isFull()) {
            this.endGame();
            return true;
        }
        return false;
    }

    /**
     * This method handles the end of the game. It disables clicks,
     * clears highlights, counts pieces, determines the winner, and
     * displays the result on screen.
     */
    private void endGame() {
        this.isGameOver = true;
        this.board.disableClicks();
        this.board.clearHighlights();

        int whiteCount = this.board.countPieces(Constants.WHITE);
        int blackCount = this.board.countPieces(Constants.BLACK);

        this.updateScoreLabel();

        // Set the status label to show game over and winner
        if (whiteCount > blackCount) {
            this.statusLabel.setText("GAME OVER - WHITE WINS!");
        } else if (blackCount > whiteCount) {
            this.statusLabel.setText("GAME OVER - BLACK WINS!");
        } else {
            this.statusLabel.setText("GAME OVER - TIE GAME!");
        }
    }

    /**
     * Checks if the given player is a human player.
     */
    private boolean isComputerPlayer(Player player) {
        return !player.isHuman();
    }

    /**
     * Public Getter to see who current player is.
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Sets the current player based on color (used when continuing game
     * with new players after Apply Settings)
     */
    public void setCurrentPlayer(int color) {
        if (color == this.player1.getColor()) {
            this.currentPlayer = this.player1;
        } else {
            this.currentPlayer = this.player2;
        }
    }

    /**
     * Stops timeline for delay and then handles the move by calling makeMove - parameters do not
     * matter for computer moves.
     */
    private void handleComputerMove() {
        this.timeline.stop();
        if (this.isGameOver || this.gameStopped) {
            return;
        }
        this.currentPlayer.makeMove(0, 0);
    }

    /**
     * returns boolean if game is over.
     */
    public boolean getIsGameOver() {
        return this.isGameOver;
    }
}