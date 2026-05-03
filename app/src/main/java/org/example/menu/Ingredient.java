package org.example.menu;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.example.sql.SqlQueries;

/**
 * The class represents an ingredient with properties like id and name,
 * and provides methods to save ingredients to a database and
 * retrieve all ingredients from the database.
 */
public class Ingredient {
  private int id;
  private String name;

  /**
   * constructor class is initializing an ingredient
   * object with the provided id and name values.
   *
   * @param id   ingredient id
   * @param name ingredient name
   */
  public Ingredient(int id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * returns the value of the id.
   */
  public int getId() {
    return id;
  }

  /**
   * the value of the name.
   */
  public String getName() {
    return name;
  }

  /**
   * To string method for the name of the ingredient.
   */
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Ingredient ingredient = (Ingredient) obj;
    return getName() != null
        ? getName().equals(ingredient.getName())
        : ingredient.getName() == null;
  }

  @Override
  public int hashCode() {
    return getName() != null ? getName().hashCode() : 0;
  }

  /**
   * The function inserts an ingredient name
   * into a database table and retrieves the generated ID from the database.
   */
  public void saveToDb() throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      this.id = pool.saveToDb(this);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * The method is responsible for retrieving all ingredients from the database
   * table in a list.
   */
  public List<Ingredient> getAllIngredients() throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.getAllIngredients();

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Searching ingredients by name.
   *
   * @param name name of the desired ingredient
   * @return List containing all ingredients that match the input string name
   * @throws SQLException SQL errors
   */
  public List<Ingredient> searchIngredientsByName(String name) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.searchIngredientsByName(name);

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Serching ingredients by price.
   *
   * @param maxPrice total maximum price that is searched for
   * @return List containing all ingredients that are below input price
   * @throws SQLException SQL error
   */
  public List<Ingredient> searchIngredientsByPrice(double maxPrice) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.searchIngredientsByPrice(maxPrice);

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Searching ingredients by name and price.
   *
   * @param name     string name of the desired ingredient
   * @param maxPrice maximum price value that is searched for
   * @return List containing all ingredients that fall below input max price and
   *         match input name
   * @throws SQLException SQL error
   */
  public List<Ingredient> searchIngredientByNameAndPrice(
      String name, double maxPrice) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.searchIngredientByNameAndPrice(name, maxPrice);

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }
}