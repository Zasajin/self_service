package org.example;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.buttons.ArrowButton;
import org.example.buttons.BlackButtonWithImage;
import org.example.buttons.CartSquareButton;
import org.example.buttons.CircleButtonWithSign;
import org.example.buttons.ColorBtnOutlineImage;
import org.example.buttons.ColorButtonWithImage;
import org.example.buttons.ColorSettingManager;
import org.example.buttons.ColorSquareButtonWithImage;
import org.example.buttons.ConfirmOrderButton;
import org.example.buttons.KeyboardButton;
import org.example.buttons.KeyboardButtonPrim;
import org.example.buttons.MidLabelSec;
import org.example.buttons.TitleLabel;
import org.example.kiosk.InactivityTimer;
import org.example.kiosk.LabelManager;
import org.example.screens.BackgroundColorStore;
import org.example.screens.CustomScene;
import org.example.screens.WelcomeScreen;

/**
 * The main app class.
 */
public class App extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    // Window dimensions
    double windowWidth = 1920;
    double windowHeight = 1080;

    // Instantiates List to load color scheme
    Color[] savedColors = null;

    try {

      // Loads colors
      savedColors = ColorSettingManager.loadColors();

    } catch (IOException e) {

      e.printStackTrace();

    }

    // Sets loaded color scheme
    if (savedColors != null) {

      LabelManager.setTextColor(savedColors[0]);
      TitleLabel.setTextColor(savedColors[0]);
      BlackButtonWithImage.setButtonBackgroundColor(savedColors[0]);
      ColorSquareButtonWithImage.setButtonColor(savedColors[0]);
      CartSquareButton.setButtonColor(savedColors[0]);
      ColorBtnOutlineImage.setButtonColor(savedColors[0]);
      ArrowButton.setButtonColor(savedColors[0]);
      ConfirmOrderButton.setButtonBackgroundColor(savedColors[0]);
      CircleButtonWithSign.setPlusColor(savedColors[0]);
      CircleButtonWithSign.setMinusBorder(savedColors[0]);

      ColorButtonWithImage.setButtonBackgroundColor(savedColors[1]);
      CircleButtonWithSign.setMinusBackground(savedColors[1]);
      BackgroundColorStore.setCurrentBackgroundColor(savedColors[2]);

      KeyboardButtonPrim.setButtonBackgroundColor(savedColors[1]);
      KeyboardButton.setButtonBackgroundColor(savedColors[0]);
      MidLabelSec.setTextColor(savedColors[1]);
    }

    // Create the WelcomeScreen object and get the scene
    WelcomeScreen welcomeScreen = new WelcomeScreen();
    CustomScene welcomeScene = welcomeScreen.createWelcomeScreen(
        primaryStage,
        windowWidth,
        windowHeight);

    // Get welcome scene to stop timer every time on this scene and for later use
    InactivityTimer.getInstance().setWelcomeScene(welcomeScene);

    // Force fullscreen mode
    primaryStage.setFullScreen(false);
    // primaryStage.setFullScreenExitHint("");
    // Optional: remove the default "press ESC to exit full screen" message
    // primaryStage.setResizable(false);

    // Set the scene and show the stage
    primaryStage.setScene(welcomeScene);
    primaryStage.setTitle("Self Check Kiosk by Clarke");
    primaryStage.show();

    // Stop timer globally when application is closed,
    // so application wont run even after closing the window
    primaryStage.setOnCloseRequest(e -> InactivityTimer.getInstance().stopTimer());
  }

  /**
   * To run the app.
   *
   * @param args Input
   */
  public static void main(String[] args) {
    launch(args);
  }
}