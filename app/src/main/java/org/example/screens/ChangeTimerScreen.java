package org.example.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.kiosk.InactivityTimer;
import org.example.kiosk.LanguageSetting;

/**
 * This is the timer editor scene.
 * Used in the admin menu in 'AdminMenuScreen.java'.
 */
public class ChangeTimerScreen {

  private Stage primaryStage;
  private Scene prevScene;

  /**
   * The timer editor scene constructor.
   * ONLY used in the admin menu in 'AdminMenuScreen.java'.
   *
   * @param primaryStage the primary stage for the scenes
   * @param prevScene    the scene you were previously in
   */
  public ChangeTimerScreen(
      Stage primaryStage,
      Scene prevScene) {

    this.primaryStage = primaryStage;
    this.prevScene = prevScene;
  }

  /**
   * This is the method to create the scene for changing
   * the inactivity timers in the main menu for customers.
   *
   * @return change timer scene
   */
  public Scene getChangeTimerScene() {

    // Get editor for kiosk timer and popup timer and combine both
    HBox timerEditor = new HBox(400, getEditorBox(true), getEditorBox(false));
    timerEditor.setAlignment(Pos.CENTER);

    // Language + Cancel Buttons HBOX BOTTOM

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

    // Setting positioning of all the elements - putting all elements together
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setTop(getWindowTitle());
    BorderPane.setMargin(timerEditor, new Insets(200, 0, 50, 0));
    layout.setCenter(timerEditor);
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
      lang.smartLabelTranslate(layout);
    });

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(layout);
    lang.smartLabelTranslate(layout);

    var scene = new Scene(layout, 1920, 1080);

    return scene;
  }

  /**
   * Helper method to get the window's title.
   *
   * @return window title in an HBox
   */
  private HBox getWindowTitle() {
    // Label for screen title
    Label windowTitle = new Label("Timer Editor:");
    windowTitle.setStyle(
        "-fx-font-size: 45px;"
            + "-fx-font-weight: bold;");
    windowTitle.setAlignment(Pos.TOP_CENTER);

    HBox titleBox = new HBox(windowTitle);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setPadding(new Insets(20, 0, 60, 0));

    return titleBox;
  }

  /**
   * Flexible helper method for the timer editor boxes.
   *
   * @param isKioskTimer true if it's the kiosk timer
   *                     false it it's the popup timer
   * @return VBox containing one timer editor
   */
  private VBox getEditorBox(boolean isKioskTimer) {

    // Getting name depending on what kind of timer
    String name = isKioskTimer ? "Kiosk " : "Popup ";

    // Label for timer
    Label timerTitle = new Label(name + "Timer:");
    timerTitle.setStyle(
        "-fx-font-size: 30px;"
            + "-fx-font-weight: bold;");

    // Get current timer value depending on what kind of timer
    int currentTimerValue;
    if (isKioskTimer) {
      currentTimerValue = InactivityTimer.getInstance().getInactivityTimer();
    } else {
      currentTimerValue = InactivityTimer.getInstance().getInactivityTimerPopup();
    }

    // Current timer value
    Label currentTimer = new Label(
        "Current inactivity timer: "
            + currentTimerValue
            + " seconds");
    currentTimer.setStyle("-fx-font-size: 20px;");

    // Input field to enter new value
    TextField timerInput = new TextField();
    timerInput.setPromptText("New timer value (in seconds)");
    timerInput.setMaxWidth(250);

    // Label for feedback after update
    Label consoleOutput = new Label();
    consoleOutput.setStyle("-fx-font-size: 18px;");

    // Button to submit new value
    Button updateButton = new Button("Update Timer");

    updateButton.setOnAction(e -> {
      String input = timerInput.getText();

      // Error handling of incorrect value input
      try {
        // If input is int value
        int newValue = Integer.parseInt(input);

        // If int value is not reasonable (realistic)
        if (newValue < 5) {
          consoleOutput.setText("Please enter a value >= 5 seconds.");
          // No value change
          return;
        }

        // User entered proper value -> execute value change

        // Set new inactivity timer depending on what kind of timer
        if (isKioskTimer) {
          InactivityTimer.getInstance().setNewInactivityTimer(newValue);
        } else {
          InactivityTimer.getInstance().setNewInactivityTimerPopup(newValue);
        }

        // Update display label
        currentTimer.setText(
            "Current inactivity timer: "
                + newValue + " seconds");

        consoleOutput.setText("Timer updated successfully!");

        // User entered a letter -> no value change
      } catch (NumberFormatException ex) {
        consoleOutput.setText("Invalid input! Please enter a number.");
      }
    });

    // Combine left timer editor
    VBox timer = new VBox(
        20,
        timerTitle,
        currentTimer,
        timerInput,
        updateButton,
        consoleOutput);
    timer.setAlignment(Pos.TOP_LEFT);

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(timer);
    lang.smartLabelTranslate(timer);

    return timer;
  }
}
