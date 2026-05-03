package org.example.kiosk;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Color;

/**
 * Manages label-related operations in the kiosk application.
 */
public class LabelManager {
  private static final List<Labeled> allLabels = new ArrayList<>();
  private static Color currentColor = Color.BLACK;

  /**
   * Registration of a labeled in the label manager.
   *
   * @param labeled new labeled that should be registered
   */
  public static void register(Labeled labeled) {
    labeled.setTextFill(currentColor);
    allLabels.add(labeled);
  }

  /**
   * Sets the text color for all registered labels and updates the current color.
   */
  public static void setTextColor(Color color) {
    currentColor = color;
    for (Labeled labeled : allLabels) {
      labeled.setTextFill(color);
    }
  }

  public static Color getCurrentColor() {
    return currentColor;
  }
}
