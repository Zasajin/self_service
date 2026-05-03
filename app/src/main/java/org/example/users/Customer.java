package org.example.users;

import java.sql.SQLException;
import org.example.orders.Order;
import org.example.sql.SqlQueries;

/**
 * Represents a customer using the Self Service Kiosk.
 */
public class Customer implements User {

  /**
   * Creates a new order.
   *
   * @throws SQLException SQL Database errors
   */
  public int placeOrder(Order order, boolean discountApplied, int discountFactor) throws SQLException {
    try {

      if (discountApplied) {
        order.applyDiscount(discountFactor); // ✅ Apply the discount to the existing order
      }
      SqlQueries pool = new SqlQueries();
      return pool.placeOrder(order);

    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  /**
   * Chooses between eat here and takeaway.
   *
   * @param newOrder the order that is being placed.
   */
  public void selectOrderOption(Order newOrder) {

  }

  /**
   * Shows the summary of the order.
   */
  public void viewOrderSummary() {

  }

  /**
   * Cancels the current order.
   */
  public void abortOrder() {

  }

  /**
   * Proceeds to payment.
   */
  public void selectPayment() {

  }

  /**
   * Optionally link the order to a user account.
   */
  public void linkToAccount() {

  }

  /**
   * Browses the menu implementation.
   */
  @Override
  public void browseMenu() {

  }

  /**
   * Searches a product by name.
   */
  @Override
  public void searchProduct(String name) {

  }

}
