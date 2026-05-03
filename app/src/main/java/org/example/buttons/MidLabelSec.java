package org.example.buttons;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * A custom label class for displaying mid-section labels with global style settings.
 */
public class MidLabelSec extends Label {
  // Global setting for all the titleLabels
  private static String fontFamily = "Tahoma";
  private static int fontSize = 20;
  private static String fontWeight = "bolder";
  private static String textAlignment = "center";
  private static String textColor = "rgb(1, 176, 51)";

  private static final List<WeakReference<MidLabelSec>> instances = new ArrayList<>();

  /**
   * Constructs a TitleLabel with the specified text and applies the global style.
   */
  public MidLabelSec(String text) {
    super(text);
    instances.add(new WeakReference<>(this));
    applyStyle();
  }

  private void applyStyle() {
    setStyle(
        "-fx-font-family: '" + fontFamily + "';"
        + "-fx-font-size: " + fontSize + "px;"
        + "-fx-font-weight: " + fontWeight + ";"
        + "-fx-text-alignment: " + textAlignment + ";"
        + "-fx-text-fill: " + textColor + ";");
  }

  /**
   * Setter for the font of the title label.
   *
   * @param font String name of the font
   */
  public static void setFontFamily(String font) {
    fontFamily = font;
    updateAllStyles();
  }

  /**
   * Setter for the font size of the title label.
   *
   * @param size int font size value
   */
  public static void setFontSize(int size) {
    fontSize = size;
    updateAllStyles();
  }

  /**
   * Setter for weight of the title label.
   *
   * @param weight String weight
   */
  public static void setFontWeight(String weight) {
    fontWeight = weight;
    updateAllStyles();
  }

  /**
   * Setter for alignment of the title label.
   *
   * @param alignment String alignment (center, etc.)
   */
  public static void setTextAlignment(String alignment) {
    textAlignment = alignment;
    updateAllStyles();
  }

  /**
   * Setter for text colour.
   *
   * @param color colour of the label
   */
  public static void setTextColor(Color color) {
    textColor = toRgbString(color);
    updateAllStyles();
  }

  private static void updateAllStyles() {
    Iterator<WeakReference<MidLabelSec>> iterator = instances.iterator();

    while (iterator.hasNext()) {
      MidLabelSec label = iterator.next().get();
      if (label == null) {
        iterator.remove();
      } else {
        label.applyStyle();
      }
    }
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
  public static Color getTextColor() {

    try {

      String[] rgb = textColor.replace("rgb(", "").replace(")", "").split(",");
      int r = Integer.parseInt(rgb[0].trim());
      int g = Integer.parseInt(rgb[1].trim());
      int b = Integer.parseInt(rgb[2].trim());

      return Color.rgb(r, g, b);

    } catch (Exception e) {

      return Color.BLACK;

    }

  }
}
