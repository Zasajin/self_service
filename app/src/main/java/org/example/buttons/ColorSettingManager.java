package org.example.buttons;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import javafx.scene.paint.Color;

/**
 * Class to read and write color scheme from/to a data file.
 */
public class ColorSettingManager {

  private static final String Settings_File = "colors.properties";

  /**
   * Saves currently chosen color scheme.
   *
   * @param primary color
   * @param secondary color
   * @param background color
   */
  public static void saveColors(
      Color primary,
      Color secondary,
      Color background
  ) throws IOException {

    Properties props = new Properties();
    props.setProperty("primary", colorToHex(primary));
    props.setProperty("secondary", colorToHex(secondary));
    props.setProperty("background", colorToHex(background));

    try (OutputStream out = new FileOutputStream(Settings_File)) {

      props.store(out, "Saved color settings");

    }

  }

  /**
   * Loads previously saved color scheme from file.
   *
   * @return list with colors
   */
  public static Color[] loadColors() throws IOException {

    if (!Files.exists(Path.of(Settings_File))) {

      return null;

    }

    Properties props = new Properties();

    try (InputStream in = new FileInputStream(Settings_File)) {

      props.load(in);

    }

    Color primary = hexToColor(props.getProperty("primary"));
    Color secondary = hexToColor(props.getProperty("secondary"));
    Color background = hexToColor(props.getProperty("background"));

    return new Color[]{primary, secondary, background};

  }

  /**
   * Transfroms color code to string.
   *
   * @param color the color
   * @return the string
   */
  private static String colorToHex(Color color) {

    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);

    return String.format("#%02X%02X%02X", r, g, b);

  }

  /**
   * Transforms string to color.
   *
   * @param hex String holding color code
   * @return returns color code
   */
  private static Color hexToColor(String hex) {

    if (hex == null || hex.isEmpty()) {

      return Color.WHITE;

    }

    return Color.web(hex);

  }

  /**
   * Resets the colors to our default scheme.
   */
  public static void resetToDefaults() throws IOException {

    Color defaultPrime = Color.web("#000000");
    Color defaultSecond = Color.web("#01b033");
    Color defaultBackground = Color.web("#FFFFFF");

    saveColors(defaultPrime, defaultSecond, defaultBackground);

  }
    
}
