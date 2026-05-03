package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class just to check if queries can be ran in the database.
 */
public class TestingQueries {
  private Connection connection;

  /**
   * Testing queries constructor.
   *
   * @param connection database connection
   */
  public TestingQueries(Connection connection) {
    this.connection = connection;
  }
  /**
   * Example method to insert something into order.
   *
   * @param username Admin username to insert.
   * @param password Admin password to insert.
   * @return boolean
   */

  public boolean insertAdmin(String username, String password) {
    String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
        
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, username);
      pstmt.setString(2, password);
            
      int affectedRows = pstmt.executeUpdate();
      connection.commit();
      return affectedRows > 0;
      
    } catch (SQLException e) {
      try {
        connection.rollback(); // Cancels operation if an error occurs
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
      e.printStackTrace();
      return false;
    }
  }
}
