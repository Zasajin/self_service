package org.example.orders;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import org.example.menu.Meal;
import org.example.menu.Product;
import org.example.menu.Type;
import org.example.sql.SqlQueries;

/**
 * The cart class implemented as a singleton.
 * That it can be accessed anywhere without
 * making multiple instances.
 */
public class Cart {

  private static Cart instance;
  private ArrayList<Product> items;
  private ArrayList<Integer> quantity;
  private ArrayList<Runnable> allListeners;

  // Private constructor to prevent instantiation from outside
  private Cart() {
    items = new ArrayList<>();
    quantity = new ArrayList<>();
    allListeners = new ArrayList<>();
  }

  /**
   * Method to get the single instance of the Cart.
   *
   * @return the Cart instance
   */
  public static Cart getInstance() {
    if (instance == null) {
      instance = new Cart();
    }
    return instance;
  }

  /**
   * Method to get an array of the items.
   *
   * @return the array of the items
   */
  public Product[] getItems() {
    Product[] newItems = new Product[items.size()];
    for (int i = 0; i < items.size(); i++) {
      newItems[i] = items.get(i);
    }
    return newItems;
  }

  /**
   * Method to get an array of the quantitys.
   *
   * @return array of quantitys
   */
  public int[] getQuantity() {
    int[] newQuantity = new int[quantity.size()];
    for (int i = 0; i < quantity.size(); i++) {
      newQuantity[i] = quantity.get(i);
    }
    return newQuantity;
  }

  /**
   * Method to add a product to the cart.
   *
   * @param product the product to add
   */
  public void addProduct(Product product) {
    if (items.contains(product)) {
      int index = items.indexOf(product);
      quantity.set(index, (quantity.get(index) + 1));
    } else {
      items.add(product);
      quantity.add(1);
    }
    notifyAllListeners();
  }

  /**
   * Method to remove a product from the cart.
   *
   * @param product the product to remove
   */
  public void removeProduct(Product product) {
    if (items.contains(product)) {
      int index = items.indexOf(product);
      int currentQuantity = quantity.get(index);

      if (currentQuantity > 1) {

        quantity.set(index, currentQuantity - 1);
      } else {
        items.remove(index);
        quantity.remove(index);
      }
      notifyAllListeners();
    }
  }

  /**
   * to save quantity to database.
   *
   * @param orderId order id from database
   * @throws SQLException database error
   */
  public void saveQuantityToDb(int orderId) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      pool.saveQuantityToDb(orderId, items, quantity);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Turning the cart into a string.
   *
   * @param orderId the order id
   * @return the string
   */
  public String printCart(int orderId) {
    String output = "\n\n";
    output = output + "ID: " + orderId + "\n\n";
    output = output + "Total: " + getTotalPrice() + "kr\n\n";

    for (int i = 0; i < items.size(); i++) {
      output = output + "x" + quantity.get(i) + "  " + items.get(i) + "\n\n";
    }
    return output;
  }

  /**
   * Empties all items and quantities from the cart.
   */
  public void clearCart() {
    items.clear();
    quantity.clear();
    notifyAllListeners();
  }

  /**
   * Add a listener for later notifications when the
   * cart updates.
   * Since cart is a singleton and other classes aren't or
   * things like scene components, that cannot be made into
   * a singleton at all, it is simpler to notify all other
   * listeners by adding them here.
   * This way, you can use the cart immediately to notify
   * other liseners, instead of using/making extra instances
   * to notify other liseners separately, and prevent
   * possible complications with those instances.
   *
   * @param listener operation from another class that should
   *                 listen to changes in cart
   */
  public void addListener(Runnable listener) {
    allListeners.add(listener);
  }

  /**
   * Notifying all listeners in the private array when
   * a change in the cart occurs.
   * For now, only used inside the class but left public
   * for possible later improvements.
   */
  public void notifyAllListeners() {
    for (Runnable eachListener : allListeners) {
      eachListener.run();
    }
  }

