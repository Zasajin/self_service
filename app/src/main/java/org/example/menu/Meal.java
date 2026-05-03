package org.example.menu;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.example.sql.SqlQueries;

/**
 * The class represents a meal with a name, a list of items, and methods to add
 * items, calculate total price, and retrieve contents.
 */
public class Meal extends Product {
  private Single main;
  private Product side;
  private Product drink;

  /**
   * Constructs a meal with a given name.
   *
   * @param name the name of the meal
   */
  public Meal(String name) {
    setName(name);
  }

  public void setSide(Product side) {
    this.side = side;
  }

  public Product getSide() {
    return side;
  }

  public void setDrink(Product drink) {
    this.drink = drink;
  }

  public Product getDrink() {
    return drink;
  }

  public Single getMain() {
    return main;
  }

  /**
   * The method is responsible for retrieving all meals from a database.
   */
  public List<Meal> getAllMeals() throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.getAllMeals();

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Helper method for setMain.
   * Actual set main will be done with queries, so
   * this method should ONLY be used in SqlQueries.java!
   *
   * @param main new main of the meal
   */
  public void setMainDb(Single main) {
    this.main = main;
  }

  /**
   * Method to set the main of the Meal.
   *
   * @throws SQLException if database fails
   */
  public void setMain() throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      pool.setMainOfDb(this);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieves a list of meals from the database that are
   * under the input price limit.
   *
   * @param priceLimit maximum price for filtering meals
   * @return list of meals priced under the input limit
   * @throws SQLException if a database access error occurs
   */
  public List<Meal> getMealsUnder(double priceLimit) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.getMealsUnder(priceLimit);

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Retrieves a list of meals from the database.
   * The meals are filerted by the input meal type.
   *
   * @param type type of meal that is requested
   * @return list of meals of the given type
   * @throws SQLException if a database access error occurs
   */
  public List<Meal> getMealsbyType(String type) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.getMealsbyType(type);

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * The toString method.
   */
  public String toString() {
    String output = getName() + "    " + getPrice() + "kr";
    output = output + "\n      " + main.getName();
    output = output + "\n        " + side.getName();
    output = output + "\n        " + drink.getName();
    return output;
  }
}
