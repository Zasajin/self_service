package org.example.screens;

import java.sql.SQLException;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.buttons.SearchBar;
import org.example.kiosk.LanguageSetting;
import org.example.menu.Product;
import org.example.sql.SqlQueries;

/**
 * Updating menu class.
 */
public class UpdateMenuItems {

  private SqlQueries pool;

  /**
   * This is the update menu items constructor.
   * (Only used for assigning variables.)
   *
   * @throws SQLException sql errors
   */
  public UpdateMenuItems() throws SQLException {
    this.pool = new SqlQueries();
  }

  /**
   * Creates a scene for updating menu items in the admin interface.
   *
   * @param primaryStage The primary stage of the application.
   * @param prevScene    The previous scene to return to.
   * @return The scene for updating menu items.
   */
  public Scene adminUpdateMenuItems(Stage primaryStage, Scene prevScene) {

    // All the buttons for updating menu items
    MidButton addProductButton = makeMidButton("Add Product to Menu");
    MidButton editProductButton = makeMidButton("Edit Product Data");
    MidButton removeProductButton = makeMidButton("Remove Product from Menu");

    // Actions for all mid buttons

    // Action: Adding product
    addProductButton.setOnAction(e -> {

      // Get add product scene
      AddProductScene addProductScene = new AddProductScene(
          primaryStage,
          prevScene);
      // Set the new (current) scene
      primaryStage.setScene(addProductScene.getProductScene());
    });
    // Action: editing product
    editProductButton.setOnAction(e -> {

      // Get product editor scene
      SearchBar searchBar = new SearchBar();
      ProductEditorScene productEditor = new ProductEditorScene(
          primaryStage,
          prevScene,
          getProductTable(true, true, true, true, true),
          searchBar);

      // Set the new (current) scene
      primaryStage.setScene(productEditor.getProductEditorScene());
    });

    // Action: deleting product
    removeProductButton.setOnAction(e -> {

      // Get product deletion scene
      SearchBar searchBar = new SearchBar();
      DeleteProductScene productDeletionScene = new DeleteProductScene(
          primaryStage,
          prevScene,
          getProductTable(false, false, false, false, false),
          searchBar);

      // Set the new (current) scene
      primaryStage.setScene(productDeletionScene.getProductDeletionScene());
    });

    MidButton addMealButton = makeMidButton("Add a Meal");

    addMealButton.setOnAction(e -> {
      AddMealScene addMealScene = new AddMealScene(primaryStage, prevScene);

      primaryStage.setScene(addMealScene.getAddMealScene());
    });

    MidButton globalDiscountButton = makeMidButton("Apply Global Discounts");

    globalDiscountButton.setOnAction(e -> {
      GlobalDiscountScreen globalDiscountScreen = new GlobalDiscountScreen(
          primaryStage,
          prevScene);

      primaryStage.setScene(globalDiscountScreen.getGlobalDiscountScreen());
    });

    // MidButton globalDiscountButton = makeMidButton("Apply Global Discounts");

    globalDiscountButton.setOnAction(e -> {
      GlobalDiscountScreen globalDiscountScreen = new GlobalDiscountScreen(
          primaryStage,
          prevScene);

      primaryStage.setScene(globalDiscountScreen.getGlobalDiscountScreen());
    });

    MidButton addIngredientsButton = makeMidButton("Add Ingredients to DB");

    addIngredientsButton.setOnAction(e -> {
      AddIngredientsToDatabase addIngredientScreen = new AddIngredientsToDatabase(
          primaryStage,
          prevScene);
      primaryStage.setScene(addIngredientScreen.addIngredientToDb());
    });

    // Back button -> user goes to previous screen
    var backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> {
      primaryStage.setScene(prevScene);
    });

    // Language Button -> cycles images on click
    var langButton = new LangBtn();

