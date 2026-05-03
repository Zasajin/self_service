package org.example.orders;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.example.menu.OrderItem;
import org.example.menu.Product;

/**
 * Handles the ordering process and holds the carted items.
 */
public class Order {

  private int orderId;
  private int kioskId;
  private int customerId;
  private Timestamp orderDate;
  private double amountTotal;
  private String status;
  private ArrayList<OrderItem> items = new ArrayList<>();
  private double discountFactor = 1.0;
  private final List<Runnable> listeners = new ArrayList<>();

  /**
   * Constructor for orderes queried from the db.
   */
  public Order(int orderId, int kioskId, int customerId,
      Timestamp orderDate, double amountTotal, String status) {

    this.orderId = orderId;
    this.kioskId = kioskId;
    this.customerId = customerId;
    this.orderDate = orderDate;
    this.amountTotal = amountTotal;
    this.status = status;

  }

  /**
   * Empty constructor.
   */
  public Order() {
  }

  /**
   * Method to add a listener that gets later on notified,
   * about changes in the Order class.
   *
   * @param listener Listener that should follow changes of
   *                 the Order class
   */
  public void addListener(Runnable listener) {
    listeners.add(listener);
  }

  /**
   * Notifying when changing the total with the discount.
   */
  public void notifyListeners() {
    for (Runnable listener : listeners) {
      listener.run();
    }
  }

  /**
   * apply discount method.
   *
   * @param percentage percentage of the discount
   */
  public void applyDiscount(int percentage) {
    if (percentage < 0 || percentage > 100) {
      throw new IllegalArgumentException("Invalid discount percentage");
    }

    this.discountFactor = 1 - (percentage / 100.0);
    notifyListeners();
  }

  /**
   * Will calculate the overall cost of the order
   * by iterating thru the ArrayLists.
   */
  public double calculatePrice() {

    // Get items and their quantities
    Product[] theItems = Cart.getInstance().getItems();
    int[] theQuantities = Cart.getInstance().getQuantity();

    // Get total price
    double total = 0.0;

    // Iterate through the lists to get item and corresponding quantity
    for (int index = 0; index < theItems.length; index++) {

      // Multiply price of current item by its' quantity
      // and add calculated price to total
      total += theItems[index].getPrice() * theQuantities[index];
    }

    return total * discountFactor;

  }

  /**
   * Will add a Meal to the corresponding ArrayList
   * Must call on calculatePrice() to update orders cost.
   */
  public void addMeal() {
  }

  /**
   * Will add a Single to the corresponding ArrayList
   * Must call on calculatePrice() to update orders cost.
   */
  public void addSingle() {
  }

  /**
   * Will remove a selected Item from its ArrayList
   * Must call on calculatePrice() to update orders cost.
   */
  public void removeItem() {
  }

  /**
   * Getter for the orders ID.
   */
  public int getOrderId() {

    return orderId;

  }

  /**
   * Getter for the kiosk ID.
   */
  public int getKioskId() {

    return kioskId;

  }

  /**
   * Getter for the customer ID.
   */
  public int getCustomerId() {

    return customerId;

  }

  /**
   * Getter for date of order.
   */
  public Timestamp getOrderDate() {

    return orderDate;

  }

  /**
   * Getter for the cost of the order.
   */
  public double getAmountTotal() {

    return amountTotal;

  }

  /**
   * Getter for the orders status.
   */
  public String getStatus() {

    return status;

  }

  /**
   * Getter for the products of the order.
   */
  public ArrayList<OrderItem> getProducts() {

    return items;

  }

  /**
   * Stringbuilder for a well readable display in OrderHistory.
   */
  public String getProductSummary() {

    return items.stream()
        .map(OrderItem::getSummary)
        .collect(Collectors.joining("\n"));

  }
}
