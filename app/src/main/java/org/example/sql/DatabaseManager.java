package org.example.sql;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Method for database connection pooling.
 */
public class DatabaseManager {
  private static BasicDataSource dataSource;

  static {
    dataSource = new BasicDataSource();
    dataSource.setUrl("jdbc:mysql://b8gwixcok22zuqr5tvdd-mysql.services"
        + ".clever-cloud.com:21363/b8gwixcok22zuqr5tvdd");
    dataSource.setUsername("u5urh19mtnnlgmog");
    dataSource.setPassword("zPgqf8o6na6pv8j8AX8r");
    dataSource.setMinIdle(5);
    dataSource.setMaxIdle(5);
    dataSource.setMaxTotal(5);
    dataSource.setMaxWaitMillis(-1);
    dataSource.setTestOnBorrow(true);
    dataSource.setValidationQuery("SELECT 1");

  }

  /**
   * Getter for database connection.
   *
   * @return the proper connection to the database
   * @throws SQLException SQL errors
   */
  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  /**
   * Closes connection upon calling.
   *
   * @param connection this connection.
   */
  public static void closeConnection(Connection connection) {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}