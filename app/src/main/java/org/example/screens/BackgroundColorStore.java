package org.example.screens;

import javafx.scene.paint.Color;

/**
 * Class for storing customized background color.
 * uses static for global usability without instantiation
 */
public class BackgroundColorStore {

  private static Color currentBackgroundColor = Color.WHITE;

  /**
   * Getter for the currently chosen color.
   *
   * @return the color
   */
  public static Color getCurrentBackgroundColor() {

    return currentBackgroundColor;

  }

  /**
   * Sets the newly chosen color.
   *
   * @param color the new color
   */
  public static void setCurrentBackgroundColor(Color color) {

    currentBackgroundColor = color;

  }

}
