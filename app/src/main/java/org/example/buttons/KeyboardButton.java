package org.example.buttons;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A custom JavaFX Button representing a keyboard key with customizable
 * background color and style.
 */
public class KeyboardButton extends Button {

  private static String buttonBackgroundColor = "black";
  private static final List<KeyboardButton> instances = new ArrayList<>();

  /**
   * Constructs a KeyboardButton with the specified text label.
   *
   * @param text the text to display on the button
   */
  public KeyboardButton(String text) {
    super(text);
    setPrefSize(60, 40);
    applyStyle();
    instances.add(this);

    // Add default press/release animation
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

  public static void setButtonBackgroundColor(Color color) {
    buttonBackgroundColor = toRgbString(color);
    updateAllStyles();
  }

  /**
   * Returns the current background color of the keyboard buttons as a
   * {@link Color} object.
   *
   * @return the current button background color
   */
  public static Color getButtonColor() {
    try {
      String[] rgb = buttonBackgroundColor.replace("rgb(", "").replace(")", "").split(",");
      int r = Integer.parseInt(rgb[0].trim());
      int g = Integer.parseInt(rgb[1].trim());
      int b = Integer.parseInt(rgb[2].trim());
      return Color.rgb(r, g, b);
    } catch (Exception e) {
      return Color.LIGHTGRAY;
    }
  }

  private static String toRgbString(Color color) {
    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);
    return "rgb(" + r + ", " + g + ", " + b + ")";
  }

  private static void updateAllStyles() {
    for (KeyboardButton button : instances) {
      button.applyStyle();
    }
  }

  private void animateButtonPress(Button button, double scale) {
    ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
    st.setToX(scale);
    st.setToY(scale);
    st.play();
  }
}