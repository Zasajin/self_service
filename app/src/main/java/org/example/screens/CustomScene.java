package org.example.screens;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Scene subclass for scenes with customizable backround.
 */
public class CustomScene extends Scene {

  private Pane rootPane;

  /**
   * Constructor.
   */
  public CustomScene(Pane root, double width, double height) {

    super(root, width, height);
    this.rootPane = root;

  }

  /**
   * Changing the backgroundcolor.
   *
   * @param color the wanted color
   */
  public void setBackgroundColor(Color color) {

    String rgb = String.format("rgb(%d, %d, %d)",
        (int) (color.getRed() * 255),
        (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255));
    rootPane.setStyle("-fx-background-color: " + rgb + ";");

  }

}
