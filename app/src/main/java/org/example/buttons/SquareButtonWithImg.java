package org.example.buttons;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Square button with an image on top and a label below.
 */
public class SquareButtonWithImg extends AnimatedButton {

  /**
   * Constructor for vertically stacked button (image on top, label below).
   */
  public SquareButtonWithImg(String buttonText, String imageName, String buttonColor) {
    // Set fixed square button size (140x140)
    this.setPrefSize(140, 140);

    // Image setup (smaller than before)
    Image image = new Image(imageName);
    ImageView buttonImage = new ImageView(image);
    buttonImage.setFitWidth(60);
    buttonImage.setFitHeight(60);

    // Label setup
    buttonLabel = new Label(buttonText);
    buttonLabel.setStyle("-fx-background-color: transparent;"
        + "-fx-font-size: 18;"
        + "-fx-font-weight: normal;"
        + "-fx-padding: 5 0;");

    // Adjust text color based on background color
    if (buttonColor.equals("rgb(255, 255, 255)")) {
      buttonLabel.setStyle(buttonLabel.getStyle() + "-fx-text-fill: black;");
      this.setStyle(
          "-fx-background-color: rgb(255, 255, 255);"
              + "-fx-border-color: black;"
              + "-fx-border-width: 2;"
              + "-fx-border-radius: 20;"
              + "-fx-background-radius: 20;");
    } else {
      buttonLabel.setStyle(buttonLabel.getStyle() + "-fx-text-fill: white;");
      this.setStyle(
          "-fx-background-color: " + buttonColor + ";"
              + "-fx-border-radius: 20;"
              + "-fx-background-radius: 20;");
    }

    // Image on top, label below
    VBox contentColumn = new VBox(8, buttonImage, buttonLabel);
    contentColumn.setAlignment(Pos.CENTER);

    // Stackpane for centering and padding
    StackPane stack = new StackPane(contentColumn);
    stack.setPadding(new Insets(10));

    // Set button's graphic (stacked layout)
    this.setGraphic(stack);
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
