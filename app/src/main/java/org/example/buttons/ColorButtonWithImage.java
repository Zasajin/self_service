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
 * A custom JavaFX button that displays a label and an image,
 * with a customizable background color.
 */
public class ColorButtonWithImage extends AnimatedButton {

  private Label buttonLabel;

  private static String buttonBackgroundColor = "rgb(1, 176, 51)";
  private static final List<ColorButtonWithImage> instances = new ArrayList<>();

  /**
   * Creates a new ColorButtonWithImage.
   *
   * @param buttonText the text to display on the button
   * @param imageName  the path to the image file
   */
  public ColorButtonWithImage(String buttonText, String imageName) {
    setPrefSize(460, 140);
    instances.add(this); // register this button instance

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

  private void applyStyle() {
    setStyle(
        "-fx-background-color: " + buttonBackgroundColor + ";"
            + "-fx-border-radius: 30;"
            + "-fx-background-radius: 30;"
            + "-fx-padding: 10 20;");
  }

  private static void updateAllStyles() {
    for (ColorButtonWithImage button : instances) {
      button.applyStyle();
    }
  }

  /**
   * Setter for the buttons background colour.
   *
   * @param color Colour of the background
   */
  public static void setButtonBackgroundColor(Color color) {
    buttonBackgroundColor = toRgbString(color);
    updateAllStyles();
  }

  private static String toRgbString(Color color) {
    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);
    return "rgb(" + r + ", " + g + ", " + b + ")";
  }

  /**
   * To set the color picker pane default to current color.
   *
   * @return current color
   */
  public static Color getButtonColor() {

    try {

      String[] rgb = buttonBackgroundColor.replace("rgb(", "").replace(")", "").split(",");
      int r = Integer.parseInt(rgb[0].trim());
      int g = Integer.parseInt(rgb[1].trim());
      int b = Integer.parseInt(rgb[2].trim());

      return Color.rgb(r, g, b);

    } catch (Exception e) {

      return Color.BLACK;

    }
  }
}
