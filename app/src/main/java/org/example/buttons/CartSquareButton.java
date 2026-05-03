package org.example.buttons;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * A square button with an image only, designed for use in the application.
 * This button has a fixed size and a transparent background.
 * Supports dynamic tint color updates via a color picker.
 */
public class CartSquareButton extends AnimatedButton {

  private static final List<CartSquareButton> instances = new ArrayList<>();
  private static String buttonColor = "rgb(0, 0, 0)";

  private ImageView cartIcon;

  /**
   * Constructs a new CartSquareButton, initializes the cart icon, and sets up the
   * button style.
   */
  public CartSquareButton() {
    instances.add(this);

    // Load image
    cartIcon = new ImageView(new Image(getClass().getResourceAsStream("/cart_bl.png")));
    cartIcon.setFitWidth(100);
    cartIcon.setFitHeight(100);
    cartIcon.setPreserveRatio(true);

    applyColor();

    // Configure button size and style
    this.setPrefSize(140, 140);
    this.setMinSize(140, 140);
    this.setMaxSize(140, 140);
    this.setGraphic(cartIcon);
    this.setStyle("-fx-background-color: transparent;");
  }

  /**
   * Applies the tint color to the cart icon.
   */
  private void applyColor() {
    cartIcon.setStyle("-fx-effect: innershadow(gaussian, " + buttonColor + ", 100, 0.0, 0, 0);");
  }

  /**
   * Sets the tint color for all CartSquareButton instances.
   */
  public static void setButtonColor(Color color) {
    buttonColor = toRgbString(color);
    for (CartSquareButton btn : instances) {
      btn.applyColor();
    }
  }

  /**
   * Converts a JavaFX Color to an RGB string for CSS.
   */
  private static String toRgbString(Color color) {
    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);
    return "rgb(" + r + ", " + g + ", " + b + ")";
  }
}
