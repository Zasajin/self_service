package org.example.screens;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.RectangleTextFieldWithLabel;
import org.example.buttons.SqrBtnWithOutline;
import org.example.sql.DatabaseManager;

/**
 * The Scene to add a Meal to the menu.
 */
public class AddMealScene {
  private Stage primaryStage;
  private Scene prevScene;

  /**
   * Adding a meal scene constructor.
   *
   * @param primaryStage the primary stage of the current scene
   * @param prevScene    the previous scene
   */
  public AddMealScene(Stage primaryStage, Scene prevScene) {
    this.primaryStage = primaryStage;
    this.prevScene = prevScene;
  }

  /**
   * Method for getting the meal scene.
   *
   * @return the meal scene
   */
  public Scene getAddMealScene() {

    ObservableList<CheckBox> products = FXCollections.observableArrayList();
    ObservableList<CheckBox> sides = FXCollections.observableArrayList();
    ObservableList<CheckBox> drinks = FXCollections.observableArrayList();

    try (Connection connection = DatabaseManager.getConnection()) {
      String sql = "SELECT name FROM product WHERE category_id = 1";
      PreparedStatement stmt = connection.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        CheckBox checkBox = new CheckBox(rs.getString("name"));
        checkBox.setStyle(
            "-fx-font-size: 18px;");
        products.add(checkBox);
      }

      for (CheckBox checkbox : products) {
        checkbox.setOnAction(e -> {
          if (checkbox.isSelected()) {
            for (CheckBox otherbox : products) {
              if (otherbox != checkbox) {
                otherbox.setSelected(false);
              }
            }
          }
        });
      }

      String sql2 = "SELECT name FROM product WHERE category_id = 2";
      PreparedStatement stmt2 = connection.prepareStatement(sql2);
      ResultSet rs2 = stmt2.executeQuery();

      while (rs2.next()) {
        CheckBox checkBox = new CheckBox(rs2.getString("name"));
        checkBox.setStyle(
            "-fx-font-size: 18px;");
        sides.add(checkBox);
      }

      String sql3 = "SELECT name FROM product WHERE category_id = 3";
      PreparedStatement stmt3 = connection.prepareStatement(sql3);
      ResultSet rs3 = stmt3.executeQuery();

      while (rs3.next()) {
        CheckBox checkBox = new CheckBox(rs3.getString("name"));
        checkBox.setStyle(
            "-fx-font-size: 18px;");
        drinks.add(checkBox);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    ListView<CheckBox> pickSide = new ListView<>(sides);
    ListView<CheckBox> pickDrink = new ListView<>(drinks);
    ListView<CheckBox> chooseProduct = new ListView<>(products);
    chooseProduct.setPrefSize(100, 300);
    chooseProduct.setMaxWidth(350);
    VBox pickOptionsBox = new VBox(pickSide, pickDrink);
    pickOptionsBox.setSpacing(20);
    pickOptionsBox.setPadding(new Insets(0, 50, 0, 10));

    var productLabel = new Label("Choose Main");
    productLabel.setStyle("-fx-font-size: 30");

    VBox chooseProductBox = new VBox(productLabel, chooseProduct);
    chooseProductBox.setAlignment(Pos.CENTER);
    chooseProductBox.setPadding(new Insets(0, 150, 50, 0));

    RectangleTextFieldWithLabel mealName = new RectangleTextFieldWithLabel("Meal Name:",
        "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel mealPrice = new RectangleTextFieldWithLabel("Price",
        "rgb(255, 255, 255)");
    RectangleTextFieldWithLabel mealDescription = new RectangleTextFieldWithLabel(
        "Meal Description:",
        "rgb(255, 255, 255)");

    VBox leftSide = new VBox();
    leftSide.getChildren().addAll(mealName, mealPrice, mealDescription);
    leftSide.setAlignment(Pos.CENTER);
    leftSide.setPadding(new Insets(20));
    var menuLabel = new Label("Add a meal to the menu");
    menuLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bold;");
    HBox topLayout = new HBox(menuLabel);
    topLayout.setAlignment(Pos.CENTER);
    topLayout.setPadding(new Insets(30));

    SqrBtnWithOutline confirmButton = new SqrBtnWithOutline("Confirm",
        "green_tick.png", "rgb(81, 173, 86)");

    confirmButton.setOnAction(e -> {
      ArrayList<String> selectedProducts = getSelected(products);
      if (selectedProducts.size() == 1) {
        ArrayList<String> selectedDrinks = getSelected(drinks);
        ArrayList<String> selectSides = getSelected(sides);

        try (Connection connection = DatabaseManager.getConnection()) {
          String sql = "SELECT product_id FROM product WHERE name = ?";
          PreparedStatement stmt = connection.prepareStatement(sql);
          stmt.setString(1, selectedProducts.get(0));
          ResultSet rs = stmt.executeQuery();

          rs.next();
          rs.close();
          stmt.close();

          String sql2 = "INSERT INTO meal"
              + "(name, description, price, image_url, product_id) VALUES (?, ?, ?, ?, ?)";
          PreparedStatement stmt2 = connection.prepareStatement(
              sql2, PreparedStatement.RETURN_GENERATED_KEYS);
          stmt2.setString(1, mealName.getText());
          stmt2.setString(2, mealDescription.getText());
          stmt2.setDouble(3, Double.parseDouble(mealPrice.getText()));
          stmt2.setString(4, "/food/default_meal.png");

          int productId = rs.getInt("product_id");

          stmt2.setInt(5, productId);
          stmt2.executeUpdate();

          int mealId;
          try (ResultSet generatedKeys = stmt2.getGeneratedKeys()) {
            if (generatedKeys.next()) {
              mealId = generatedKeys.getInt(1);
            } else {
              throw new SQLException("Creating producted failed, no ID obtained");
            }
          }
          stmt2.close();

          for (int i = 0; i < selectSides.size(); i++) {
            String sql3 = "SELECT product_id FROM product WHERE name = ?";
            PreparedStatement stmt3 = connection.prepareStatement(sql3);
            stmt3.setString(1, selectSides.get(i));
            ResultSet rs3 = stmt3.executeQuery();
            rs3.next();

            int sideId = rs3.getInt("product_id");

            String sql4 = "INSERT INTO meal_sideoptions (meal_id, product_id) Values (?, ?)";
            PreparedStatement stmt4 = connection.prepareStatement(sql4);
            stmt4.setInt(1, mealId);
            stmt4.setInt(2, sideId);
            stmt4.executeUpdate();
          }

          for (int i = 0; i < selectedDrinks.size(); i++) {
            String sql5 = "SELECT product_id FROM product WHERE name = ?";
            PreparedStatement stmt5 = connection.prepareStatement(sql5);
            stmt5.setString(1, selectedDrinks.get(i));
            ResultSet rs5 = stmt5.executeQuery();
            rs5.next();

            int drinkId = rs5.getInt("product_id");

            String sql6 = "INSERT INTO meal_drinkoptions (meal_id, product_id) Values (?, ?)";
            PreparedStatement stmt6 = connection.prepareStatement(sql6);
            stmt6.setInt(1, mealId);
            stmt6.setInt(2, drinkId);
            stmt6.executeUpdate();
          }

          connection.commit();
          System.out.println("Ended");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      } else {
        System.out.println("Choose only one Product");
      }

    });

    BackBtnWithTxt backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> {
      primaryStage.setScene(prevScene);
    });
    HBox bottomLayout = new HBox(confirmButton, backButton);
    bottomLayout.setAlignment(Pos.CENTER);
    bottomLayout.setPadding(new Insets(20));
    bottomLayout.setSpacing(20);
    BorderPane layout = new BorderPane();
    layout.setTop(topLayout);
    layout.setLeft(leftSide);
    layout.setCenter(chooseProductBox);
    layout.setRight(pickOptionsBox);
    layout.setBottom(bottomLayout);

    Scene addMealScene = new Scene(layout, 1920, 1080);
    return addMealScene;
  }

  private ArrayList<String> getSelected(ObservableList<CheckBox> list) {
    ArrayList<String> select = new ArrayList<>();
    for (CheckBox cb : list) {
      if (cb.isSelected()) {
        select.add(cb.getText());
      }
    }
    return select;
  }
}
