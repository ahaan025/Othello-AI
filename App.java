package othello;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
  * This is the  main class where your Othello game will start.
  * The main method of this application calls the App constructor. You
  * will need to fill in the constructor to instantiate your game.
  *
  * Class comments here...
  *
  */

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new PaneOrganizer().getRoot(), 850, 600);
        stage.setScene(scene);
        stage.setTitle("Othello");
        stage.show();    }

    /*
    * Here is the mainline! No need to change this.
    */
    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}
