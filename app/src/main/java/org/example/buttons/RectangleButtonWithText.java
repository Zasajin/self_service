package org.example.buttons;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This is the rectangular button mostly used for + and - symbols.
 */
public class RectangleButtonWithText extends AnimatedButton {

  /**
   * Constructor that creates either a + or a - button with its own settings.
   *
   * @param buttonSign The symbol to display ("+" or "-")
   */
  public RectangleButtonWithText(String buttonSign) {
    // Classic setting up of the size and playing with the style of the buttons
    this.setText(buttonSign);
    this.setFont(Font.font(24));
    this.setPrefSize(175, 100);
    this.setMaxSize(175, 100);
    this.setMinSize(80, 40);

    // Common style properties for both buttons
    this.setStyle(
        "-fx-background-radius: 5;" // Smaller radius for rectangular look
            + "-fx-border-radius: 5;"
            + "-fx-border-width: 2;"
            + "-fx-font-weight: bold;"
            + "-fx-content-display: center;"
            + "-fx-padding: 0;");

    if (buttonSign.equals("+")) {
      this.setTextFill(Color.WHITE);
      this.setStyle(this.getStyle()
          + "-fx-background-color: black;"
          + "-fx-border-color: black;");
    } else if (buttonSign.equals("-")) {
      this.setTextFill(Color.BLACK);
      this.setStyle(this.getStyle()
          + "-fx-background-color: white;"
          + "-fx-border-color: black;");
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
}