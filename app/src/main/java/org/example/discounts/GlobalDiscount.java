package org.example.discounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.sql.DatabaseManager;

/**
 * Class for global discounts.
 */
public class GlobalDiscount implements IdiscountStrategy {

  public double percentage;

  /**
   * Applying discount on ALL items.
   * TODO move method to sql queries class
   */
  public void applyDiscount(double percentage) {
    double discountFactor = (1 - (percentage / 100));
    String selectItemsSql = "SELECT product_id, price FROM product";
    String updatingItemsSql = "UPDATE product SET price = ? WHERE product_id = ?";

    try (Connection connection = DatabaseManager.getConnection()) {

      PreparedStatement selectStmt = connection.prepareStatement(selectItemsSql);
      PreparedStatement updateStmt = connection.prepareStatement(updatingItemsSql);
      {

        connection.setAutoCommit(false);
        ResultSet selectRs = selectStmt.executeQuery();

        while (selectRs.next()) {
          int productId = selectRs.getInt("product_id");
          double originalPrice = selectRs.getDouble("price");
          double discountedPrice = originalPrice * discountFactor;

          discountedPrice = Math.round(discountedPrice * 100.0) / 100.0;

          updateStmt.setDouble(1, discountedPrice);
          updateStmt.setInt(2, productId);
          updateStmt.addBatch();
        }

        updateStmt.executeBatch();
        connection.commit();

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
