package org.example.screens;

import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.buttons.SearchBar;

/**
 * This class represents a screen with a search bar and navigation buttons.
 */
public class SeachBarScreen {

  /**
   * Scene to display only the search bar and cancel button.
   *
   * @param primaryStage this window
   * @param prevScene    previous scene to go back to
   * @return the scene itself
   * @throws SQLException sql exception.
   */
  public Scene showSearchScene(Stage primaryStage, Scene prevScene) throws SQLException {

    // Create the search bar
    SearchBar adminSearch = new SearchBar();
    adminSearch.setMaxWidth(800);
    adminSearch.setMinWidth(500);

    VBox centerBox = new VBox(20, adminSearch);
    centerBox.setAlignment(Pos.CENTER);

    // Cancel/Back button
    var backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> primaryStage.setScene(prevScene));

    // Language Button (optional)
    var langButton = new LangBtn();

    Region spacer = new Region();
    HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

    // Bottom bar layout
    HBox bottomContainer = new HBox(10, langButton, spacer, backButton);
    bottomContainer.setPadding(new Insets(10));
    bottomContainer.setAlignment(Pos.CENTER_LEFT);

    // Main layout
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setCenter(centerBox);
    layout.setBottom(bottomContainer);

    return new Scene(layout, 1920, 1080);
  }
}
