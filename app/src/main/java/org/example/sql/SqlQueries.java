package org.example.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.menu.Drink;
import org.example.menu.Ingredient;
import org.example.menu.Meal;
import org.example.menu.OrderItem;
import org.example.menu.Product;
import org.example.menu.Side;
import org.example.menu.Single;
import org.example.menu.Type;
import org.example.orders.Order;

/**
 * Class for all queries related to the DB.
 */
public class SqlQueries {

  /**
   * Order query method.
   *
   * @return history arraylist.
   * @throws SQLException error handling.
   */
  public ArrayList<Order> queryOrders() throws SQLException {

    // ArrayList to hold all orders queried from the db
    ArrayList<Order> history = new ArrayList<>();

    String querySql = "SELECT order_ID, kiosk_ID, customer_ID, order_date, amount_total, status "
        + "FROM `order`";

    try (Connection connection = DatabaseManager.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement(querySql);
      ResultSet results = stmt.executeQuery();

      // Creates Orders from queried data
      while (results.next()) {
        int orderId = results.getInt("order_ID");
        int kioskId = results.getInt("kiosk_ID");
        int customerId = results.getInt("customer_ID");
        Timestamp orderDate = results.getTimestamp("order_date");
        double amountTotal = results.getDouble("amount_total");
        String status = results.getString("status");

        Order order = new Order(orderId, kioskId, customerId, orderDate, amountTotal, status);
        history.add(order);
      }
    }
    return history;
  }

