package org.example.buttons;

import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A reusable black back button component with a back arrow icon.
 */
public class BackButton extends AnimatedButton {

  /**
   * Constructs a styled back button.
   */
  public BackButton() {

    // Setting size for back button
    this.setPrefSize(30, 30);

    // Set style of button
    this.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-border-color: black;"
            + "-fx-border-width: 3;"
            + "-fx-border-radius: 12;"
            + "-fx-padding: 10;"
            + "-fx-background-radius: 12;"
            + "-fx-text-fill: black;"
            + "-fx-font-weight: bold;");

    // Setting image for back button
    ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/back.png")));
    backIcon.setFitHeight(45);
    backIcon.setFitWidth(45);

    // Put the back image inside the button
    this.setGraphic(backIcon);
    this.setContentDisplay(ContentDisplay.TOP);
    this.setMinSize(60, 60);
  }
}
