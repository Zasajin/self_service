package org.example.screens;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.CancelButtonWithText;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.buttons.RoundRainbowBtn;
import org.example.kiosk.LanguageSetting;

/**
 * Admin menu class.
 */
public class AdminMenuScreen {

  /**
   * Admin menu screen.
   */
  public Scene createAdminMenuScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene,
      Connection conn) {

    // the mainlayout
    VBox adminMenuLayout = new VBox(20);
    adminMenuLayout.setAlignment(Pos.TOP_CENTER);
    adminMenuLayout.setPadding(new Insets(10));

    // Making the title on top of the admin menu screen
    Label adminMenuText = new Label("Welcome, Admin!");
    adminMenuText.setStyle(
        "-fx-font-size: 100px;"
            + "-fx-font-weight: bold;");

    adminMenuLayout.getChildren().addAll(adminMenuText);

    // this gridpane is used for all the middle buttons in the admin menu,
    // to align tem properly in rows and columns.
    GridPane centerGrid = new GridPane();
    centerGrid.setHgap(50);
    centerGrid.setVgap(30);
    centerGrid.setAlignment(Pos.CENTER);

    // All the same instances of the MidButton
    MidButton orderHistoryBtn = new MidButton("Order History", "rgb(255, 255, 255)", 30);
    orderHistoryBtn.setOnAction(e -> {
      Scene historyScene = new AdminOrdHistoryScreen().showHistoryScene(
          primaryStage,
          adminMenuLayout.getScene());
      primaryStage.setScene(historyScene);
    });

    MidButton salesSummaryBtn = new MidButton("See Sales Summary", "rgb(255, 255, 255)", 30);
    salesSummaryBtn.setOnAction(e -> {
      Scene statsScene = new SalesStatsScreen().showStatsScene(
          primaryStage,
          adminMenuLayout.getScene());
      primaryStage.setScene(statsScene);
    });

    MidButton changeTimerBtn = new MidButton("Change Timer Setting", "rgb(255, 255, 255)", 30);
    changeTimerBtn.setOnAction(e -> {
      Scene timerEditor = new ChangeTimerScreen(
          primaryStage,
          adminMenuLayout.getScene()).getChangeTimerScene();
      primaryStage.setScene(timerEditor);
    });

    MidButton updateMenuBtn = new MidButton("Update Menu Items", "rgb(255, 255, 255)", 30);

    centerGrid.add(updateMenuBtn, 0, 0);
    centerGrid.add(changeTimerBtn, 0, 1);
    centerGrid.add(orderHistoryBtn, 1, 0);
    centerGrid.add(salesSummaryBtn, 1, 1);

    MidButton searchBarBtn = new MidButton("Search", "rgb(255, 255, 255)", 30);

    searchBarBtn.setOnAction(e -> {
      Scene searchBarScreen = null;
      try {
        searchBarScreen = new SeachBarScreen().showSearchScene(
            primaryStage,
            adminMenuLayout.getScene());
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
      primaryStage.setScene(searchBarScreen);
    });

    centerGrid.add(searchBarBtn, 0, 2);

    // Adding the language button which already has the functionality of
    // changing the logo of the language
    var langButton = new LangBtn();
    HBox bottomLeftBox = new HBox(langButton);
    bottomLeftBox.setAlignment(Pos.BOTTOM_LEFT);

    // Spacer for bottom part of the screen
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Similar to a normal cancel button, it just has text under the image
    CancelButtonWithText cancelButton = new CancelButtonWithText();
    HBox bottomRightBox = new HBox(cancelButton);
    bottomRightBox.setAlignment(Pos.BOTTOM_RIGHT);

    updateMenuBtn.setOnAction(e -> {
      Scene updateMenuScene;
      try {
        updateMenuScene = new UpdateMenuItems().adminUpdateMenuItems(
            primaryStage,
            adminMenuLayout.getScene());
        primaryStage.setScene(updateMenuScene);
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    });

    // go back to the main screen if clicked
    cancelButton.setOnAction(e -> {
      primaryStage.setScene(welcomeScrScene);
    });

    HBox bottomLayout = new HBox();
    bottomLayout.setPadding(new Insets(0, 0, 0, 0));
    bottomLayout.getChildren().addAll(bottomLeftBox, spacerBottom, bottomRightBox);

    // Assigning the positions of elements for the Admin menu screen
    BorderPane mainBorderPane = new BorderPane();
    mainBorderPane.setPadding(new Insets(50));
    mainBorderPane.setTop(adminMenuLayout);
    mainBorderPane.setCenter(centerGrid);
    mainBorderPane.setBottom(bottomLayout);

    // Create a custom button with a rainbow border and "Design" label
    var customBtn = new RoundRainbowBtn();

    // go back to the main screen if clicked
    customBtn.setOnAction(e -> {
      Scene statsScene = new CustomizationScreen().showCustomizationScreen(
          primaryStage, windowWidth, windowHeight, welcomeScrScene);
      primaryStage.setScene(statsScene);
    });

    // Position the customization button in the top right corner
    StackPane.setAlignment(customBtn, Pos.TOP_RIGHT);
    StackPane.setMargin(customBtn, new Insets(50, 50, 0, 0));

    // put everything into a stackpane
    StackPane layout = new StackPane(mainBorderPane, customBtn);
    layout.setPrefSize(windowWidth, windowHeight);

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

    Scene adminMenuScene = new Scene(layout, windowWidth, windowHeight);

    return adminMenuScene;
  }
}
