package org.example.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for checking if user inputs correct admin details for accessing the
 * admin menu.
 */

public class AdminAuth {
  private Connection connection;

  /**
   * Admin Authentication class constructor.
   *
   * @param connection server connection
   */
  public AdminAuth(Connection connection) {
    this.connection = connection;
  }

  /**
   * Verifies admin credentials.
   *
   * @param username Admin username
   * @param password Admin password
   * @return true if credentials are valid, otherwise false.
   */
  public boolean verifyAdmin(String username, String password) {
    String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, username);
      pstmt.setString(2, password);

      ResultSet rs = pstmt.executeQuery();
      return rs.next(); // Returns true if a matching record was found
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}