package org.example.screens;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.boxes.CustomKeyboard;
import org.example.buttons.ArrowButton;
import org.example.buttons.BlackButtonWithImage;
import org.example.buttons.CartSquareButton;
import org.example.buttons.CircleButtonWithSign;
import org.example.buttons.ColorBtnOutlineImage;
import org.example.buttons.ColorButtonWithImage;
import org.example.buttons.ColorPickersPane;
import org.example.buttons.ColorSettingManager;
import org.example.buttons.ColorSquareButtonWithImage;
import org.example.buttons.ConfirmOrderButton;
import org.example.buttons.KeyboardButton;
import org.example.buttons.KeyboardButtonPrim;
import org.example.buttons.KioskName;
import org.example.buttons.LangBtn;
import org.example.buttons.MidLabelSec;
import org.example.buttons.TitleLabel;
import org.example.kiosk.LabelManager;
import org.example.kiosk.LanguageSetting;

/**
 * Screen for customizing and testing the kiosk design.
 */
public class CustomizationScreen {

  /**
   * Admin menu screen.
   */
  public CustomScene showCustomizationScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene) {

    // The mainlayout
    VBox adminMenuLayout = new VBox(20);
    adminMenuLayout.setAlignment(Pos.TOP_CENTER);
    adminMenuLayout.setPadding(new Insets(10));

    // Label to prompt name change
    Label promptInput = new Label("Change Kiosk Name here: ");
    promptInput.setStyle(
        "-fx-font-size: 25px;"
            + "-fx-font-weight: bold;");
    LabelManager.register(promptInput);

    // Textfield for the name change
    TextField nameInput = new TextField();
    nameInput.setMaxWidth(300);
    nameInput.setAlignment(Pos.CENTER);
    nameInput.setText("Current name: " + KioskName.getCompanyTitle());

    // Saves the name into KioskName.java
    Button saveNameButton = new Button("Save name");
    saveNameButton.setOnAction(e -> {

      String newName = nameInput.getText().trim();

      if (!newName.isEmpty()) {

        KioskName.setCompanyTitle(newName);

      }

    });

    // Wraps all the name stuff
    VBox nameBox = new VBox(10, promptInput, nameInput, saveNameButton);
    nameBox.setAlignment(Pos.CENTER);

    // Colopickers moved to a separate file
    var colorPickers = new ColorPickersPane(
        primaryStage,
        windowWidth,
        windowHeight,
        welcomeScrScene,
        TitleLabel.getTextColor(),
        ColorButtonWithImage.getButtonColor(),
        BackgroundColorStore.getCurrentBackgroundColor());

    // Button to save selected color scheme
    Button saveColorsBtn = new Button();
    saveColorsBtn.setText("Save Color Scheme");
    saveColorsBtn.setOnAction(e -> {

      try {

        // Gets current colors and saves to file
        ColorSettingManager.saveColors(
            colorPickers.getPrimaryColorPicker().getValue(),
            colorPickers.getSecondaryColorPicker().getValue(),
            colorPickers.getBackgroundColorPicker().getValue());

        System.out.println("Scheme saved");

      } catch (IOException ex) {

        ex.printStackTrace();

      }

    });

    // Button to reset saved color scheme to our baseline scheme
    Button resetColorsBtn = new Button();
    resetColorsBtn.setText("Reset Color Scheme");
    resetColorsBtn.setOnAction(e -> {

      try {

        // Writes default scheme to file
        ColorSettingManager.resetToDefaults();

        // Fetches colors from file
        Color[] defaults = ColorSettingManager.loadColors();

        if (defaults != null) {

          // Sets color picker defaults
          colorPickers.getPrimaryColorPicker().setValue(defaults[0]);
          colorPickers.getSecondaryColorPicker().setValue(defaults[1]);
          colorPickers.getBackgroundColorPicker().setValue(defaults[2]);

          // And sets the scheme to the running program
          LabelManager.setTextColor(defaults[0]);
          TitleLabel.setTextColor(defaults[0]);
          BlackButtonWithImage.setButtonBackgroundColor(defaults[0]);
          ColorSquareButtonWithImage.setButtonColor(defaults[0]);
          CartSquareButton.setButtonColor(defaults[0]);
          ColorBtnOutlineImage.setButtonColor(defaults[0]);
          ArrowButton.setButtonColor(defaults[0]);
          ConfirmOrderButton.setButtonBackgroundColor(defaults[0]);
          CircleButtonWithSign.setPlusColor(defaults[0]);
          CircleButtonWithSign.setMinusBorder(defaults[0]);
          ;
          ColorButtonWithImage.setButtonBackgroundColor(defaults[1]);
          CircleButtonWithSign.setMinusBackground(defaults[1]);
          BackgroundColorStore.setCurrentBackgroundColor(defaults[2]);

          KeyboardButtonPrim.setButtonBackgroundColor(defaults[1]);
          KeyboardButton.setButtonBackgroundColor(defaults[0]);
          MidLabelSec.setTextColor(defaults[1]);

        }

        System.out.println("Scheme reset");

        // Rerenders this scene to apply new background color
        Scene statsScene = new CustomizationScreen().showCustomizationScreen(
            primaryStage, windowWidth, windowHeight, welcomeScrScene);
        primaryStage.setScene(statsScene);

      } catch (IOException ex) {

        ex.printStackTrace();

      }

    });

    // Custom keyboard for textfields
    CustomKeyboard keyboard = new CustomKeyboard(primaryStage, nameInput);

    // Keyboard functionality for username and password fields
    nameInput.setOnMouseClicked(e -> {
      keyboard.setTargetInput(nameInput);
      keyboard.show();
      Platform.runLater(() -> {
        nameInput.requestFocus();
        nameInput.positionCaret(nameInput.getText().length());
      });
    });

    // Wraps the management buttons
    HBox colorManagement = new HBox();
    colorManagement.setSpacing(50);
    colorManagement.setAlignment(Pos.CENTER);
    colorManagement.getChildren().addAll(resetColorsBtn, saveColorsBtn);

    // Making the title on top of the admin menu screen
    Label adminMenuText = new TitleLabel("Set & Test Design");

    // Wraps top part of the screen
    adminMenuLayout.getChildren().addAll(adminMenuText, nameBox, colorPickers, colorManagement);

    // this gridpane is used for all the middle buttons in the admin menu,
    // to align tem properly in rows and columns.
    GridPane centerGrid = new GridPane();
    centerGrid.setHgap(50);
    centerGrid.setVgap(30);
    centerGrid.setAlignment(Pos.CENTER);

    // Test Buttons
    var testBtn1 = new ColorBtnOutlineImage("Outline", "/back.png");
    var testBtn2 = new ColorButtonWithImage("With Image", "/eatHere.png");
    var testBtn3 = new BlackButtonWithImage("BlBtn", "/eatHere.png");
    var testBtn4 = new ColorSquareButtonWithImage("With Image", "/back.png");

    centerGrid.add(testBtn1, 0, 0);
    centerGrid.add(testBtn4, 0, 1);
    centerGrid.add(testBtn2, 1, 0);
    centerGrid.add(testBtn3, 1, 1);

    // Adding the language button which already has the functionality of
    // changing the logo of the language
    var langButton = new LangBtn();
    HBox bottomLeftBox = new HBox(langButton);
    bottomLeftBox.setAlignment(Pos.BOTTOM_LEFT);

    // Spacer for bottom part of the screen
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Similar to a normal cancel button, it just has text under the image
    ColorSquareButtonWithImage cancelBtn = new ColorSquareButtonWithImage("Cancel", "/cancel.png");
    HBox bottomRightBox = new HBox(cancelBtn);
    bottomRightBox.setAlignment(Pos.BOTTOM_RIGHT);

    // go back to the main screen if clicked
    // reinstantiates welcome screen, so the color change takes effect
    cancelBtn.setOnAction(e -> {

      // Close the keyboard when going back to the welcome screen
      keyboard.close();

      try {

        CustomScene welcomeScreen = new WelcomeScreen().createWelcomeScreen(primaryStage,
            windowWidth, windowHeight);
        primaryStage.setScene(welcomeScreen);

      } catch (SQLException ex) {

        ex.printStackTrace();

      }

    });

    // Wraps bottom part of the screen
    HBox bottomLayout = new HBox();
    bottomLayout.setPadding(new Insets(0, 0, 0, 0));
    bottomLayout.getChildren().addAll(bottomLeftBox, spacerBottom, bottomRightBox);

    // Assigning the positions of elements for the Admin menu screen
    BorderPane mainBorderPane = new BorderPane();
    mainBorderPane.setPadding(new Insets(50));
    mainBorderPane.setTop(adminMenuLayout);
    mainBorderPane.setCenter(centerGrid);
    mainBorderPane.setBottom(bottomLayout);

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
      lang.translateLabels(mainBorderPane);
    });

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(mainBorderPane);
    lang.translateLabels(mainBorderPane);

    var customizationScene = new CustomScene(mainBorderPane, windowWidth, windowHeight);

    // Reads and applies the customized background color
    Color bgColor = BackgroundColorStore.getCurrentBackgroundColor();

    if (bgColor != null) {

      customizationScene.setBackgroundColor(bgColor);

    }

    return customizationScene;
  }
}