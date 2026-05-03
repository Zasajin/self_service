package org.example.menu;

/**
 * Class for querying prev orders content.
 */
public class OrderItem {

  private Product product;
  private int quantity;
  private double unitPrice;
  private double totalPrice;

  /**
   * Constructor.
   *
   * @param product   the item is
   * @param quantity  of that product in the order
   * @param unitPrice of the product
   */
  public OrderItem(Product product, int quantity, double unitPrice) {

    this.product = product;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
    this.totalPrice = unitPrice * quantity;

  }

  /**
   * Getter for the product name.
   */
  public Product getProduct() {

    return product;

  }

  /**
   * Getter for the product quantity.
   */
  public int getQuantity() {

    return quantity;

  }

  /**
   * Getter for the product unit price.
   */
  public double getUnitPrice() {

    return unitPrice;

  }

  /**
   * Getter for the product quantity price.
   */
  public double getTotalPrice() {

    return totalPrice;

  }

  /**
   * Getter for a nice String representation in the order history.
   * Usable in the future for printing receipts
   */
  public String getSummary() {

    return quantity + "x " + product.getName() + " @ " + unitPrice + " = " + totalPrice + " SEK";
  }

}
