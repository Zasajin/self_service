package org.example.buttons;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * This class represents the arrow buttons.
 * Used in:
 * menu screen to browse
 * checkout screen to switch between item pages
 * if order is larger than max items per checkout page
 * item detail screen to browse between ingredient list of an item
 */
public class ArrowButton extends AnimatedButton {

  private static String buttonColor = "black"; // default color
  private static final List<ArrowButton> instances = new ArrayList<>();

  private ImageView buttonImage;

  /**
   * Arrow button constructor.
   * OBS! Arrow button uses hard coded local image and
   * not image from database!
   *
   * @param pointingLeft boolean that gives you
   *                     left arrow if set to true and
   *                     right arrow if set to false
   * @param setGrey      boolean that makes button grey and
   *                     unclickable if set to true
   */
  public ArrowButton(boolean pointingLeft, boolean setGrey) {
    // Add this instance to the list for dynamic updates
    instances.add(this);

    // Default button sizes
    this.setPrefSize(100, 500);

    // Border color depends on setGrey but will be updated later dynamically if
    // needed
    String borderColor = setGrey ? "grey" : buttonColor;

    // Set normal style of button
    this.setStyle(
        "-fx-background-color: rgb(255, 255, 255);"
            + "-fx-border-color: " + borderColor + ";"
            + "-fx-border-width: 3px;"
            + "-fx-border-radius: 12px;"
            + "-fx-background-radius: 12;"
            + "-fx-padding: 10px;");

    // Using hard coded arrow image (assumed black arrow)
    this.buttonImage = new ImageView(new Image("/nav_bl.png"));

    // Set size and scalability of image
    buttonImage.setFitHeight(40);
    buttonImage.setPreserveRatio(true);

    // Arrow will point right if not pointingLeft
    if (!pointingLeft) {
      // Mirror image to point right
      buttonImage.setScaleX(-1);
    }

    // Apply color tint to image
    applyColorTint(setGrey ? Color.GREY : Color.web(buttonColor));

    // Make image inside button (arrow) grey and unclickable if setGrey
    if (setGrey) {
      buttonImage.setOpacity(0.5);
      this.setDisable(true);
    }

    // Put the button (arrow) image inside the button
    this.setGraphic(buttonImage);
  }

  /**
   * Applies a color tint effect on the arrow image.
   */
  private void applyColorTint(Color color) {
    if (buttonImage != null) {
      // Tint the arrow with an inner shadow of the given color
      buttonImage.setStyle("-fx-effect: innershadow(gaussian, "
          + toRgbString(color) + ", 100, 0.0, 0, 0);");
    }
  }

  /**
   * Static method to set color for all ArrowButtons.
   */
  public static void setButtonColor(Color color) {
    buttonColor = toRgbString(color);
    for (ArrowButton button : instances) {
      // Update border color style
      button.setStyle(
          "-fx-background-color: rgb(255, 255, 255);"
              + "-fx-border-color: " + buttonColor + ";"
              + "-fx-border-width: 3px;"
              + "-fx-border-radius: 12px;"
              + "-fx-background-radius: 12;"
              + "-fx-padding: 10px;");

      // Update arrow color if button is enabled
      if (!button.isDisable()) {
        button.applyColorTint(color);
      }
    }
  }

  /**
   * Helper method to convert Color to CSS rgb string.
   */
  private static String toRgbString(Color color) {
    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);
    return "rgb(" + r + ", " + g + ", " + b + ")";
  }
}
