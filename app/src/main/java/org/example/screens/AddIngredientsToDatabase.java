package org.example.screens;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.buttons.RectangleTextFieldWithLabel;
import org.example.buttons.SqrBtnWithOutline;
import org.example.buttons.TitleLabel;
import org.example.kiosk.LanguageSetting;
import org.example.sql.DatabaseManager;

/**
 * Scene for adding new ingredients to the database.
 */
public class AddIngredientsToDatabase {
  private Stage primaryStage;
  private Scene prevScene;
  private Label errorLabel;

  /**
   * Constructor for the AddIngredientsToDatabase scene.
   *
   * @param primaryStage the primary stage of the application
   * @param prevScene    the previous scene to return to
   */
  public AddIngredientsToDatabase(Stage primaryStage, Scene prevScene) {
    this.primaryStage = primaryStage;
    this.prevScene = prevScene;
  }

  /**
   * Creates and returns the scene for adding ingredients to the database.
   *
   * @return the configured Scene object
   */
  public Scene addIngredientToDb() {

    TitleLabel titleLabel = new TitleLabel("Add Ingredients to the Database");

    HBox titleBox = new HBox(titleLabel);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setPadding(new Insets(30));

    errorLabel = new Label();
    errorLabel.setStyle(
        "-fx-text-fill: black;"
            + "-fx-font-weight: lighter;"
            + "-fx-font-size: 13;"
            + "-fx-background-radius: 10;");
    errorLabel.setMinHeight(40);
    errorLabel.setPrefHeight(40);
    ;
    // Initially hidden
    errorLabel.setOpacity(0);
    errorLabel.setManaged(true);

    VBox categoryBox = new VBox(5);
    categoryBox.setPrefHeight(500);
    categoryBox.setPrefWidth(250);
    categoryBox.setAlignment(Pos.TOP_CENTER);
    loadCategories(categoryBox);

    var checkBoxLabel = new Label("Select Categories which will have this ingredient:");
    checkBoxLabel.setStyle("-fx-font-family: Tahoma;"
        + "-fx-font-size: 20 px;"
        + "-fx-font-weight: bolder;"
        + "-fx-text-alignment: center;"
        + "-fx-text-fill: rgb(0, 0, 0);");

    // Input fields
    RectangleTextFieldWithLabel nameField = new RectangleTextFieldWithLabel(
        "Ingredient Name:", "rgb(255, 255, 255)");
  
    // Buttons
    SqrBtnWithOutline confirmButton = new SqrBtnWithOutline(
        "Add", "green_tick.png", "rgb(81, 173, 86)");
    confirmButton.setOnAction(e -> addIngredientToDatabase(
        nameField.getText(),
        getSelectedCategories(categoryBox)));

    BackBtnWithTxt backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> primaryStage.setScene(prevScene));

    HBox buttonBox = new HBox(20, confirmButton, backButton);
    buttonBox.setAlignment(Pos.TOP_CENTER);
    buttonBox.setPadding(new Insets(20));

    VBox inputFields = new VBox(20, nameField, checkBoxLabel,
        categoryBox, buttonBox, errorLabel);
    inputFields.setAlignment(Pos.CENTER);
    inputFields.setPadding(new Insets(20, 0, 0, 0));

    // Language button
    LangBtn langButton = new LangBtn();

    // Spacer for bottom layout
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    // Bottom layout
    HBox bottomBox = new HBox(langButton);
    bottomBox.setAlignment(Pos.BOTTOM_LEFT);
    bottomBox.setPadding(new Insets(20));

    // Main layout
    BorderPane mainLayout = new BorderPane();
    mainLayout.setTop(titleBox);
    mainLayout.setCenter(inputFields);
    mainLayout.setBottom(bottomBox);

    // Translate button action
    langButton.addAction(event -> {
      LanguageSetting lang = LanguageSetting.getInstance();
      String newLang = lang.getSelectedLanguage().equals("en") ? "sv" : "en";
      lang.changeLanguage(newLang);
      lang.translateLabels(mainLayout);
    });

    // Initial translation
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(mainLayout);
    lang.translateLabels(mainLayout);

