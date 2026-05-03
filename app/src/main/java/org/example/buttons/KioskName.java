package org.example.buttons;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class for changing the name of the kiosk.
 */
public class KioskName {

  private static final String File_Path = "kiosk_name.txt";
  private static String companyTitle = loadCompanyTitle();

  /**
   * Getter for the current name.
   *
   * @return the name
   */
  public static String getCompanyTitle() {

    return companyTitle;

  }

  /**
   * Sets the new title.
   *
   * @param newTitle the new title from the customization screen
   */
  public static void setCompanyTitle(String newTitle) {

    companyTitle = newTitle;
    saveCompanyTitle(newTitle);

  }

  private static String loadCompanyTitle() {

    try {

      File file = new File(File_Path);

      if (file.exists()) {

        return new String(Files.readAllBytes(Paths.get(File_Path))).trim();

      }

    } catch (IOException e) {

      e.printStackTrace();

    }

    return "Bun & Patty";

  }

  private static void saveCompanyTitle(String title) {

    try (FileWriter writer = new FileWriter(File_Path)) {

      writer.write(title);

    } catch (IOException e) {

      e.printStackTrace();

    }

  }

}
