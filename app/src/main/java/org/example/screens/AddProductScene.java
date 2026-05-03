package org.example.screens;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.buttons.DropBoxWithLabel;
import org.example.buttons.LangBtn;
import org.example.buttons.RectangleTextFieldWithLabel;
import org.example.buttons.SqrBtnWithOutline;
import org.example.buttons.SquareButtonWithImg;
import org.example.buttons.TickBoxWithLabel;
import org.example.kiosk.LanguageSetting;
import org.example.sql.DatabaseManager;
import org.example.sql.SqlQueries;

/**
 * Scene in the admin menu for adding products to the menu.
 */
public class AddProductScene {

  private Stage primaryStage;
  private Scene prevScene;
  private ImageView imagePreview = new ImageView();
  private Label imageUrlLabel = new Label("No image selected");
  private String selectedImageUrl = "/food/default_burger.png"; // Default image
  private String relativeImagePath;

  /**
   * The add product scene constructor.
   * ONLY used in the admin menu in 'UpdateMenuItems.java'.
   *
   * @param primaryStage Primary stage for the scenes (stage itself)
   * @param prevScene    Previous scene to return to
   */
  public AddProductScene(Stage primaryStage, Scene prevScene) {
    this.primaryStage = primaryStage;
    this.prevScene = prevScene;
  }

