package org.example.buttons;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.orders.Cart;

/**
 * A reusable red cancel button component with a red X icon.
 */
public class CancelButton extends AnimatedButton {

  /**
   * Constructs a red cancel button with icon and styling.
   */
  public CancelButton() {
    // Set default button size
    this.setPrefSize(30, 30);

    // Set style of button
    this.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-border-color: red;"
            + "-fx-border-width: 3;"
            + "-fx-border-radius: 12;"
            + "-fx-padding: 10;"
            + "-fx-background-radius: 12;"
            + "-fx-text-fill: red;"
            + "-fx-font-weight: bold;");

    // Using hard coded cancel image
    ImageView cancelIcon = new ImageView(new Image(getClass().getResourceAsStream("/cancel.png")));

    // Set size of image
    cancelIcon.setFitWidth(30);
    cancelIcon.setFitHeight(30);

    // Put the cancel image inside the button
    this.setGraphic(cancelIcon);
    this.setMinSize(60, 60);

    // Clear cart when clicked
    this.setOnAction(e -> Cart.getInstance().clearCart());
  }
}