    // Spacer for Bottom Row
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Bottom row of the screen
    HBox bottomContainer = new HBox(langButton, spacerBottom, backButton);
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);

    // Layout for arranging buttons in a grid
    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(20);
    gridPane.setVgap(20);
    gridPane.add(addProductButton, 0, 0);
    gridPane.add(editProductButton, 0, 1);
    gridPane.add(removeProductButton, 0, 2);
    gridPane.add(globalDiscountButton, 1, 0);
    gridPane.add(addIngredientsButton, 1, 1);
    gridPane.add(addMealButton, 1, 2);

    // Position the language button in the bottom-left corner
    StackPane.setAlignment(langButton, Pos.BOTTOM_LEFT);
    StackPane.setMargin(langButton, new Insets(0, 0, 30, 30));

    // Layout borders
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setCenter(gridPane);
    layout.setBottom(bottomContainer);

    // Translate button action
    langButton.addAction(event -> {
      LanguageSetting lang = LanguageSetting.getInstance();
      String newLang;
      if (lang.getSelectedLanguage().equals("en")) {
        newLang = "sv";
      } else {
        newLang = "en";
      }
      lang.changeLanguage(newLang);
      lang.translateLabels(layout);
    });

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(layout);
    lang.translateLabels(layout);

    // put everything into a stackpane
    StackPane layoutWithLangButton = new StackPane(layout, langButton);

    Scene updateItemScene = new Scene(layoutWithLangButton, 1920, 1080);

    return updateItemScene;
  }

  /**
   * Helper method to make the 3 update menu items
   * scene buttons.
   *
   * @param labelName label name of mid button
   * @return mid button
   */
  private MidButton makeMidButton(String labelName) {
    MidButton thisMidButton = new MidButton(
        labelName,
        "rgb(255, 255, 255)",
        30);
    return thisMidButton;
  }

  /**
   * This is a method to get a product table for
   * admin menu sections like price editor and product
   * deletion menu.
   *
   * @param priceEditable true if the price should be editable
   *                      false if the price should not be editable
   * @return a table filled with products and data about them, like
   *         product id, name, type, activity and price
   */
  private TableView<Product> getProductTable(
      boolean priceEditable,
      boolean descriptionEditable,
      boolean activityEditable,
      boolean nameEditable,
      boolean timeEditable) {

    // Product ID column
    TableColumn<Product, Integer> idColumn = new TableColumn<>("Product ID");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);

    // Product name column
    TableColumn<Product, String> nameColumn = new TableColumn<>("Product Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    // If product name is editable
    if (nameEditable) {
      nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

      // Name column of a product is clicked
      nameColumn.setOnEditCommit(event -> {

        // Get current value of clicked field and get newly entered value
        Product product = event.getRowValue();
        String newName = event.getNewValue().strip();

        // NO empty string was entered
        if (!(newName.strip().isEmpty())) {

          // Get id of local product
          int productId = product.getId();

          // Update product name locally
          product.setName(newName);

          try {
            // Update newly inserted activity value of product in database
            pool.updateProductName(newName, productId);

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
        // ELSE: empty string was entered -> do nothing
      });
    }

    // Product description column
    TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

    // If product description is editable
    if (descriptionEditable) {
      descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

      // Description column of a product is clicked
      descriptionColumn.setOnEditCommit(event -> {

        // Get current value of clicked field and get newly entered value
        Product product = event.getRowValue();
        String newDescription = event.getNewValue().strip();

        // Get id of local product
        int productId = product.getId();

        // Update product name locally
        product.setDescription(newDescription);

        try {
          // Update newly inserted activity value of product in database
          pool.updateProductDescription(newDescription, productId);

        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
    }

    // Product category column
    TableColumn<Product, String> categoryColumn = new TableColumn<>("Product Category");
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

    // Product activity column
    TableColumn<Product, Integer> activityColumn = new TableColumn<>("Product Active");
    activityColumn.setCellValueFactory(new PropertyValueFactory<>("activity"));
    activityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

    // If activity value is editable
    if (activityEditable) {
      activityColumn.setOnEditCommit(event -> {
        Product product = event.getRowValue();
        int newActivityValue = event.getNewValue();

        // NO invalid int value was entered
        if (newActivityValue == 1 || newActivityValue == 0) {

          // getting local product id
          int productId = product.getId();

          // Updating value locally
          product.setActivity(newActivityValue);

          try {
            // Update newly inserted activity value in database
            pool.updateActivityValue(newActivityValue, productId);

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
        // ELSE: invalid value was entered -> do nothing
      });
    }
    activityColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);

    // Product price column
    TableColumn<Product, Double> priceColumn = new TableColumn<>("Product Price");
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    // If product price is editable
    if (priceEditable) {
      priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
      priceColumn.setOnEditCommit(event -> {
        Product product = event.getRowValue();
        Double newPrice = event.getNewValue();
        int productId = product.getId();
        product.setPrice(newPrice);

        try {
          // update the newly inserted price in database
          pool.updateProductPrice(newPrice, productId);

        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
    }
    priceColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);

    // Product preparation time column
    TableColumn<Product, Integer> timeColumn = new TableColumn<>("Preparation Time");
    timeColumn.setCellValueFactory(new PropertyValueFactory<>("preparationTime"));

    // If product preparation time is editable
    if (timeEditable) {
      timeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
      timeColumn.setOnEditCommit(event -> {
        Product product = event.getRowValue();
        int newTime = event.getNewValue();
        int productId = product.getId();
        product.setPreparationTime(newTime);

        try {
          // update the newly inserted price in database
          pool.updateProductPreptime(newTime, productId);

        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
    }
    timeColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);

    // Creating Table
    TableView<Product> productTable = new TableView<>();
    productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    productTable.setMaxWidth(Double.MAX_VALUE);
    productTable.setPrefWidth(Region.USE_COMPUTED_SIZE);
    productTable.setEditable(true);

    // Combining columns in table
    productTable.getColumns().add(idColumn);
    productTable.getColumns().add(nameColumn);
    productTable.getColumns().add(descriptionColumn);
    productTable.getColumns().add(categoryColumn);
    productTable.getColumns().add(activityColumn);
    productTable.getColumns().add(priceColumn);
    productTable.getColumns().add(timeColumn);

    // Querys data into the table
    try {
      // Gets products with needed data
      ArrayList<Product> products = pool.fetchAllProductData();

      // Insert fetched data in table
      productTable.getItems().addAll(products);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return productTable;
  }
}