  /**
   * Getter for add product scene.
   * Scene for adding a product to the menu.
   *
   * @return The add product scene.
   */
  public Scene getProductScene() {

    // List view box for showing all the ingredients upon category selection
    ListView<String> ingredientListView = new ListView<>();
    ingredientListView.setStyle("-fx-font-size: 20px;");
    ingredientListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    ingredientListView.setPrefSize(300, 400);

    Label ingredientListLabel = new Label("Ingredient List");
    ingredientListLabel.setAlignment(Pos.CENTER);
    ingredientListLabel.setStyle("-fx-font-size: 30");

    VBox ingredientBox = new VBox(10, ingredientListLabel, ingredientListView);
    ingredientBox.setAlignment(Pos.CENTER_LEFT);
    ingredientBox.setPadding(new Insets(0, 150, 50, 0));

    // Creates a dropdown for selecting a product category
    DropBoxWithLabel productCategoryDropBox = new DropBoxWithLabel("Product Category:");
    productCategoryDropBox.getComboBox().setStyle("-fx-font-size: 20px;");
    // Map to store category name and its corresponding ID from the database.
    Map<String, Integer> categoryMap = new HashMap<>();

    // This is a query to fetch all the categories from the database
    try (Connection connection = DatabaseManager.getConnection()) {
      String sql = "SELECT category_id, name FROM category";
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("category_id");
        String name = rs.getString("name");
        categoryMap.put(name, id);
        productCategoryDropBox.getComboBox().getItems().add(name);
      }

      // This is an event handler when a category is selected
      // When users selects a category, the ingredient list is updated
      productCategoryDropBox.getComboBox().setOnAction(e -> {
        // Gets selected category
        String selectedCategory = productCategoryDropBox.getSelectedItem();
        if (selectedCategory != null) {
          try {
            SqlQueries queries = new SqlQueries();
            List<String> ingredients = queries.getIngredientsByCategory(selectedCategory);
            ingredientListView.setItems(FXCollections.observableArrayList(ingredients));

          } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Database error", "Failed to load categories", Alert.AlertType.ERROR);
          }
        }
      });
    } catch (SQLException ex) {
      ex.printStackTrace();
      showAlert("Database error", "Failed to load categories", Alert.AlertType.ERROR);
    }

    SqrBtnWithOutline confirmButton = new SqrBtnWithOutline("Confirm",
        "green_tick.png", "rgb(81, 173, 86)");

    // Textfields for the information to put into the SQL query
    RectangleTextFieldWithLabel productName = new RectangleTextFieldWithLabel("Product Name:",
        "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel productDescription = new RectangleTextFieldWithLabel(
        "Product Description:", "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel productPrice = new RectangleTextFieldWithLabel("Product Price:",
        "rgb(255, 255, 255)");
    TickBoxWithLabel productIsActive = new TickBoxWithLabel("Is active?");
    TickBoxWithLabel productIsLimited = new TickBoxWithLabel("Is limited?");

    // Handler for when the confirm button is clicked, it adds that new product
    confirmButton.setOnAction(e -> {
      try (Connection connection = DatabaseManager.getConnection()) {
        connection.setAutoCommit(false);

        ObservableList<String> selectedItems = ingredientListView
            .getSelectionModel().getSelectedItems();

        String selectedCategory = productCategoryDropBox.getSelectedItem();

        // Validating user inputs first before attempting to add the product
        if (productName.getText().isEmpty()
            || productPrice.getText().isEmpty()
            || selectedCategory == null) {

          showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
          return;

        }
        if (selectedItems.isEmpty()) {
          showAlert("Info", "No ingredients selected!", Alert.AlertType.INFORMATION);
        }

        // Build an SQL query to insert a new product into the 'product table'
        String sqlProduct = "INSERT INTO"
            + " product (name, description, price, category_id,"
            + " is_active, is_limited, image_url)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmtProduct = connection
            .prepareStatement(sqlProduct, PreparedStatement.RETURN_GENERATED_KEYS);

        String imageUrl = relativeImagePath;

        byte isActive = (byte) (productIsActive.isSelected() ? 1 : 0);
        byte isLimited = (byte) (productIsLimited.isSelected() ? 1 : 0);

        stmtProduct.setString(1, productName.getText());
        stmtProduct.setString(2, productDescription.getText());

        // Must parse the string as a double
        stmtProduct.setDouble(3, Double.parseDouble(productPrice.getText()));
        stmtProduct.setInt(4, categoryMap.get(selectedCategory));
        stmtProduct.setByte(5, isActive);
        stmtProduct.setByte(6, isLimited);
        stmtProduct.setString(7, imageUrl);

        int affectedRows = stmtProduct.executeUpdate();

        // Notifying the admin if the adding proccess was successful or not
        if (affectedRows > 0) {
          showAlert("Success!", "Product Added Successfully!", Alert.AlertType.INFORMATION);
        }

        int productId;
        // Retrieving auto generated product ID to use for ingredient mapping
        try (ResultSet generatedKeys = stmtProduct.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            productId = generatedKeys.getInt(1);
          } else {
            throw new SQLException("Creating producted failed, no ID obtained");
          }
        }

        // Inserting selected ingredients into the linking table with the new product ID
        String sqlIngredients = "INSERT INTO productingredients (product_id, "
            + "ingredient_id, ingredientCount) "
            + "VALUES (?, (SELECT ingredient_id FROM "
            + "ingredient WHERE ingredient_name = ?), ?)";

        PreparedStatement stmtIngredients = connection
            .prepareStatement(sqlIngredients);

        int ingredientCount = 1;
        for (String ingredientName : selectedItems) {
          stmtIngredients.setInt(1, productId);
          stmtIngredients.setString(2, ingredientName);
          stmtIngredients.setInt(3, ingredientCount);
          stmtIngredients.addBatch();
          ingredientCount++;
        }

        stmtIngredients.executeBatch();
        connection.commit();

      } catch (NumberFormatException ex) {
        showAlert(
            "Input error",
            "Enter valid numbers for price and category ID",
            Alert.AlertType.ERROR);

      } catch (SQLException ex) {
        // Full error for debugging
        ex.printStackTrace();
        showAlert(
            "Database Error",
            "Failed to save product",
            Alert.AlertType.ERROR);
      }
    });

    SqrBtnWithOutline backButton = new SqrBtnWithOutline(
        "Cancel", "cancel.png", "rgb(255, 0, 0)");

    backButton.setOnAction(e -> {
      primaryStage.setScene(prevScene);
    });

    // The top part of the scene, the label name
    var menuLabel = new Label("Add a Product to the Menu");
    menuLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bold;");

    HBox menuTitle = new HBox(menuLabel);
    menuTitle.setAlignment(Pos.CENTER);
    menuTitle.setPadding(new Insets(70, 0, 20, 0));

    // Setting the name, description and price to left of the screen
    VBox menuLayoutLeft = new VBox(productName, productDescription, productPrice);
    menuLayoutLeft.setAlignment(Pos.BASELINE_LEFT);

    // Active and limited tick boxes are center but to the left and
    // category drop box is center to the right
    VBox activeLimitedBox = new VBox(20);
    activeLimitedBox.getChildren().addAll(productIsActive, productIsLimited);
    activeLimitedBox.setAlignment(Pos.CENTER_LEFT);

    VBox categoryIdBox = new VBox(productCategoryDropBox);
    categoryIdBox.setAlignment(Pos.CENTER_RIGHT);
    categoryIdBox.setPadding(new Insets(0, 0, 160, 0));

    // Adding them together and setting them to center of the border pane
    HBox menuLayoutCenter = new HBox(activeLimitedBox, categoryIdBox);
    menuLayoutCenter.setPadding(new Insets(0, 10, 280, 30));

    VBox imageSelection = imageSelection();

    // Bottom container for add and back button
    HBox bottomContainer = new HBox(20, confirmButton, backButton, imageSelection);
    bottomContainer.setAlignment(Pos.BOTTOM_CENTER);
    productName.setPrefWidth(300);

    // Final layout
    BorderPane layout = new BorderPane();
    layout.setTop(menuTitle);
    layout.setLeft(menuLayoutLeft);
    layout.setCenter(menuLayoutCenter);
    layout.setRight(ingredientBox);
    layout.setBottom(bottomContainer);
    layout.setPadding(new Insets(0, 0, 50, 0));

    // Language Button
    var langButton = new LangBtn();

    // Position the language button in the bottom-left corner
    StackPane.setAlignment(langButton, Pos.BOTTOM_LEFT);
    StackPane.setMargin(langButton, new Insets(0, 0, 30, 30));

    // put everything into a stackpane
    StackPane mainPane = new StackPane(layout, langButton);

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
      lang.translateLabels(mainPane);
    });

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(mainPane);
    lang.translateLabels(mainPane);

    Scene addProductScene = new Scene(mainPane, 1920, 1080);

    return addProductScene;
  }

  /**
   * Helper method for showing alerts on incorrect input.
   *
   * @param title   String title of the alert
   * @param message String message of the alert
   * @param type    what type of alert it is
   */
  private void showAlert(String title, String message, Alert.AlertType type) {
    LanguageSetting lang = LanguageSetting.getInstance();
    String translatedTitle = lang.translate(title);
    String translatedMessage = lang.translate(message);

    Alert alert = new Alert(type);
    alert.setTitle(translatedTitle);
    alert.setHeaderText(null);
    alert.setContentText(translatedMessage);
    alert.showAndWait();
  }

  private VBox imageSelection() {

    SquareButtonWithImg selectFile = new SquareButtonWithImg("Select image",
        "right_arrow.png", "rgb(170, 170, 170)");

    // Creating an image preview and the whole Vbox for image selection
    imagePreview.setFitWidth(150);
    imagePreview.setFitHeight(150);
    imagePreview.setPreserveRatio(true);
    VBox imageSelectionBox = new VBox(10, imagePreview, selectFile, imageUrlLabel);
    imageSelectionBox.setAlignment(Pos.CENTER);
    // Event handling for file choosing
    FileChooser fileChooser = new FileChooser();
    selectFile.setOnAction(e -> {
      fileChooser.getExtensionFilters().addAll(
          new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

      File selectedFile = fileChooser.showOpenDialog(primaryStage);

      if (selectedFile != null) {
        try {
          String fileName = selectedFile.getName();
          // Convert to URL string and store it
          selectedImageUrl = selectedFile.toURI().toURL().toString();
          this.relativeImagePath = "/food/" + fileName;

          // Update the UI
          imageUrlLabel.setText("Selected Image");
          imagePreview.setImage(new Image(selectedImageUrl));

        } catch (MalformedURLException ex) {
          ex.printStackTrace();
          showAlert("Error", "Invalid file path", Alert.AlertType.ERROR);
        }
      }
    });

    return imageSelectionBox;
  }
}
