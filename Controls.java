package othello;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/*
 * Controls sets up the GUI for the game menu, allowing the user to pick the
 * game modes and to start and track games. Controls holds a one-way reference
 * to the Game, so it can control the Game's player settings.
 */
public class Controls {

  private SetupGame game;

  private VBox controlsPane;

  // Arrays for player buttons. Each button is checked to see if it is
  // selected when the user starts each game.
  private RadioButton[][] playerButtons;

  /**
   * Constructor creates the game controls on the side
   * @param othello
   */
  public Controls(SetupGame othello) {
    this.game = othello;

    this.controlsPane = new VBox();
    this.controlsPane.setPadding(new Insets(10));
    this.controlsPane.setSpacing(30);
    this.controlsPane.setAlignment(Pos.CENTER);
    this.controlsPane.setPrefWidth(Constants.CONTROLS_PANE_WIDTH);

    this.setupInstructions();

    this.setupStatusLabels();

    this.setupMenu();
    this.setupGameButtons();
  }

  /**
   * This method adds the two extra labels for updating the score and the status label.
   */
  private void setupStatusLabels() {
    VBox statusBox = new VBox();
    statusBox.setAlignment(Pos.CENTER);
    statusBox.setSpacing(10);

    statusBox.getChildren().addAll(
            this.game.getScoreLabel(),
            this.game.getStatusLabel()
    );

    this.controlsPane.getChildren().add(statusBox);
  }

  /**
   * Gets the pane.
   * @return
   */
  public Pane getPane() {
    return this.controlsPane;
  }

  /**
   * Method to setup label which shows instructions for the game.
   */

  private void setupInstructions() {
    Label instructionsLabel = new Label(
            "Select options, then press Apply Settings");
    this.controlsPane.getChildren().add(instructionsLabel);
  }

  /*
   * Sets up the two halves of the player mode menu.
   */
  private void setupMenu() {
    this.playerButtons = new RadioButton[2][4];

    HBox playersMenu = new HBox();
    playersMenu.setSpacing(10);
    playersMenu.setAlignment(Pos.CENTER);
    playersMenu.getChildren().addAll(this.playerMenu(Constants.WHITE),
            this.playerMenu(Constants.BLACK));

    this.controlsPane.getChildren().add(playersMenu);
  }

  /**
   * Helper method to create the player Menu.
   */

  private VBox playerMenu(int player) {
    VBox playerMenu = new VBox();
    playerMenu.setPrefWidth(Constants.CONTROLS_PANE_WIDTH / 2);
    playerMenu.setSpacing(10);
    playerMenu.setAlignment(Pos.CENTER);

    //Player color.
    String playerColor = "White";
    if (player == Constants.BLACK) {
      playerColor = "Black";
    }
    Label playerName = new Label(playerColor);

    ToggleGroup toggleGroup = new ToggleGroup();

    //Human player.
    RadioButton humanButton = new RadioButton("Human         ");
    humanButton.setToggleGroup(toggleGroup);
    humanButton.setSelected(true);
    this.playerButtons[player][0] = humanButton;

    //Computer Players.
    for (int i = 1; i < 4; i++) {
      RadioButton computerButton = new RadioButton("Computer " + i + "  ");
      computerButton.setToggleGroup(toggleGroup);
      this.playerButtons[player][i] = computerButton;


    }
    playerMenu.getChildren().add(playerName);
    for (RadioButton rb : this.playerButtons[player]) {
      playerMenu.getChildren().add(rb);
    }

    return playerMenu;
  }

  /**
   * Helper method to create the 3 buttons.
   */
  private void setupGameButtons() {
    Button applySettingsButton = new Button("Apply Settings");
    applySettingsButton.setOnAction((ActionEvent e)->this.applySettings());
    applySettingsButton.setFocusTraversable(false);

    Button resetButton = new Button("Reset");
    resetButton.setOnAction((ActionEvent e)-> this.resetHandler());
    resetButton.setFocusTraversable(false);

    Button quitButton = new Button("Quit");
    quitButton.setOnAction((ActionEvent e)->Platform.exit());
    quitButton.setFocusTraversable(false);

    this.controlsPane.getChildren().addAll(applySettingsButton, resetButton,
            quitButton);
  }

  /**
   * Method to set functionality for apply settigns - calls for players to be created.
   */
  public void applySettings() {

    int whitePlayerMode = 0;
    int blackPlayerMode = 0;
    for (int player = 0; player < 2; player++) {
      for (int mode = 0; mode < 4; mode++) {
        if (this.playerButtons[player][mode].isSelected()) {
          if (player == Constants.WHITE) {
            whitePlayerMode = mode;
          } else {
            blackPlayerMode = mode;
          }
        }
      }
    }
    this.game.setPlayers(whitePlayerMode, blackPlayerMode);
  }


  public void resetHandler(){
    this.game.reset();
  }

  /**
   * The following methods are public helper methods used for adding keyboard functionality
   * Designed for better encapsulation instead of giving PaneOrganizer full access to
   * Controls.
   */
  public void triggerReset() {
    this.game.reset();
  }

  public void triggerQuit() {
    Platform.exit();
  }

  public void triggerApplySettings() {
    this.applySettings();
  }

}