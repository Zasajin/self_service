package org.example.buttons;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A custom JavaFX button that displays text and an image,
 * with a shared background color applied to all instances.
 */
public class BlackButtonWithImage extends AnimatedButton {

  private Label buttonLabel;
  private static String buttonBackgroundColor = "rgb(0, 0, 0)";
  private static final List<BlackButtonWithImage> INSTANCES = new ArrayList<>();

  /**
   * Creates a new BlackButtonWithImage.
   *
   * @param buttonText the text to display on the button
   * @param imageName  the path to the image file
   */
  public BlackButtonWithImage(String buttonText, String imageName) {
    setPrefSize(460, 140);
    INSTANCES.add(this); // Register instance for global style updates

    applyStyle();

    Image image = new Image(imageName);
    ImageView buttonImage = new ImageView(image);
    buttonImage.setFitWidth(100);
    buttonImage.setFitHeight(100);

    buttonLabel = new Label(buttonText);
    buttonLabel.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-font-weight: normal;"
            + "-fx-font-size: 38;"
            + "-fx-padding: 5 10;"
            + "-fx-background-radius: 10;"
            + "-fx-text-fill: white;");

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox contentRow = new HBox(10, buttonLabel, spacer, buttonImage);
    contentRow.setPadding(new Insets(10));
    contentRow.setPrefHeight(140);
    contentRow.setStyle("-fx-alignment: center;");

    setGraphic(contentRow);

    // Add default press/release animation
    setOnMousePressed(e -> animateButtonPress(this, 0.95));
    setOnMouseReleased(e -> animateButtonPress(this, 1.0));
  }

  /**
   * Returns the label used in this button.
   *
   * @return the Label node
   */
  public Label getButtonLabel() {
    return buttonLabel;
  }

  /**
   * Updates the text of the button label.
   *
   * @param text the new text to set
   */
  public void setButtonText(String text) {
    if (buttonLabel != null) {
      buttonLabel.setText(text);
    }
  }

  /** Applies the current shared background style to this button. */
  private void applyStyle() {
    setStyle(
        "-fx-background-color: " + buttonBackgroundColor + ";"
            + "-fx-border-radius: 30;"
            + "-fx-background-radius: 30;"
            + "-fx-padding: 10 20;");
  }

  /** Updates the background style for all button instances. */
  private static void updateAllStyles() {
    for (BlackButtonWithImage button : INSTANCES) {
      button.applyStyle();
    }
  }

  /**
   * Sets the shared background color for all buttons.
   *
   * @param color the new Color to apply
   */
  public static void setButtonBackgroundColor(Color color) {
    buttonBackgroundColor = toRgbString(color);
    updateAllStyles();
  }

  /**
   * Converts a JavaFX Color to an RGB string.
   *
   * @param color the Color to convert
   * @return the RGB string in format "rgb(r, g, b)"
   */
  private static String toRgbString(Color color) {
    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);
    return "rgb(" + r + ", " + g + ", " + b + ")";
  }

  private void animateButtonPress(Button button, double scale) {
    ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
    st.setToX(scale);
    st.setToY(scale);
    st.play();
  }
}