    return new Scene(mainLayout, 1920, 1080);
  }

  private void loadCategories(VBox categoryBox) {
    try (Connection connection = DatabaseManager.getConnection()) {
      String sql = "SELECT category_id, name FROM category";
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        CheckBox checkBox = new CheckBox(rs.getString("name"));
        checkBox.setUserData(rs.getInt("category_id"));
        checkBox.setStyle("-fx-font-size: 16px;"
            + "-fx-padding: 8px;"
            + "-fx-scale-x: 1.2;"
            + "-fx-scale-y: 1.2;");

        // Add directly to the VBox instead of creating HBoxes
        categoryBox.getChildren().add(checkBox);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private List<Integer> getSelectedCategories(VBox categoryBox) {
    List<Integer> selected = new ArrayList<>();
    for (Node node : categoryBox.getChildren()) {
      if (node instanceof CheckBox) {
        CheckBox checkBox = (CheckBox) node;
        if (checkBox.isSelected()) {
          selected.add((Integer) checkBox.getUserData());
        }
      }
    }
    return selected;
  }

  private void addIngredientToDatabase(String name, List<Integer> categoryIds) {
    if (name.isEmpty()) {
      showError("Ingredient field cannot be empty", errorLabel);
      return;
    }
    List<String> existingIngredients = getIngredients();

    if (existingIngredients.contains(name.toLowerCase())) {
      showError("This ingredient is already in the database", errorLabel);
      return;
    }

    Connection connection = null;
    try {
      connection = DatabaseManager.getConnection();
      connection.setAutoCommit(false);

      // Insert ingredient
      String ingredientSql = "INSERT INTO ingredient (ingredient_name) VALUES (?)";
      PreparedStatement ingredientStmt = connection.prepareStatement(
          ingredientSql, PreparedStatement.RETURN_GENERATED_KEYS);
      ingredientStmt.setString(1, name);
      ingredientStmt.executeUpdate();

      // Get generated ingredient ID
      int ingredientId;
      try (ResultSet generatedKeys = ingredientStmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          ingredientId = generatedKeys.getInt(1);
        } else {
          throw new SQLException("Failed to get ingredient ID");
        }
      }

      // Insert category associations
      if (!categoryIds.isEmpty()) {
        String checkSql = "SELECT 1 FROM categoryingredients "
            + "WHERE ingredient_id = ? AND category_id = ?";
        String insertSql = "INSERT INTO categoryingredients "
            + "(ingredient_id, category_id) VALUES (?, ?)";

        PreparedStatement checkStmt = connection.prepareStatement(checkSql);
        PreparedStatement insertStmt = connection.prepareStatement(insertSql);

        for (int categoryId : categoryIds) {
          // Check if relationship already exists
          checkStmt.setInt(1, ingredientId);
          checkStmt.setInt(2, categoryId);
          try (ResultSet rs = checkStmt.executeQuery()) {
            if (!rs.next()) { // Only insert if relationship doesn't exist
              insertStmt.setInt(1, ingredientId);
              insertStmt.setInt(2, categoryId);
              insertStmt.addBatch();
            }
          }
        }

        insertStmt.executeBatch();
      }

      connection.commit();
      changeLabelText("Ingredient has been successfully added!", errorLabel);

    } catch (SQLException e) {
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
      showError("Error adding ingredient: " + e.getMessage(), errorLabel);
      e.printStackTrace();
    } finally {
      if (connection != null) {
        try {
          connection.setAutoCommit(true);
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private List<String> getIngredients() {
    List<String> ingredients = new ArrayList<>();
    try (Connection connection = DatabaseManager.getConnection()) {
      String sql = "SELECT ingredient_name FROM ingredient";
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        ingredients.add(rs.getString("ingredient_name").toLowerCase());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ingredients;
  }

  private void showError(String errorText, Label label) {
    if (label != null) {
      label.setText(errorText);
      label.setStyle("-fx-text-fill: red; -fx-font-size: 14;");
      label.setOpacity(1);

      PauseTransition pause = new PauseTransition(Duration.seconds(2));
      pause.setOnFinished(event -> {
        label.setOpacity(0);
        label.setText(null);
        label.setGraphic(null); // removes all fields of the label
      });
      pause.play();
    }
  }

  private void changeLabelText(String errorText, Label label) {
    if (label != null) {
      label.setText(errorText);
      label.setStyle("-fx-text-fill: green; -fx-font-size: 14;");
      label.setOpacity(1);

      PauseTransition pause = new PauseTransition(Duration.seconds(2));
      pause.setOnFinished(event -> {
        label.setOpacity(0);
        label.setText(null);
        label.setGraphic(null); // removes all fields of the label
      });
      pause.play();
    }
  }
}