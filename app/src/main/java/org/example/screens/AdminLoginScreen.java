package org.example.screens;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.boxes.CustomKeyboard;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.buttons.MidButtonWithImage;
import org.example.kiosk.LanguageSetting;
import org.example.sql.DatabaseManager;
import org.example.users.AdminAuth;

/**
 * Admin login screen.
 */
public class AdminLoginScreen {
  /**
   * Admin login screen.
   *
   * @param primaryStage    The actual window itself.
   * @param windowWidth     Size of window width
   * @param windowHeight    Size of window height
   * @param welcomeScrScene Welcome screen scene
   * @return Returns the admin login screen
   */
  public Scene createAdminLoginScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene) {
    // the mainlayout
    VBox adminMenuLayout = new VBox(20);
    adminMenuLayout.setAlignment(Pos.CENTER);

    var adminMenuTitle = new Label();
    adminMenuTitle.setText("Admin Menu");
    adminMenuTitle.setStyle(
        "-fx-text-fill: black;"
            + "-fx-font-weight: lighter;"
            + "-fx-font-size: 50;"
            + "-fx-background-radius: 10;");

    // the field to enter the username
    TextField usernameField = new TextField();
    usernameField.setMaxSize(460, 140);
    usernameField.setPromptText("Username");
    usernameField.setStyle(
        "-fx-background-color: grey;"
            + "-fx-text-fill: black;"
            + "-fx-font-weight: lighter;"
            + "-fx-font-size: 50;"
            + "-fx-background-radius: 10;");

    // the field to enter the password
    PasswordField passwordField = new PasswordField();
    passwordField.setMaxSize(460, 140);
    passwordField.setPromptText("Password");
    passwordField.setStyle(
        "-fx-background-color: grey;"
            + "-fx-text-fill: black;"
            + "-fx-font-weight: lighter;"
            + "-fx-font-size: 50;"
            + "-fx-background-radius: 10;");

    Image errorIcon = new Image(getClass().getResourceAsStream("/errorLogin.png"));
    ImageView errorIconView = new ImageView(errorIcon);
    errorIconView.setFitWidth(40);
    errorIconView.setFitHeight(40);
    errorIconView.setPreserveRatio(true);

    var errorLabel = new Label();
    errorLabel.setStyle(
        "-fx-text-fill: black;"
            + "-fx-font-weight: lighter;"
            + "-fx-font-size: 50;"
            + "-fx-background-radius: 10;");
    errorLabel.setGraphic(errorIconView);
    errorLabel.setGraphicTextGap(10);
    // Initially hidden
    errorLabel.setVisible(false);

    // Custom keyboard for username and password fields
    CustomKeyboard keyboard = new CustomKeyboard(primaryStage, usernameField);

    var loginButton = new MidButton(
        "Login",
        "rgb(0, 0, 0)",
        50);

    // login button functionality
    loginButton.setOnAction(e -> {
      try (Connection connection = DatabaseManager.getConnection()) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        AdminAuth adminauth = new AdminAuth(connection);
        Boolean checkLogin = adminauth.verifyAdmin(username, password);
        if (checkLogin) {
          errorLabel.setVisible(false);
          passwordField.clear();
          usernameField.clear();
          AdminMenuScreen adminMenuScreen = new AdminMenuScreen();
          Scene adminMenuScene = adminMenuScreen.createAdminMenuScreen(primaryStage,
              windowWidth, windowHeight, welcomeScrScene, connection);

          // Close the keyboard when switching scenes
          keyboard.close();

          primaryStage.setScene(adminMenuScene);
        } else {
          var lang = LanguageSetting.getInstance();
          var errorMessage = lang.translate("Invalid login details");

          errorLabel.setText(errorMessage);

          errorLabel.setVisible(true);
          passwordField.clear();

          // Hide the error label after 2 seconds
          PauseTransition pause = new PauseTransition(Duration.seconds(2));
          pause.setOnFinished(event -> errorLabel.setVisible(false));
          pause.play();
        }
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    });

    // back button
    var backButton = new MidButtonWithImage(
        "Back to Menu",
        "/back.png",
        "rgb(255, 255, 255)");

    // Put buttons in an HBox
    HBox buttonContainer = new HBox(20);
    buttonContainer.setAlignment(Pos.CENTER);
    buttonContainer.getChildren().addAll(loginButton, backButton);

    // Set action for back button (to go back to the welcome screen)
    backButton.setOnAction(e -> {
      primaryStage.setScene(welcomeScrScene);

      // Close the keyboard when going back to the welcome screen
      keyboard.close();

      passwordField.clear();
      usernameField.clear();
    });

    adminMenuLayout.getChildren().addAll(
        adminMenuTitle,
        usernameField,
        passwordField,
        loginButton,
        backButton,
        errorLabel);

    // Add the language button
    var langButton = new LangBtn();

    // Position the language button in the bottom-left corner
    StackPane.setAlignment(langButton, Pos.BOTTOM_LEFT);
    StackPane.setMargin(langButton, new Insets(0, 0, 30, 30));

    // Keyboard functionality for username and password fields
    usernameField.setOnMouseClicked(e -> {
      keyboard.setTargetInput(usernameField);
      keyboard.show();
      Platform.runLater(() -> {
        usernameField.requestFocus();
        usernameField.positionCaret(usernameField.getText().length());
      });
    });

    passwordField.setOnMouseClicked(e -> {
      keyboard.setTargetInput(passwordField);
      keyboard.show();
      Platform.runLater(() -> passwordField.requestFocus());
    });

    // put everything into a stackpane
    StackPane mainPane = new StackPane(adminMenuLayout, langButton);
    mainPane.setPrefSize(windowWidth, windowHeight);

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

    // Create the admin login scene and go there
    Scene adminLoginScene = new Scene(mainPane, windowWidth, windowHeight);

    // Puts focus on the admin menu label so that the username field isn't by
    // default focused
    adminMenuTitle.requestFocus();

    usernameField.clear();
    passwordField.clear();

    return adminLoginScene;
  }
}
