package org.example.buttons;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Represents a primary keyboard button.
 */
public class KeyboardButtonPrim extends AnimatedButton {
  private static String buttonBackgroundColor = "rgb(1, 176, 51)";
  private static final List<KeyboardButtonPrim> instances = new ArrayList<>();

  /**
   * Constructs a primary keyboard button with the specified text.
   *
   * @param text the text to display on the button
   */
  public KeyboardButtonPrim(String text) {
    super(text);
    setPrefSize(60, 40);
    applyStyle();
    instances.add(this);

    setOnMousePressed(e -> animateButtonPress(this, 0.95));
    setOnMouseReleased(e -> animateButtonPress(this, 1.0));
  }

  private void applyStyle() {
    setStyle(
        "-fx-background-color: " + buttonBackgroundColor + ";"
        + "-fx-border-radius: 5;"
        + "-fx-background-radius: 5;"
        + "-fx-font-size: 18;"
        + "-fx-font-family: 'Arial';"
        + "-fx-text-fill: white;");
  }

  private void animateButtonPress(Button button, double scale) {
    ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
    st.setToX(scale);
    st.setToY(scale);
    st.play();
  }

  public static void setButtonBackgroundColor(Color color) {
    buttonBackgroundColor = toRgbString(color);
    updateAllStyles();
  }

  private static void updateAllStyles() {
    for (KeyboardButtonPrim button : instances) {
      button.applyStyle();
    }
  }

  private static String toRgbString(Color color) {
    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);
    return "rgb(" + r + ", " + g + ", " + b + ")";
  }
}
