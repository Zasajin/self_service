package org.example.buttons;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Eat here button with icon and label.
 */
public class EatHereButton extends VBox {
  private final Button button;

  /**
   * Creates a button with an image and label for eat here type.
   *
   *
   */
  public EatHereButton() {
    this.setAlignment(Pos.CENTER);
    this.setSpacing(10);

    ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/eatHere.png")));
    icon.setFitWidth(60);
    icon.setFitHeight(60);
    button = new Button();
    button.setGraphic(icon);
    button.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-border-color: black;"
            + "-fx-border-width: 3;"
            + "-fx-border-radius: 15;"
            + "-fx-background-radius: 15;"
            + "-fx-padding: 15;");

    button.setMinSize(100, 100);

    Label label = new Label("Eat Here");
    label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

    this.getChildren().addAll(button, label);
  }

  /**
   * Getter for takeaway button.
   *
   * @return the button
   */
  public Button getButton() {
    return button;
  }

}
