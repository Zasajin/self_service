package org.example.buttons;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * This is the medium-sized button with centered text and no image.
 */
public class MidButton extends AnimatedButton {

  /**
   * Class constructor.
   */
  public MidButton(String buttonText, String buttonColor, int fontSize) {
    // Set button size
    this.setPrefSize(460, 140);

    // Label setup
    buttonLabel = new Label(buttonText);
    buttonLabel.setStyle("-fx-background-color: transparent;"
        + "-fx-font-weight: normal;"
        + "-fx-font-size: " + fontSize + "px;"
        + "-fx-padding: 5 10;"
        + "-fx-background-radius: 10;");

    // Set color styles
    if (buttonColor.equals("rgb(255, 255, 255)")) {
      buttonLabel.setStyle(buttonLabel.getStyle() + "-fx-text-fill: black;");
      this.setStyle(
          "-fx-background-color: rgb(255, 255, 255);"
              + "-fx-border-color: black;"
              + "-fx-border-width: 2;"
              + "-fx-border-radius: 30;"
              + "-fx-background-radius: 30;"
              + "-fx-padding: 10 20;");
    } else {
      buttonLabel.setStyle(buttonLabel.getStyle() + "-fx-text-fill: white;");
      this.setStyle(
          "-fx-background-color: " + buttonColor + ";"
              + "-fx-border-radius: 30;"
              + "-fx-background-radius: 30;"
              + "-fx-padding: 10 20;");
    }

    // Center the label inside an HBox
    HBox centeredContent = new HBox(buttonLabel);
    centeredContent.setAlignment(Pos.CENTER);
    centeredContent.setPadding(new Insets(10));
    centeredContent.setPrefHeight(140);

    // Set the button graphic to the centered HBox
    this.setGraphic(centeredContent);
  }

  private Label buttonLabel;

  public Label getButtonLabel() {
    return buttonLabel;
  }

  /**
   * Sets the text of the button's label.
   */
  public void setButtonText(String text) {
    if (buttonLabel != null) {
      buttonLabel.setText(text);
    }
  }
}
