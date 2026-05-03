package org.example.menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.example.sql.SqlQueries;


/**
 * Abstract base class for all single items on the menu.
 */
public class Single extends Product {
  public List<Ingredient> ingredients;
  public List<Integer> quantity;
  private boolean modified;
  private boolean inMeal;
  private boolean needsIngredients;

  /**
   * This constructor is used to create instances of the Single class with the
   * specified name,
   * price, and an empty list of ingredients.
   */
  public Single(int id, String name, double price, Type type, String imgPath) {
    setId(id);
    setName(name);
    setPrice(price);
    this.ingredients = new ArrayList<>();
    this.quantity = new ArrayList<>();
    setType(type);
    setImagePath(imgPath);
    needsIngredients = true;
  }

  /**
   * This constructor initializes a Single object with the specified parameters.
   */
  public Single(
      int id,
      String name,
      double price,
      Type type,
      String imgPath,
      List<Ingredient> ingredients) {
    setId(id);
    setName(name);
    setPrice(price);
    this.ingredients = new ArrayList<>();
    this.quantity = new ArrayList<>();
    setType(type);
    setImagePath(imgPath);
    this.ingredients = ingredients;
    needsIngredients = true;
  }

  public void setModefied(boolean modified) {
    this.modified = modified;
  }

  public boolean getModified() {
    return modified;
  }

  /**
   * The method calculates the total cost by adding the base price to the cost of
   * ingredients.
   */
  public double recalc() {
    return getPrice() + ingredients.size() * 0.5f;
  }

  /**
   * adds an Ingredient to a list of ingredients.
   */
  public void addIngredient(Ingredient ingredient) {
    ingredients.add(ingredient);
  }

  /**
   * removes an ingredient from a list based on its ID.
   */
  public void removeIngredient(Ingredient ingredient) {
    ingredients.removeIf(i -> i.getId() == ingredient.getId());
  }

  /**
   * The function retrieves all singles data from a database table and returns a
   * list of objects.
   *
   * @throws SQLException if database error occurs
   */
  public List<Single> getAllSingles() throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.getAllSingles();

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Inserts this product into the database and sets the generated product ID.
   * Assumes category name maps to valid category in DB.
   */
  public void saveToDb() throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      pool.saveSingleToDb(this);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieving all options by type.
   *
   * @param type type of single food item
   * @return options by type
   * @throws SQLException error with sql
   */
  public List<Single> getOptionsByType(Type type) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.getOptionsByType(type);

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Retrieves a list of Singles filtered by type (string input).
   * Converts string input to a SingleType enum and hands it down to the
   * overloaded method.
   *
   * @param type name of the SingleType
   * @return list of Singles matching the input type
   * @throws SQLException             if database access error occurs
   * @throws IllegalArgumentException if type string doesn't match any SingleType
   */
  public List<Single> getOptionsByType(String type) throws SQLException {
    // Convert input string to uppercase and map it to the enum and hand it down
    return getOptionsByType(Type.valueOf(type.toUpperCase()));
  }

  /**
   * Retrieves list of Single food items from the database
   * that are priced under a specific price limit.
   *
   * @param priceLimit maximum price to filter the food items
   * @return list of Singles that are under the specified price
   * @throws SQLException if database access error occurs
   */
  public List<Single> getSinglesUnder(double priceLimit) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.getSinglesUnder(priceLimit);

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Lets you delete a single item from the database
   * by using the single's id.
   *
   * @param id   id of the product (single)
   * @throws SQLException if server issues arise
   */
  public void deleteSingleById(int id) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      pool.deleteSingleById(id);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Lets you reduce the product quantity inside
   * the database.
   *
   * @param id     id of the product (single)
   * @param amount new amount of this product
   * @throws SQLException if server issues arise
   */
  public void reduceProductQuantity(int id, int amount) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      pool.reduceProductQuantity(id, amount);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieves a list of Singles that belong to a specific category ID.
   * If the category name does not map to a valid SingleType enum, defaults to
   * SingleType.EXTRA.
   *
   * @param categoryId id of the category to filter by
   * @return list of Singles belonging to the filtered category
   * @throws SQLException if database access error occurs
   */
  public List<Single> getOptionsByCategoryId(int categoryId) throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      return pool.getOptionsByCategoryId(categoryId);

    } catch (SQLException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  /**
   * Method to set the ingredients to them in the database.
   */
  public void setIngredients() throws SQLException {
    System.out.println("setting ingredients");
    if (needsIngredients) {
      try {
        SqlQueries pool = new SqlQueries();
        pool.setIngredientsForSingle(this, needsIngredients);
        needsIngredients = false;

      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Overloading the equality operator.
   *
   * @param obj the other single
   * @return the equality value
   */
  @Override
  public boolean equals(Object obj) {
    
    //Single other = (Single) obj;
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Single other = (Single) obj;
    
    if (this.getId() != other.getId()) {
      return false;
    }
    if (this.quantity.size() != other.quantity.size()) {
      return false;
    }

    for (int i = 0; i < other.quantity.size(); i++) {
      if (!Objects.equals(this.quantity.get(i), other.quantity.get(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Method that checks if the product is in a meal.
   *
   * @return if the product is in the meal
   * @throws SQLException if databse error
   */
  public boolean isInMeal() throws SQLException {
    try {
      SqlQueries pool = new SqlQueries();
      this.inMeal = pool.isInMeal(this, inMeal);
      return this.inMeal;

    } catch (SQLException e) {
      e.printStackTrace();
      return this.inMeal;
    }
  }

  /*public List<Single> getOptionsByCategoryName(Connection conn,
                          String categoryName) throws SQLException {
      List<Single> options = new ArrayList<>();
      String sql = "SELECT i.id, i.name, i.price, c.name AS category_name " +
      "FROM item i " +
      "JOIN category c ON i.category_id = c.category_id " +
      "WHERE c.name = ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, categoryName);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
          options.add(new Single(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getFloat("price"),
                rs.getString("category_name")
            ));
        }
        rs.close();
      }
      return options;
    }*/

  /**
   * The toString method.
   */
  public String toString() {
    return getName() + "    " + getPrice() + "kr";
  }
}
