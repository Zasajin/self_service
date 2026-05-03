package org.example.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.buttons.SearchBar;
import org.example.kiosk.LanguageSetting;
import org.example.menu.Product;

/**
 * This is the product editor scene.
 * Used in the admin menu in 'UpdateMenuItems.java'.
 */
public class ProductEditorScene {

  private Stage primaryStage;
  private Scene prevScene;
  private TableView<Product> productTable;
  private SearchBar searchBar;

  /**
   * The product editor scene constructor.
   * ONLY used in the admin menu in 'UpdateMenuItems.java'.
   *
   * @param primaryStage the primary stage for the scenes
   * @param prevScene    the scene you were previously in
   */
  public ProductEditorScene(
      Stage primaryStage,
      Scene prevScene,
      TableView<Product> productTable,
      SearchBar searchBar) {

    this.primaryStage = primaryStage;
    this.prevScene = prevScene;
    this.productTable = productTable;
    this.searchBar = searchBar;
    searchBar.setOnResultsListHandler(filteredProducts -> {
      productTable.getItems().clear();
      productTable.getItems().addAll(filteredProducts);
    });

    searchBar.setOnResultSelectHandler(selected -> {
      if (selected instanceof Product product) {

        boolean alreadyExists = productTable.getItems().stream()
            .anyMatch(p -> p.getId() == product.getId());

        if (!alreadyExists) {
          productTable.getItems().add(product);
        }

      }
    });

  }

  /**
   * This is the method to create the scene for editing
   * products in the admin menu.
   *
   * @return product editor scene
   */
  public Scene getProductEditorScene() {

    // Label for screen
    Label historyLabel = new Label("Product Editor:");
    historyLabel.setStyle(
        "-fx-font-size: 45px;"
            + "-fx-font-weight: bold;");

    // VBox for the table
    VBox productListings = new VBox(productTable);
    VBox.setVgrow(productListings, Priority.ALWAYS);
    productTable.prefWidthProperty().bind(productListings.widthProperty());
    productListings.setPadding(new Insets(20, 0, 0, 0));

    // VBox to align screen label and table
    VBox topBox = new VBox();
    topBox.setMaxWidth(Double.MAX_VALUE);
    topBox.setAlignment(Pos.TOP_CENTER);
    topBox.setSpacing(40);
    topBox.getChildren().addAll(historyLabel, productListings);
    topBox.getChildren().add(searchBar);

    // Upper part of the screen
    HBox topContainer = new HBox();
    topContainer.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(topBox, Priority.ALWAYS);
    topContainer.setAlignment(Pos.CENTER);
    topContainer.getChildren().addAll(topBox);

    // Back button
    // Clicking button means user goes to previous screen
    var backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> {
      primaryStage.setScene(prevScene);
    });

    // Language Button
    // cycles images on click
    var langButton = new LangBtn();

    // Spacer for Bottom Row
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Bottom row of the screen
    HBox bottomContainer = new HBox();
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);
    bottomContainer.getChildren().addAll(langButton, spacerBottom, backButton);

    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setTop(topContainer);
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

    Scene scene = new Scene(layout, 1920, 1080);

    return scene;
  }
}
