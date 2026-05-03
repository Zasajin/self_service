package org.example.buttons;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 * A custom JavaFX button with a white background and colored outline,
 * displaying a label on the left and an image on the right.
 */
public class ColorBtnOutlineImage extends AnimatedButton {

  private static final List<ColorBtnOutlineImage> INSTANCES = new ArrayList<>();

  private static String buttonColor = "rgb(0, 0, 0)";

  private Label buttonLabel;
  private ImageView imageView;

  /**
   * Constructs a ColorBtnOutlineImage with specified text and image path.
   *
   * @param buttonText the text to display on the button
   * @param imageName  the path to the image file
   */
  public ColorBtnOutlineImage(String buttonText, String imageName) {
    setPrefSize(460, 140);
    INSTANCES.add(this);

    Image image = new Image(imageName);
    imageView = new ImageView(image);
    imageView.setFitWidth(100);
    imageView.setFitHeight(100);
    imageView.setPreserveRatio(true);

    buttonLabel = new Label(buttonText);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox contentRow = new HBox(10, buttonLabel, spacer, imageView);
    contentRow.setPadding(new Insets(10));
    contentRow.setStyle("-fx-alignment: center;");

    setGraphic(contentRow);

    applyStyle();
  }

  /**
   * Applies the CSS styles for the button, including background,
   * border, text color, and image effects.
   */
  private void applyStyle() {
    setStyle(
        "-fx-background-color: white;"
            + "-fx-border-color: " + buttonColor + ";"
            + "-fx-border-width: 2;"
            + "-fx-border-radius: 30;"
            + "-fx-background-radius: 30;"
            + "-fx-padding: 10 20;");

    buttonLabel.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-font-weight: normal;"
            + "-fx-font-size: 38;"
            + "-fx-padding: 5 10;"
            + "-fx-text-fill: " + buttonColor + ";");

    imageView.setStyle(
        "-fx-effect: innershadow(gaussian, " + buttonColor + ", 100, 0.0, 0, 0);");
  }

  /**
   * Returns the Label component of the button.
   *
   * @return the label displayed on the button
   */
  public Label getButtonLabel() {
    return buttonLabel;
  }

  /**
   * Updates the text displayed on the button label.
   *
   * @param text the new text to set
   */
  public void setButtonText(String text) {
    if (buttonLabel != null) {
      buttonLabel.setText(text);
    }
  }

  /**
   * Sets the outline color of all ColorBtnOutlineImage instances
   * and updates their styles accordingly.
   *
   * @param color the new color to apply
   */
  public static void setButtonColor(Color color) {
    buttonColor = toRgbString(color);
    for (ColorBtnOutlineImage button : INSTANCES) {
      button.applyStyle();
    }
  }

  /**
   * Converts a Color object to an RGB CSS string.
   *
   * @param color the Color object to convert
   * @return a string in the format "rgb(r, g, b)"
   */
  private static String toRgbString(Color color) {
    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);
    return "rgb(" + r + ", " + g + ", " + b + ")";
  }
}
