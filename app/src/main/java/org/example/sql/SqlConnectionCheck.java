package org.example.sql;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.control.Label;

/**
 * The class to check Database connection.
 */
public class SqlConnectionCheck {

  // Declare the Label
  private Label mysql;

  /**
   * Construct the class.
   */
  public SqlConnectionCheck() {
    // SQL Connection starts here
    mysql = new Label(); // Initialize the Label variable
    try (Connection connection = DatabaseManager.getConnection()) {
      mysql.setText("Driver found and connected");

      // Error handling
    } catch (SQLException e) {
      mysql.setText("An error has occurred: " + e.getMessage());
      mysql.setStyle(
          "-fx-background-color: transparent;"
              + "-fx-text-fill: black;"
              + "-fx-font-size: 10;"
              + "-fx-padding: 5 10;"
              + "-fx-background-radius: 10;");
    }
    // End of SQL Section
  }

  /**
   * Getter for the mysql label.
   *
   * @return the mysql Label.
   */
  public Label getMysqlLabel() {
    return mysql;
  }
}