package othello;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

/**
 * This is the top-level graphics class. It creates and organizes
 * the main BorderPane layout by placing the game board in the center and the
 * control panel on the right. It also sets up keyboard shortcuts for common
 * game actions.
 */
public class PaneOrganizer {
    private BorderPane root;
    private Controls controls;

    /**
     * The constructor sets up the entire game layout.
     */
    public PaneOrganizer() {
        this.root = new BorderPane();
        SetupGame game = new SetupGame();
        this.root.setCenter(game.getBoard().getPane());
        this.controls = new Controls(game);
        this.root.setRight(this.controls.getPane());
        this.setupKeyboardShortcuts();
    }
    /**
     * This method sets up keyboard shortcuts for the game.
     */
    private void setupKeyboardShortcuts() {
        this.root.setOnKeyPressed((KeyEvent e) -> this.handleKeyPress(e));
        this.root.setFocusTraversable(true);
        this.root.requestFocus();
    }

    /**
     * This method handles keyboard input and triggers the appropriate
     * actions based on which key was pressed.
     */
    private void handleKeyPress(KeyEvent e) {
        KeyCode key = e.getCode();
        switch (key) {
            case R:
                this.controls.triggerReset();
                break;
            case Q:
                this.controls.triggerQuit();
                break;
            case A:
                this.controls.triggerApplySettings();
                break;
            default:
                break;
        }
        this.root.requestFocus();
    }
    /**
     * Returns the root BorderPane so the App class can add it to the scene.
     */
    public BorderPane getRoot() {
        return this.root;
    }
}