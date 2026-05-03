package org.example.orders;

import java.sql.Timestamp;

/**
 * Class for inhouse orders.
 */
public class InHouse extends Order {

  public int vat = 25;

  /**
   * Empty constructor, reason -> see Order.java.
   */
  public InHouse(int orderId, int kioskId, int customerId,
      Timestamp orderDate, double amountTotal, String status) {

    super(orderId, kioskId, customerId, orderDate, amountTotal, status);

  }

  /**
   * Overrides Order.java method as to include the vat in the calculation.
   */
  public double calculatePrize(int vat) {

    return 0.00;

  }

}
