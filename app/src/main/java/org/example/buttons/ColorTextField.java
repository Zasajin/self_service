package org.example.buttons;

import javafx.scene.control.TextField;

/**
 * Represents a text field with color functionality.
 */
public class ColorTextField extends TextField {

  // Default style constants
  private static final String defaultStyle = "-fx-background-color: grey;"
      + "-fx-text-fill: black;"
      + "-fx-font-weight: lighter;"
      + "-fx-font-size: 40;"
      + "-fx-background-radius: 10;";

  private double defaultMaxWidth = 620;
  private double defaultPrefHeight = 100;

  /**
   * Constructor to create a styled text field with prompt text.
   *
   * @param promptText the placeholder text to show when the field is empty
   */
  public ColorTextField(String promptText) {
    super();

    setPromptText(promptText);
    setMaxWidth(defaultMaxWidth);
    setPrefHeight(defaultPrefHeight);
    setStyle(defaultStyle);
  }
}
