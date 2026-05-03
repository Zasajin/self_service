package org.example.users;

import com.mysql.cj.x.protobuf.MysqlxCrud.Order;
import java.util.ArrayList;

/**
 * Represents an admin with privileges.
 */
public class Admin implements User {

  // These should be changed to private.
  public String password;
  public String username;

  /**
   * Updates the menu logic.
   */
  public void updateMenu() {

  }

  /**
   * Sets the special items in the menu.
   */
  public void setSpecials() {

  }

  /**
   * returns the Salese summary.
   */
  public ArrayList<Order> viewSalesSummary() {
    return new ArrayList<>();
  }

  /**
   * Sets the timer for the terminal.
   */
  public void setTerminalTimer() {

  }

  /**
   * shows all previous orders.
   */
  public ArrayList<Order> viewOrderHistory() {
    return new ArrayList<>();
  }

  /**
   * Modifies available discounts.
   */
  public void editDiscounts() {

  }

  /**
   * Admins version of browsing.
   */
  @Override
  public void browseMenu() {

  }

  /**
   * Searches for editing and analyzing.
   */
  @Override
  public void searchProduct(String name) {

  }
}
