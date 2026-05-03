package org.example.buttons;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A square button with an image only, designed for use in the application.
 * This button has a fixed size and a transparent background.
 */
public class SqrBtnImgOnly extends AnimatedButton {

  /**
   * Constructs a square button with an image only.
   * The button has a fixed size, a transparent background, and displays a cart
   * icon.
   */
  public SqrBtnImgOnly() {
    // Load image and fit it within button
    ImageView cartIcon = new ImageView(new Image(getClass().getResourceAsStream("/cart_bl.png")));
    cartIcon.setFitWidth(100);
    cartIcon.setFitHeight(100);
    cartIcon.setPreserveRatio(true);

    // Configure button to always be 140x140
    this.setPrefSize(140, 140);
    this.setMinSize(140, 140);
    this.setMaxSize(140, 140);
    this.setGraphic(cartIcon);
    this.setStyle("-fx-background-color: transparent;");
  }
}