  /**
   * Converting meals into singles for storing them in the database.
   */
  public void convertMealsIntoSingles() {
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i) instanceof Meal) {
        Meal item = (Meal) items.get(i);
        items.remove(i);
        int quant = quantity.get(i);
        quantity.remove(i);
        items.add(item.getMain());
        quantity.add(quant);
        items.add(item.getSide());
        quantity.add(quant);
        items.add(item.getDrink());
        quantity.add(quant);
      }
    }
  }

  /**
   * Method to calculate the estimated preparation time of the customer's
   * order.
   * There are two different ways of calculations:
   * DAYTIME: A full staff team is present and therefore, each different
   * Type of item can be made simultaniously.
   * NIGHTTIME: Barely any staff is currently working (worst case:
   * 1 worker in the kitchen). Therefore, all items are made
   * individually.
   * The meals get converted into singles after the confirm order
   * button is pressed. Hence, there is no meal time calculation
   * needed.
   *
   * @return total estimated preparation time of the order
   */
  public int getEstimateTime() {

    // Set preparation times for products (current times from database)
    try {
      SqlQueries pool = new SqlQueries();
      pool.setProductPrepTime(items);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Set starting values for each type-specific preparation time
    int timeBurgers = 0;
    int timeSides = 0;
    int timeDrinks = 0;
    int timeExtra = 0;
    int timeDesserts = 0;
    ArrayList<Integer> allTimes = new ArrayList<>();

    // Get time to make proper calculation
    LocalTime currenTime = LocalTime.now();
    // Daytime shift start 07:00
    LocalTime dayTime = LocalTime.of(7, 0);
    // Nighttime shift start 22:00
    LocalTime nightTime = LocalTime.of(22, 0);

    // DAYTIME calculation - time between 07:00 and 22:00
    if (currenTime.isBefore(nightTime) && currenTime.isAfter(dayTime)) {

      return dayTimeCalculation(allTimes, timeBurgers, timeSides,
          timeDrinks, timeExtra, timeDesserts);

      // NIGHTTIME calculation
    } else {

      return nightTimeCalculation(allTimes, timeBurgers, timeSides,
          timeDrinks, timeExtra, timeDesserts);
    }
  }

  // Helper method for daytime calculation
  private int dayTimeCalculation(
      ArrayList<Integer> allTimes, int timeBurgers, int timeSides,
      int timeDrinks, int timeExtra, int timeDesserts) {

    // Go through all products
    for (Product product : items) {

      // Adding prep times depending on their type
      if (product.getType() == Type.BURGERS) {
        timeBurgers += product.getPreparationTime();

      } else if (product.getType() == Type.SIDES) {
        timeSides += product.getPreparationTime();

      } else if (product.getType() == Type.DRINKS) {
        timeDrinks += product.getPreparationTime();

      } else if (product.getType() == Type.EXTRA) {
        timeExtra += product.getPreparationTime();

      } else if (product.getType() == Type.DESSERTS) {
        timeDesserts += product.getPreparationTime();
      }
    }

    // Add all times to array
    allTimes.add(timeBurgers);
    allTimes.add(timeSides);
    allTimes.add(timeDrinks);
    allTimes.add(timeExtra);
    allTimes.add(timeDesserts);

    // Sort times from small to big
    Collections.sort(allTimes);

    return allTimes.get(allTimes.size() - 1);
  }

  // Helper method for nighttime calculation
  private int nightTimeCalculation(
      ArrayList<Integer> allTimes, int timeBurgers, int timeSides,
      int timeDrinks, int timeExtra, int timeDesserts) {

    int totalTime = 0;
    for (Product product : items) {
      totalTime += product.getPreparationTime();
    }
    return totalTime;
  }

  /**
   * Method to get the total Price.
   *
   * @return the totalprice.
   */
  public double getTotalPrice() {
    double total = 0;
    for (int i = 0; i < items.size(); i++) {
      double price = items.get(i).getPrice();
      int quant = quantity.get(i);
      total = total + (price * quant);
    }
    return total;
  }

  public boolean isEmpty() {
    return getItems().length == 0;
  }

}
