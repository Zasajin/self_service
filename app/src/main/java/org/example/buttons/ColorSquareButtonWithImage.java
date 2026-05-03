package org.example.buttons;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Square button with image on top and label below.
 * Supports dynamic border/text color updates.
 */
public class ColorSquareButtonWithImage extends AnimatedButton {

  private Label buttonLabel;
  private static String buttonColor = "rgb(0, 0, 0)";
  private ImageView imageView;
  private static final List<ColorSquareButtonWithImage> instances = new ArrayList<>();

  /**
   * Constructor to create a color square button with image and label.
   */
  public ColorSquareButtonWithImage(String buttonText, String imageName) {
    this.setPrefSize(140, 140);
    instances.add(this);

    // Load and tint image
    this.imageView = createTintedImageView(imageName, Color.web(buttonColor));

    // Create label
    buttonLabel = new Label(buttonText);

    // Layout
    VBox layout = new VBox(8, this.imageView, buttonLabel);
    layout.setAlignment(Pos.CENTER);

    StackPane stack = new StackPane(layout);
    stack.setPadding(new Insets(10));
    this.setGraphic(stack);

    applyStyle();

  }

  private void applyStyle() {
    // Apply image tint effect
    if (imageView != null) {
      imageView.setStyle(
          "-fx-effect: innershadow(gaussian, " + buttonColor + ", 100, 0.0, 0, 0);");
    }

    // Button style
    this.setStyle("-fx-background-color: white;"
        + "-fx-border-color: " + buttonColor + ";"
        + "-fx-border-width: 2;"
        + "-fx-border-radius: 20;"
        + "-fx-background-radius: 20;");

    // Label style
    if (buttonLabel != null) {
      buttonLabel.setStyle("-fx-background-color: transparent;"
          + "-fx-font-size: 18;"
          + "-fx-font-weight: normal;"
          + "-fx-padding: 5 0;"
          + "-fx-text-fill: " + buttonColor + ";");
    }
  }

  public Label getButtonLabel() {
    return buttonLabel;
  }

  /**
   * Sets the text of the button label.
   */
  public void setButtonText(String text) {
    if (buttonLabel != null) {
      buttonLabel.setText(text);
    }
  }

  /**
   * Static method to set color for all buttons.
   */
  public static void setButtonColor(Color color) {
    buttonColor = toRgbString(color);
    for (ColorSquareButtonWithImage button : instances) {
      button.applyStyle();
    }
  }

  private static String toRgbString(Color color) {
    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);
    return "rgb(" + r + ", " + g + ", " + b + ")";
  }

  // Tint the image to match the color
  private ImageView createTintedImageView(Image image, Color tintColor) {
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(60);
    imageView.setFitHeight(60);
    imageView.setStyle(
        "-fx-effect: innershadow(gaussian, " + toRgbString(tintColor) + ", 100, 0.0, 0, 0);");
    return imageView;
  }

  // Overloaded method to allow passing image path as String
  private ImageView createTintedImageView(String imagePath, Color tintColor) {
    try {
      Image image = new Image(imagePath, true);
      return createTintedImageView(image, tintColor);
    } catch (Exception e) {
      System.err.println("Failed to load image: " + imagePath);
      return new ImageView();
    }
  }

}
