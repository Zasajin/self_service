package org.example.buttons;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * This is the medium sized button with an image.
 */
public class MidButtonWithImage extends AnimatedButton {

  /**
   * Class' constructor.
   */
  public MidButtonWithImage(String buttonText, String imageName, String buttonColor) {
    // Set button size
    this.setPrefSize(460, 140);

    // Image setup
    Image image = new Image(imageName);
    ImageView buttonImage = new ImageView(image);
    buttonImage.setFitWidth(100);
    buttonImage.setFitHeight(100);

    // Label setup
    buttonLabel = new Label(buttonText);
    buttonLabel.setStyle("-fx-background-color: transparent;"
        + "-fx-font-weight: normal;"
        + "-fx-font-size: 38;"
        + "-fx-padding: 5 10;"
        + "-fx-background-radius: 10;");

    // Set the white button
    if (buttonColor.equals("rgb(255, 255, 255)")) {
      buttonLabel.setStyle(buttonLabel.getStyle() + "-fx-text-fill: black;");
      this.setStyle(
          "-fx-background-color: rgb(255, 255, 255);"
              + "-fx-border-color: black;"
              + "-fx-border-width: 2;"
              + "-fx-border-radius: 30;"
              + "-fx-background-radius: 30;"
              + "-fx-padding: 10 20;");
      // Set the non-white button
    } else {
      buttonLabel.setStyle(buttonLabel.getStyle() + "-fx-text-fill: white;");
      this.setStyle(
          "-fx-background-color: " + buttonColor + ";"
              + "-fx-border-radius: 30;"
              + "-fx-background-radius: 30;"
              + "-fx-padding: 10 20;");
    }

    // Spacer to push image to the right
    Region hspacer = new Region();
    HBox.setHgrow(hspacer, Priority.ALWAYS);

    // Create the label & image row
    HBox contentRow = new HBox(10, buttonLabel, hspacer, buttonImage);
    contentRow.setPadding(new Insets(10));
    contentRow.setPrefHeight(140);
    contentRow.setStyle("-fx-alignment: center;");

    // Set button graphics
    this.setGraphic(contentRow);
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
