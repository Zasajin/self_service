package org.example.screens;

import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.animations.AnimationHelper;
import org.example.buttons.BlackButtonWithImage;
import org.example.buttons.ColorBtnOutlineImage;
import org.example.buttons.KioskName;
import org.example.buttons.LangBtn;
import org.example.kiosk.InactivityTimer;
import org.example.kiosk.LabelManager;
import org.example.kiosk.LanguageSetting;
import org.example.sql.SqlConnectionCheck;

/**
 * The welcome screen class.
 */
public class WelcomeScreen {

  /**
   * The welcome class scene.
   *
   * @throws SQLException if the server has issues
   */
  public CustomScene createWelcomeScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight) throws SQLException {

    // Initialize the welcome screen elements
    var mainWindow = new VBox();
    mainWindow.setAlignment(Pos.CENTER);

    // Setup labels
    var welcome = new Label("Welcome to");
    LabelManager.register(welcome);
    var companyTitle = new Label(KioskName.getCompanyTitle());
    LabelManager.register(companyTitle);

    // Customize labels
    welcome.setStyle(
        "-fx-background-color: transparent;"
            // + "-fx-text-fill: black;"
            + "-fx-font-weight: lighter;"
            + "-fx-font-size: 100;"
            + "-fx-background-radius: 10;");

    companyTitle.setStyle(
        "-fx-background-color: transparent;"
            // + "-fx-text-fill: black;"
            + "-fx-font-weight: bolder;"
            + "-fx-font-size: 160;"
            + "-fx-background-radius: 10;");

    // HBox for Burger images
    var rowOfBurgers = new HBox(300);
    rowOfBurgers.setAlignment(Pos.CENTER);

    // Setup side images

    // Image burger3 = new Image(getClass().getResourceAsStream("/burger3.png"));

    // Create a button with the burger image as its graphic
    Button burgerButton = new Button();
    Image burger3 = new Image(getClass().getResourceAsStream("/burger3.png"));
    ImageView burgerView3 = new ImageView(burger3);
    burgerView3.setScaleX(0.5);
    burgerView3.setScaleY(0.5);
    burgerButton.setGraphic(burgerView3);
    burgerButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    Image burger1 = new Image(getClass().getResourceAsStream("/burger1.png"));

    // Set the button action
    burgerButton.setOnAction(e -> {
      System.out.println("Right burger clicked!");
    });

    // Add the button to the HBox

    ImageView burgerView1 = new ImageView(burger1);
    var burgerContainer1 = new HBox(burgerView1);
    burgerContainer1.setAlignment(Pos.BASELINE_LEFT);
    var burgerContainer3 = new HBox(burgerView3);
    burgerContainer3.setAlignment(Pos.BASELINE_RIGHT);

    // Make size 50% smaller on all axis of Burger1+3
    burgerView1.setScaleX(0.5);
    burgerView1.setScaleY(0.5);
    burgerView3.setScaleX(0.5);
    burgerView3.setScaleY(0.5);

    // Setup buttons
    var rowOfButtons = new HBox(20);
    rowOfButtons.setPadding(new Insets(30));
    rowOfButtons.setAlignment(Pos.CENTER);

    var eatHereBtn = new BlackButtonWithImage(
        "Eat Here",
        "/eatHere.png");

    var takeAwayBtn = new ColorBtnOutlineImage(
        "Takeaway",
        "/takeaway.png");

    rowOfButtons.getChildren().addAll(eatHereBtn, takeAwayBtn);

    Button termsButton = new Button("Terms of Service");
    termsButton.setStyle(
        "-fx-text-fill: blue; -fx-underline: true; -fx-background-color: transparent;");
    termsButton.setOnAction(e -> {
      showTermsDialog(primaryStage);

    });

    // Add centre image
    Image burger2 = new Image(getClass().getResourceAsStream("/burger2.png"));
    ImageView burgerView2 = new ImageView(burger2);
    rowOfBurgers.getChildren().addAll(burgerView1, burgerView2, burgerButton);

    var langButton = new LangBtn();
    langButton.updateImage();

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
      lang.translateLabels(mainWindow);
    });

    // Position the language button in the bottom-left corner
    StackPane.setAlignment(langButton, Pos.BOTTOM_LEFT);
    StackPane.setMargin(langButton, new Insets(0, 0, 30, 30));

    // Test sql connection
    SqlConnectionCheck connectionCheck = new SqlConnectionCheck();
    Label mysql = connectionCheck.getMysqlLabel();
    LabelManager.register(mysql);

    mainWindow.getChildren().addAll(
        welcome, companyTitle, rowOfBurgers, rowOfButtons, mysql, termsButton);

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(mainWindow);
    lang.translateLabels(mainWindow);

    // Put everythng in a stackpane
    StackPane mainPane = new StackPane(mainWindow, langButton);
    mainPane.setPrefSize(windowWidth, windowHeight);

    CustomScene scene = new CustomScene(mainPane, windowWidth, windowHeight);

    // Reads and applies the customized background color
    Color bgColor = BackgroundColorStore.getCurrentBackgroundColor();

    if (bgColor != null) {

      scene.setBackgroundColor(bgColor);

    }

    // Set up action for eat here
    eatHereBtn.setOnAction(e -> {
      try {
        MainMenuScreen mainMenuScreen = new MainMenuScreen();
        CustomScene mainMenuScene = mainMenuScreen.createMainMenuScreen(
            primaryStage,
            windowWidth,
            windowHeight,
            scene,
            0,
            "eatHere");
        primaryStage.setScene(mainMenuScene);

        InactivityTimer.getInstance().setPrimaryStage(primaryStage);
        InactivityTimer.getInstance().startTimer();

        // Any movement of the user resets the inactivity timer
        primaryStage.addEventFilter(
            MouseEvent.ANY, ev -> InactivityTimer.getInstance().resetTimer());
        primaryStage.addEventFilter(
            KeyEvent.ANY, ev -> InactivityTimer.getInstance().resetTimer());

      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    });

    // Set up action for takeaway
    takeAwayBtn.setOnAction(e -> {
      try {
        MainMenuScreen mainMenuScreen = new MainMenuScreen();
        CustomScene mainMenuScene = mainMenuScreen.createMainMenuScreen(
            primaryStage,
            windowWidth,
            windowHeight,
            scene,
            0,
            "takeaway");
        primaryStage.setScene(mainMenuScene);

        InactivityTimer.getInstance().setPrimaryStage(primaryStage);
        InactivityTimer.getInstance().startTimer();

        // Any movement of the user resets the inactivity timer
        primaryStage.addEventFilter(
            MouseEvent.ANY, ev -> InactivityTimer.getInstance().resetTimer());
        primaryStage.addEventFilter(
            KeyEvent.ANY, ev -> InactivityTimer.getInstance().resetTimer());

      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    });

    AdminLoginScreen adminLoginScreen = new AdminLoginScreen();
    Scene adminMenuScene = adminLoginScreen.createAdminLoginScreen(primaryStage,
        windowWidth, windowHeight, scene);

    // Button to get to the admin menu
    burgerButton.setOnAction(e -> {
      primaryStage.setScene(adminMenuScene);
    });

    // Fade in welcome text
    AnimationHelper.fadeIn(welcome, 0.5, 0.0);

    // Company title
    AnimationHelper.slideInFromTop(companyTitle, 20, 0.5, 0.3);
    AnimationHelper.pulse(companyTitle, 1.02, 0.4, 0.8);

    // Center burger
    AnimationHelper.fadeAndSlideIn(burgerView2, 20, 0.5, 0.9);

    // Left and right burgers
    AnimationHelper.fadeAndSlideIn(burgerView1, -15, 0.4, 1.0);
    AnimationHelper.fadeAndSlideIn(burgerView3, 15, 0.4, 1.1);

    // Buttons
    AnimationHelper.pulse(eatHereBtn, 1.02, 0.4, 1.4);
    AnimationHelper.pulse(takeAwayBtn, 1.02, 0.4, 1.6);

    return scene;
  }

  private void showTermsDialog(Stage ownerStage) {
    Stage dialog = new Stage();
    dialog.initOwner(ownerStage);
    dialog.setTitle("Terms of Service");

    var termsText = new String(
        "1. Acceptance of Terms\n"
            + "By using our services, you agree to these terms...\n\n"
            + "2. Service Description\n"
            + "We provide food ordering services...\n\n"
            + "3. User Responsibilities\n"
            + "You must provide accurate information...\n\n"
            + "4. Limitation of Liability\n"
            + "We are not responsible for...\n\n"
            + "Last Updated: ");

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    var translatedTerms = lang.translate(termsText);

    Label termsContent = new Label(translatedTerms);

    termsContent.setWrapText(true);
    termsContent.setStyle("-fx-font-size: 14; -fx-padding: 10;");

    ScrollPane scrollPane = new ScrollPane(termsContent);
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefSize(400, 300);

    VBox dialogLayout = new VBox(scrollPane);
    dialogLayout.setPadding(new Insets(10));

    // Keep it translatable when click the button
    lang.registerRoot(dialogLayout);
    lang.translateLabels(dialogLayout);

    Scene dialogScene = new Scene(dialogLayout, 500, 300);
    dialog.setScene(dialogScene);
    dialog.show();
  }
}