  /**
   * Query for each product belonging to which order.
   *
   * @param orders orders.
   * @throws SQLException error handling.
   */
  public void queryOrderItemsFor(ArrayList<Order> orders) throws SQLException {

    String itemQuery = "SELECT oi.order_id, oi.product_id, p.name, p.price, oi.quantity "
        + "FROM order_item oi "
        + "JOIN product p ON oi.product_id = p.product_id";

    try (Connection connection = DatabaseManager.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement(itemQuery);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int orderId = rs.getInt("order_id");
        int productId = rs.getInt("product_id");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        int quantity = rs.getInt("quantity");

        for (Order order : orders) {

          if (order.getOrderId() == orderId) {
            Product product = new Product() {
            };
            product.setId(productId);
            product.setName(name);
            product.setPrice(price);

            OrderItem orderItem = new OrderItem(product, quantity, price);

            order.getProducts().add(orderItem);

            break;
          }
        }
      }
    }
  }

  /**
   * Query for getting all categories.
   *
   * @return The categories.
   * @throws SQLException error handling.
   */

  public Map<String, Integer> getAllCategories() throws SQLException {
    Map<String, Integer> categoryMap = new HashMap<>();
    String sql = "SELECT category_id, name FROM category";

    try (Connection connection = DatabaseManager.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("category_id");
        String name = rs.getString("name");
        categoryMap.put(name, id);
      }
    }
    return categoryMap;
  }

  /**
   * Query for getting all ingredients by category.
   *
   * @param categoryName name of the category.
   * @return a result set for all ingredients.
   * @throws SQLException error handling.
   */
  public List<String> getIngredientsByCategory(String categoryName) throws SQLException {
    List<String> ingredients = new ArrayList<>();
    String sql = "SELECT i.ingredient_id, i.ingredient_name "
        + "FROM ingredient i "
        + "JOIN categoryingredients ci ON i.ingredient_id = ci.ingredient_id "
        + "JOIN category c ON ci.category_id = c.category_id "
        + "WHERE c.name = ?";
    try (Connection connection = DatabaseManager.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, categoryName);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          ingredients.add(rs.getString("ingredient_name"));
        }
      }
    }
    return ingredients;
  }

  /**
   * Query for adding product into database.
   *
   * @param name        name of product
   * @param description description
   * @param price       price
   * @param categoryId  category ID
   * @param isActive    boolean
   * @param isLimited   boolean
   * @param imageUrl    image url.
   * @throws SQLException error handling.
   */
  public int addProduct(String name, String description, double price, int categoryId,
      byte isActive, byte isLimited, String imageUrl) throws SQLException {

    String sql = "INSERT INTO product (name, description, price, category_id, "
        + "is_active, is_limited, image_url) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = DatabaseManager.getConnection();
        PreparedStatement stmt = connection
            .prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

      stmt.setString(1, name);
      stmt.setString(2, description);
      stmt.setDouble(3, price);
      stmt.setInt(4, categoryId);
      stmt.setByte(5, isActive);
      stmt.setByte(6, isLimited);
      stmt.setString(7, imageUrl);

      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Creating product failed, no rows affected.");
      }

      try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1);
        } else {
          throw new SQLException("Creating product failed, no ID obtained.");
        }
      }
    }
  }

  /**
   * Query for adding products ingredients.
   *
   * @param productId   product ID
   * @param ingredients ingredients of the product
   * @throws SQLException error handling.
   */
  public void addProductIngredients(int productId, Map<String, Integer> ingredients)
      throws SQLException {
    String sql = "INSERT INTO productingredients (product_id, ingredient_id, ingredientCount) "
        + "VALUES (?, (SELECT ingredient_id FROM ingredient WHERE ingredient_name = ?), ?)";

    Connection connection = null;
    try {
      connection = DatabaseManager.getConnection();
      connection.setAutoCommit(false);

      try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
          stmt.setInt(1, productId);
          stmt.setString(2, entry.getKey());
          stmt.setInt(3, entry.getValue());
          stmt.addBatch();
        }

        stmt.executeBatch();
      }

      connection.commit();
    } catch (SQLException e) {
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException rollbackEx) {
          // log rollback failure
          rollbackEx.printStackTrace();
        }
      }
      throw e;
    } finally {
      if (connection != null) {
        try {
          connection.setAutoCommit(true);
          connection.close();
        } catch (SQLException ex) {
          // log closing failure
          ex.printStackTrace();
        }
      }
    }
  }

  /**
   * Retrieves all Single products from the database.
   *
   * @return list of Single products
   * @throws SQLException if database access error occurs
   */
  public List<Single> getAllSingles() throws SQLException {
    List<Single> list = new ArrayList<>();
    String sql = "SELECT product_id, name, price, image_url FROM product";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        list.add(new Single(
            rs.getInt("product_id"),
            rs.getString("name"),
            rs.getDouble("price"),
            Type.EXTRA, // Default type, can be modified as needed
            rs.getString("image_url")));
      }
    }
    return list;
  }

  /**
   * Saves a Single product to the database.
   *
   * @param single the Single product to save
   * @throws SQLException if database access error occurs
   */
  public void saveSingleToDb(Single single) throws SQLException {
    String sql = "INSERT INTO product "
        + "(name, price, category_id, image_url) VALUES (?, ?, ?, ?)";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql,
            PreparedStatement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, single.getName());
      stmt.setDouble(2, single.getPrice());
      stmt.setInt(3, getCategoryIdForType(single.getType())); // Helper method needed
      stmt.setString(4, single.getImagePath());
      stmt.executeUpdate();

      try (ResultSet rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          single.setId(rs.getInt(1));
        }
      }
    }
  }

  /**
   * Retrieves Single products by their type.
   *
   * @param type the type of Single products to retrieve
   * @return list of Single products matching the type
   * @throws SQLException if database access error occurs
   */
  public List<Single> getOptionsByType(Type type) throws SQLException {

    List<Single> options = new ArrayList<>();

    String sql = "SELECT p.product_id AS id, p.name, p.price, p.image_url, c.name AS type "
        + "FROM product p "
        + "JOIN category c ON p.category_id = c.category_id "
        + "WHERE c.name = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, type.name());

      try (ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {

          options.add(new Single(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getDouble("price"),
              Type.valueOf(rs.getString("type").toUpperCase()),
              rs.getString("image_url")));
        }
      }
    }
    return options;
  }

  /**
   * Retrieves Single products by type name.
   *
   * @param typeName the name of the type
   * @return list of Single products matching the type
   * @throws SQLException if database access error occurs
   */
  public List<Single> getOptionsByTypeName(String typeName) throws SQLException {
    return getOptionsByType(Type.valueOf(typeName.toUpperCase()));
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

    // list to store resulting Singles in
    List<Single> list = new ArrayList<>();

    // SQL query to select all specified singles
    String sql = "SELECT p.product_id AS id, p.name, p.price, p.image_url, c.name AS type "
        + "FROM product p "
        + "JOIN category c ON p.category_id = c.category_id "
        + "WHERE p.price < ?";

    // Prepare SQL statement with current connection
    // Try with this statement ensures the statement is closed automatically
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      // Bind priceLimit to SQL query
      ps.setDouble(1, priceLimit);

      // Execute query to retrieve wanted singles
      try (ResultSet rs = ps.executeQuery()) {

        // Iterate over result set and construct Single objects from each row
        while (rs.next()) {
          list.add(new Single(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getFloat("price"),
              Type.valueOf(rs.getString("type").toUpperCase()),
              rs.getString("image_url")));
        }
      }
    }
    return list;
  }

  /**
   * Deletes a Single product by its ID.
   *
   * @param id the ID of the product to delete
   * @throws SQLException if database access error occurs
   */
  public void deleteSingleById(int id) throws SQLException {
    String sql = "DELETE FROM product WHERE product_id = ?";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }

  /**
   * Reduces the quantity of a product.
   *
   * @param id     the product ID
   * @param amount the amount to reduce
   * @throws SQLException if database access error occurs
   */
  public void reduceProductQuantity(int id, int amount) throws SQLException {

    String sql = "UPDATE product SET quantity = quantity - ? "
        + "WHERE product_id = ? AND quantity >= ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, amount);
      stmt.setInt(2, id);
      stmt.setInt(3, amount);
      stmt.executeUpdate();
    }
  }

  /**
   * Retrieves Single products by category ID.
   *
   * @param categoryId the category ID
   * @return list of Single products in the category
   * @throws SQLException if database access error occurs
   */
  public List<Single> getOptionsByCategoryId(int categoryId) throws SQLException {
    List<Single> options = new ArrayList<>();

    String sql = "SELECT p.product_id AS id, p.name, p.price, p.image_url, c.name AS type "
        + "FROM product p "
        + "JOIN category c ON p.category_id = c.category_id "
        + "WHERE p.category_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, categoryId);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          options.add(new Single(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getDouble("price"),
              Type.valueOf(rs.getString("type").toUpperCase()),
              rs.getString("image_url")));
        }
      }
    }
    return options;
  }

  /**
   * Sets the ingredients for a Single product.
   *
   * @param single the Single product to set ingredients for
   * @throws SQLException if database access error occurs
   */
  public void setIngredientsForSingle(Single single, boolean needsIngredients) throws SQLException {

    if (needsIngredients) {
      String sql = "SELECT pi.ingredient_id, pi.ingredientCount, i.ingredient_name "
          + "FROM productingredients pi "
          + "JOIN ingredient i ON pi.ingredient_id = i.ingredient_id "
          + "WHERE product_id = ?";

      try (Connection conn = DatabaseManager.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, single.getId());

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
          single.addIngredient(new Ingredient(
              rs.getInt("ingredient_id"),
              rs.getString("ingredient_name")));
          single.quantity.add(rs.getInt("ingredientCount"));
        }
        needsIngredients = false;
        rs.close();
      }
    }
  }

  /**
   * Method that checks if the product is in a meal.
   *
   * @return if the product is in the meal
   * @throws SQLException if databse error
   */
  public boolean isInMeal(Single single, boolean inMeal) throws SQLException {
    String sql = "SELECT meal_id FROM meal WHERE product_id = ?";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, single.getId());

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          inMeal = true;
          return inMeal;
        } else {
          inMeal = false;
          return inMeal;
        }
      }
    }
  }

  /**
   * Helper method to get category ID for a given type.
   *
   * @param type the product type
   * @return the category ID
   * @throws SQLException if database access error occurs
   */
  private int getCategoryIdForType(Type type) throws SQLException {
    String sql = "SELECT category_id FROM category WHERE name = ?";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, type.name());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1);
        }
      }
    }
    return -1; // or throw an exception if not found
  }

  /**
   * Query method to change the name of a product.
   * Used in product table getter method.
   *
   * @param newName   String new name of product
   * @param productId int product id that gets name-change
   * @throws SQLException Database error
   */
  public void updateProductName(
      String newName,
      int productId) throws SQLException {

    String sql = "UPDATE product "
        + "SET name = ? "
        + "WHERE product_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, newName);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }

  /**
   * Query method to change the description of a product.
   * Used in product table getter method.
   *
   * @param newDescription String new description of product
   * @param productId      int product id that gets new description
   * @throws SQLException Database error
   */
  public void updateProductDescription(
      String newDescription,
      int productId) throws SQLException {

    String sql = "UPDATE product "
        + "SET description = ? "
        + "WHERE product_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, newDescription);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }

  /**
   * Query method to update is_active value of a product.
   * Used in product table getter method.
   *
   * @param newActivityValue int new is_active value (1 or 0)
   * @param productId        int id of product that will be changed
   * @throws SQLException Database error
   */
  public void updateActivityValue(
      int newActivityValue,
      int productId) throws SQLException {

    String sql = "UPDATE product "
        + "SET is_active = ? "
        + "WHERE product_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, newActivityValue);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }

  /**
   * This method updates the price of a specific product in
   * the database.
   * This method is used in the update price section of the admin menu.
   *
   * @param newPrice  int new price of the product
   * @param productId int product id of product that will be updated
   * @throws SQLException database error
   */
  public void updateProductPrice(
      double newPrice,
      int productId) throws SQLException {

    String sql = "UPDATE product "
        + "SET price = ? "
        + "WHERE product_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setDouble(1, newPrice);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }

  /**
   * This method updates the preparation time of a specific product in
   * the database.
   * This method is used in the update price section of the admin menu.
   *
   * @param newTime   int new preparation time of the product
   * @param productId int product id of product that will be updated
   * @throws SQLException database error
   */
  public void updateProductPreptime(
      int newTime,
      int productId) throws SQLException {

    String sql = "UPDATE product "
        + "SET preparation_time = ? "
        + "WHERE product_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setDouble(1, newTime);
      stmt.setInt(2, productId);
      stmt.executeUpdate();
    }
  }

  /**
   * This method fetches all necessary product data from the
   * database and returns an array of products that contain
   * all that fetched data.
   * This method is used in the price update section of the
   * admin menu.
   *
   * @return array containing all products with data from database
   * @throws SQLException database error
   */
  public ArrayList<Product> fetchAllProductData() throws SQLException {

    // ArrayList to store product data
    ArrayList<Product> products = new ArrayList<>();

    // SQL query to fetch needed data from database
    String sql = "SELECT p.product_id, p.`name`, p.description, "
        + "c.`name` AS type, p.is_active, p.price, p.preparation_time "
        + "FROM product p "
        + "JOIN category c ON p.category_id = c.category_id";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();) {

      while (rs.next()) {

        // Fetch all product data from database
        int productId = rs.getInt("product_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        Type type = Type.valueOf(rs.getString("type").toUpperCase());
        int isActive = rs.getInt("is_active");
        double price = rs.getDouble("price");
        int prepTime = rs.getInt("preparation_time");

        // Make new product with all fetched database data
        Product product = new Product() {
        };
        product.setId(productId);
        product.setName(name);
        product.setType(type);
        product.setDescription(description);
        product.setActivity(isActive);
        product.setPrice(price);
        product.setPreparationTime(prepTime);

        // Add completed product to array
        products.add(product);
      }
    }
    return products;
  }

  /**
   * Setter for the product preparation times.
   * It iterates through the given product array list,
   * fetches the needed preparation times and sets them
   * for the current product in the list.
   *
   * @param products Array List of products who's prep time should be set
   * @throws SQLException Database error
   */
  public void setProductPrepTime(ArrayList<Product> products) throws SQLException {

    // SQL query to fetch needed data from database
    String sql = "SELECT preparation_time "
        + "FROM product "
        + "WHERE product_id = ?";

    // Build connection and prepare query
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      // Go through each product
      for (int index = 0; index < products.size(); index++) {

        // Set value of product id to current product's id
        stmt.setInt(1, products.get(index).getId());

        // Execute query with inserted value (product id)
        try (ResultSet rs = stmt.executeQuery()) {

          if (rs.next()) {

            // Fetch all product preparation times from database
            int prepTime = rs.getInt("preparation_time");

            // Set fetched prep time as time for product
            products.get(index).setPreparationTime(prepTime);
          }
        }
      }
    }
  }

  /**
   * The function inserts an ingredient name
   * into a database table and retrieves the generated ID from the database.
   */
  public int saveToDb(Ingredient ingredient) throws SQLException {

    int id = -1;

    String sql = "INSERT INTO ingredients (name) VALUES (?)";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();) {

      stmt.setString(1, ingredient.getName());
      stmt.executeUpdate();

      if (rs.next()) {
        id = rs.getInt(1); // setting ID from DB
      }
    }
    return id;
  }

  /**
   * The method is responsible for retrieving all ingredients from the database
   * table in a list.
   */
  public List<Ingredient> getAllIngredients() throws SQLException {

    List<Ingredient> list = new ArrayList<>();
    String sql = "SELECT ingredient_id AS id, ingredient_name AS name FROM ingredient";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();) {

      while (rs.next()) {
        list.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
      }
    }
    return list;
  }

  /**
   * Searching ingredients by name.
   *
   * @param name name of the desired ingredient
   * @return List containing all ingredients that match the input string name
   * @throws SQLException SQL errors
   */
  public List<Ingredient> searchIngredientsByName(String name) throws SQLException {

    List<Ingredient> list = new ArrayList<>();

    String sql = "SELECT ingredient_id AS id, ingredient_name AS name "
        + "FROM ingredient "
        + "WHERE LOWER(ingredient_name) LIKE ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, "%" + name.toLowerCase() + "%");
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        list.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
      }
    }
    return list;
  }

  /**
   * Serching ingredients by price.
   *
   * @param maxPrice total maximum price that is searched for
   * @return List containing all ingredients that are below input price
   * @throws SQLException SQL error
   */
  public List<Ingredient> searchIngredientsByPrice(double maxPrice) throws SQLException {

    List<Ingredient> list = new ArrayList<>();

    String sql = "SELECT DISTINCT i.ingredient_id AS id, i.ingredient_name AS name "
        + "FROM ingredient i "
        + "JOIN productingredients pi ON i.ingredient_id = pi.ingredient_id "
        + "JOIN product p ON pi.product_id = p.product_id "
        + "WHERE p.price < ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setDouble(1, maxPrice);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        list.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
      }
    }
    return list;
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

    List<Ingredient> list = new ArrayList<>();

    String sql = "SELECT DISTINCT i.ingredient_id AS id, i.ingredient_name AS name "
        + "FROM ingredient i "
        + "JOIN productingredients pi ON i.ingredient_id = pi.ingredient_id "
        + "JOIN product p ON pi.product_id = p.product_id "
        + "WHERE LOWER(i.ingredient_name) LIKE ? AND p.price <= ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, "%" + name.toLowerCase() + "%");
      stmt.setDouble(2, maxPrice);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        list.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
      }
    }
    return list;
  }

  /**
   * loads a meal object from the db using the given product id.
   * This method queries the meal table and finds a meal associated with the
   * specified product
   *
   * @param productId the product id of the product to find a corresponding meal
   *                  for
   * @return either the meal if its found or null if no meal is linked
   * @throws SQLException we get an exception if a db access error occurs or sql
   *                      is invalid
   */
  public Meal loadMealByProductId(int productId) throws SQLException {

    String sql = "SELECT meal_id, name, price, image_url FROM meal WHERE product_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, productId);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Meal meal = new Meal(rs.getString("name"));
          meal.setId(rs.getInt("meal_id"));
          meal.setPrice(rs.getFloat("price"));
          meal.setImagePath(rs.getString("image_url"));
          meal.setType(Type.MEAL);
          return meal;
        }
      }
    }
    return null;
  }

  /**
   * Creates a new order.
   *
   * @param discountApplied true if a discount was applied
   *                        false if no discount was applied
   * @param discountFactor  integer value of how much of a discount the customer
   *                        gets
   * @return newly generated order id from the database (auto increment)
   * @throws SQLException sql errors
   */
  public int placeOrder(Order order) throws SQLException {
    // Calculate total order price

    double price = order.calculatePrice();

    // SQL Query as string statement
    String s = "INSERT INTO `order` "
        + "(kiosk_ID, customer_ID, order_date, amount_total, status)"
        + "VALUES (123, 1, NOW(), ?, 'pending')";

    // Prepare statement to be actual query
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(s);) {
      // Insert price variable
      ps.setDouble(1, price);
      System.out.println("Saving order with discounted total: " + price);
      // Execute query
      ps.executeUpdate();

      // immediately fetch the auto generated order id
      return receiveOrderId(conn);
    }
  }

  /**
   * Helper method.
   * Generates or shows the order ID.
   * This has to be used right after order creation.
   * To not get false IDs on wrong usage, this is private
   * and used in the place order method only.
   *
   * @param conn The current connection, that you are on
   *             (it HAS to be current connection!)
   * @throws SQLException SQL Database errors
   */
  private int receiveOrderId(Connection conn) throws SQLException {
    // Set default order id
    int id = -1;

    // SQL Query as string statement
    String s = "SELECT LAST_INSERT_ID()";

    // Prepare statement to be actual query
    // Using try to save ressources and close process automatically
    try (PreparedStatement ps = conn.prepareStatement(s)) {

      // Open result set to fetch order id (execute query)
      ResultSet rs = ps.executeQuery();

      // if there is a result
      if (rs.next()) {
        // store result as order id integer
        id = rs.getInt(1);
      }
    }
    return id;
  }

  /**
   * to save quantity to database.
   *
   * @param orderId order id from database
   * @throws SQLException database error
   */
  public void saveQuantityToDb(int orderId, ArrayList<Product> items,
      ArrayList<Integer> quantity) throws SQLException {

    for (int i = 0; i < items.size(); i++) {

      String s = "INSERT INTO order_item "
          + "(order_id, product_id, quantity)"
          + "VALUES (?, ?, ?)";

      // Prepare statement to be actual query
      try (Connection conn = DatabaseManager.getConnection();
          PreparedStatement ps = conn.prepareStatement(s);) {

        // Get product ID from the item
        int productId = items.get(i).getId();

        // Insert values into prepared statement
        ps.setInt(1, orderId);
        ps.setInt(2, productId);
        ps.setInt(3, quantity.get(i));

        // Execute query
        ps.executeUpdate();
        int orderItemid = receiveOrderId(conn);

        if (items.get(i) instanceof Single) {
          List<Ingredient> ingrediets = ((Single) items.get(i)).ingredients;
          List<Integer> quantitys = ((Single) items.get(i)).quantity;

          for (int j = 0; j < ingrediets.size(); j++) {
            String query = "INSERT INTO orderitemingredients "
                + "(order_item_id, ingredient_id, ingredientCount)"
                + "VALUES (?, ?, ?)";

            PreparedStatement ps2 = conn.prepareStatement(query);
            ps2.setInt(1, orderItemid);
            ps2.setInt(2, ingrediets.get(j).getId());
            ps2.setInt(3, quantitys.get(j)); // TODO: error index out of bounds
            // Error cause if 2 same burgers with different ingredients

            ps2.executeUpdate();
          }
        } else {
          System.out.println("Item is not an instance of Single: " + items.get(i));
        }
      }
    }
  }

  /**
   * Method used for meal customization screen.
   * Retrieves all side options for a specific meal.
   *
   * @param mealId int id of the specific meal
   * @return list of side options from this meal
   */
  public List<Product> getSideOptionsForMeal(int mealId) {
    List<Product> sideOptions = new ArrayList<>();
    String sql = """
        SELECT p.product_id, p.name, p.price, p.image_url
        FROM meal_sideoptions mso
        JOIN product p ON mso.product_id = p.product_id
        WHERE mso.meal_id = ?
        """;
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, mealId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Side side = new Side(
              rs.getInt("product_id"),
              rs.getString("name"),
              rs.getFloat("price"),
              Type.SIDES,
              rs.getString("image_url"));
          sideOptions.add(side);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return sideOptions;
  }

  /**
   * Method used for meal customization screen.
   * Retrieves all drink options for a specific meal.
   *
   * @param mealId int id of the specific meal
   * @return list of drink options from this meal
   */
  public List<Product> getDrinkOptionsForMeal(int mealId) {
    List<Product> drinkOptions = new ArrayList<>();
    String sql = """
        SELECT p.product_id, p.name, p.price, p.image_url
        FROM meal_drinkoptions mdo
        JOIN product p ON mdo.product_id = p.product_id
        WHERE mdo.meal_id = ?
        """;
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, mealId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Drink drink = new Drink(
              rs.getInt("product_id"),
              rs.getString("name"),
              rs.getFloat("price"),
              Type.DRINKS,
              rs.getString("image_url"));
          drinkOptions.add(drink);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return drinkOptions;
  }

  /**
   * Method to set the main of the Meal.
   *
   * @throws SQLException if database fails
   */
  public void setMainOfDb(Meal meal) throws SQLException {

    String sql = "SELECT product_id FROM meal WHERE meal_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);) {

      ps.setInt(1, meal.getId());
      ResultSet rs = ps.executeQuery();
      int productId = 0;
      while (rs.next()) {
        productId = rs.getInt("product_id");
      }
      ps.close();
      rs.close();

      String sql2 = "SELECT name, price, image_url FROM product WHERE product_id = ?";

      PreparedStatement stmt = conn.prepareStatement(sql2);
      stmt.setInt(1, productId);
      ResultSet rs2 = stmt.executeQuery();

      while (rs2.next()) {
        meal.setMainDb(new Single(
            meal.getId(),
            rs2.getString("name"),
            rs2.getFloat("price"),
            Type.valueOf("MEAL"),
            rs2.getString("image_url")));
      }
      rs2.close();
      stmt.close();
      meal.getMain().setIngredients();
    }

    System.out.println(meal.getMain());
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
    List<Meal> list = new ArrayList<>();
    String sql = "SELECT id, name, total_price FROM meals WHERE total_price < ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);) {

      ps.setDouble(1, priceLimit);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        Meal m = new Meal(rs.getString("name"));
        m.setId(rs.getInt("id"));
        list.add(m);
      }
      ps.close();
      rs.close();
      return list;
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

    // list to store resulting Meals in
    List<Meal> list = new ArrayList<>();

    // SQL query to select all specified Meals
    String sql = "SELECT id, name, total_price FROM meals WHERE type = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);) {

      // Bind type to SQL query
      ps.setString(1, type);

      // Execute query to retrieve wanted Meals
      ResultSet rs = ps.executeQuery();

      // Iterate over result set and construct Meal objects from each row
      while (rs.next()) {
        Meal m = new Meal(rs.getString("name"));
        m.setId(rs.getInt("id"));
        list.add(m);
      }

      // Close result set and statement and return build list
      ps.close();
      rs.close();
      return list;
    }
  }

  /**
   * The method is responsible for retrieving all meals from a database.
   */
  public List<Meal> getAllMeals() throws SQLException {

    List<Meal> list = new ArrayList<>();

    // TODO: how change meals to match database???
    String sql = "SELECT id, name FROM meals";

    try (Connection conn = DatabaseManager.getConnection();
        Statement stmt = conn.createStatement();) {

      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        Meal meal = new Meal(rs.getString("name"));
        meal.setId(rs.getInt("id"));
        list.add(meal);
      }

      rs.close();
      stmt.close();

      return list;
    }
  }

  /**
   * Helper method that fetches all relevant info about a meal
   * from the database.
   *
   * @return list of all relevant meals
   * @throws SQLException database error
   */
  public List<Meal> fetchMealsFromDatabase() throws SQLException {
    List<Meal> meals = new ArrayList<>();

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "SELECT meal_id, name, price, image_url FROM meal");
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        Meal meal = new Meal(rs.getString("name"));
        meal.setId(rs.getInt("meal_id"));
        meal.setPrice(rs.getFloat("price"));
        meal.setImagePath(rs.getString("image_url"));
        meals.add(meal);
      }
    }
    return meals;
  }

  /**
   * Retrieves the description of a product by its name.
   *
   * @param name the name of the product
   * @return the product's description or null if not found
   * @throws SQLException if a database access error occurs
   */
  public String getDescriptionByName(String name) throws SQLException {
    String sql = "SELECT description FROM product WHERE name = ?";

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, name);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getString("description");
        }
      }
    }
    return null; // Not found
  }
}
