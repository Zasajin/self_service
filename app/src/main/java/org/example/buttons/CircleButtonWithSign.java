package org.example.buttons;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This is the circle button mostly used for + and - symbols.
 */
public class CircleButtonWithSign extends AnimatedButton {

  // for button color customization
  private static final List<CircleButtonWithSign> instances = new ArrayList<>();
  private static String MinusBackground = "-fx-background-color: rgb(255, 255, 255);";
  private static String MinusBorder = "-fx-border-color: rgb(0, 0, 0)";
  private static String PlusBackground = "-fx-background-color: rgb(0, 0, 0);";
  private static String PlusBorder = "-fx-border-color: rgb(0, 0, 0);";
  private final String buttonSign;

  /**
   * Construtor that creates either a + or a - button with its own settings.
   *
   * @param buttonSign The symbol to display ("+" or "-")
   */
  public CircleButtonWithSign(String buttonSign) {

    // register this button instance
    instances.add(this);

    // for dynamic changes when customized
    this.buttonSign = buttonSign;
    applyStyle();

    // Classic setting up of the size and playing with the style of the buttons
    this.setText(buttonSign);
    this.setFont(Font.font(24));
    this.setPrefSize(50, 50);
    this.setMaxSize(50, 50);
    this.setMinSize(50, 50);
    // Both buttons have almost everything the same except the colors of the
    // background, border,
    // and the text fill.
    this.setStyle(
        "-fx-background-radius: 25;"
            + "-fx-border-radius: 25;"
            + "-fx-border-width: 2;"
            + "-fx-font-weight: bold;"
            + "-fx-content-display: center;"
            + "-fx-padding: 0;");
    if (buttonSign.equals("+")) {
      this.setTextFill(Color.WHITE);
      this.setStyle(this.getStyle()
          + PlusBackground + PlusBorder);
    } else if (buttonSign.equals("-")) {
      this.setTextFill(Color.BLACK);
      this.setStyle(this.getStyle()
          + MinusBackground + MinusBorder);
    }
  }

  /**
   * This method disables the button and dims it if disabled.
   *
   * @param disabled true to make the button invalid, otherwise false
   */
  public void setInvalid(boolean disabled) {
    this.setDisable(disabled);

    if (disabled) {
      this.setOpacity(0.5);
    } else {
      this.setOpacity(1);
    }
  }

  private void applyStyle() {

    if (buttonSign.equals("+")) {

      this.setTextFill(Color.WHITE);

      this.setStyle(this.getStyle()
          + PlusBackground + PlusBorder);

    } else if (buttonSign.equals("-")) {

      this.setTextFill(Color.BLACK);

      this.setStyle(this.getStyle()
          + MinusBackground + MinusBorder);

    }

  }

  private static void updateAllStyles() {

    for (CircleButtonWithSign button : instances) {

      button.applyStyle();

    }

  }

  /**
   * Sets the new background color for minus buttons.
   *
   * @param color new color
   */
  public static void setMinusBackground(Color color) {

    MinusBackground = toRgbString("-fx-background-color", color);
    updateAllStyles();

  }

  /**
   * Sets the new border color for minus buttons.
   *
   * @param color new color
   */
  public static void setMinusBorder(Color color) {

    MinusBorder = toRgbString("-fx-border-color", color);
    updateAllStyles();

  }

  /**
   * Sets the new color for plus buttons.
   *
   * @param color new color
   */
  public static void setPlusColor(Color color) {

    PlusBackground = toRgbString("-fx-background-color", color);
    PlusBorder = toRgbString("-fx-border-color", color);
    updateAllStyles();

  }

  private static String toRgbString(String key, Color color) {

    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);

    return key + ": rgb(" + r + ", " + g + ", " + b + ");";

  }

}
