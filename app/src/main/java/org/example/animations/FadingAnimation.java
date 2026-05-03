package org.example.animations;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Fading animation for fading out of a scene or fading into
 * a scene.
 * A more graceful way of transitioning between scenes.
 */
public class FadingAnimation {

  private Stage primaryStage;

  /**
   * This is the constructor of the fade out animation class.
   * Adding this, will just create the instance. This doesn't
   * animate anything. You still need to choose a sub method
   * for this.
   */
  public FadingAnimation(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  /**
   * Animation for fading out of a specific scene.
   * It loads a full coloured (color input depending) screen over
   * the current screen and becomes gradually more visible. This
   * way, it covers all elements equally and you don't have to
   * bother adding specific fading animations for specific
   * elements of the scene.
   *
   * @param color        color of the overlay (if background of scene is
   *                     white, you sould use white here)
   * @param currentScene current scene youre in (before animation)
   * @param nextScene    next scene it should show (after animation)
   */
  public void fadeOutAnimation(String color, Scene currentScene, Scene nextScene) {
    // Create window the size of the scene, as overlay to use for fading
    StackPane overlay = new StackPane();

    // setting color of overlay depending on input
    overlay.setStyle("-fx-background-color:" + color + ";");

    // overlay will be invisible at first
    overlay.setOpacity(0);

    // Get stack pane of (root) of scene
    StackPane fadingPane = (StackPane) currentScene.getRoot();
    // Combine transparent overlay with scene to cover screen
    fadingPane.getChildren().addAll(overlay);

    // Create fading animation that lasts for 3 seconds
    FadeTransition fadeTransition = new FadeTransition(
        Duration.seconds(3),
        overlay);

    // Make the fading go from transparent to fully white
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1);

    // Have event with delay + animation and then welcome screen
    fadeTransition.setOnFinished(event -> {
      // Pause after animation with a small delay
      PauseTransition delay = new PauseTransition(Duration.seconds(0.5));

      // Send user to new scene after delay + fade animation
      delay.setOnFinished(e -> {
        primaryStage.setScene(nextScene);
      });

      // start the delay after animation
      delay.play();
    });

    // Have a small delay before the animation starts
    PauseTransition delayBeforeTransition = new PauseTransition(Duration.seconds(3));

    // Play animation after this delay
    delayBeforeTransition.setOnFinished(event -> {
      fadeTransition.play();
    });

    // Start with delay (then the rest will play since it's all connected now)
    delayBeforeTransition.play();
  }
}
