package org.example.buttons;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.kiosk.LabelManager;
import org.example.screens.BackgroundColorStore;
import org.example.screens.CustomizationScreen;

/**
 * A reusable component for the three color pickers used in CustomizationScreen.
 */
public class ColorPickersPane extends HBox {
  private ColorPicker primClrPicker;
  private ColorPicker secClrPicker;
  private ColorPicker sceneColorPicker;

  /**
   * Constructs a ColorPickersPane with
   * three color pickers for primary, secondary, and background colors.
   */
  public ColorPickersPane(
      Stage primaryStage,
      Double windowWidth,
      Double windowHeight,
      Scene welcomeScrScene,
      Color initialPrimeColor,
      Color initialSecColor,
      Color initialBackgrounColor) {

    setSpacing(40);
    setAlignment(Pos.CENTER);

    // Primary Color Picker
    Label primClrLabel = new Label("Prime color: ");
    primClrLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
    primClrPicker = new ColorPicker(initialPrimeColor);
    primClrPicker.setPrefSize(200, 50);
    VBox primColorVbox = new VBox(5, primClrLabel, primClrPicker);
    primColorVbox.setAlignment(Pos.CENTER);

    // Secondary Color Picker
    Label secPickerLbl = new Label("Secondary color: ");
    secPickerLbl.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
    secClrPicker = new ColorPicker(initialSecColor);
    secClrPicker.setPrefSize(200, 50);
    VBox secColorVbox = new VBox(5, secPickerLbl, secClrPicker);
    secColorVbox.setAlignment(Pos.CENTER);

    // Background Color Picker
    Label scenePicker = new Label("Background color: ");
    scenePicker.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
    sceneColorPicker = new ColorPicker(initialBackgrounColor);
    sceneColorPicker.setPrefSize(200, 50);
    VBox sceneColorVbox = new VBox(5, scenePicker, sceneColorPicker);
    sceneColorVbox.setAlignment(Pos.CENTER);

    getChildren().addAll(primColorVbox, secColorVbox, sceneColorVbox);

    // Setup color change listeners
    setupListeners(primaryStage, windowWidth, windowHeight, welcomeScrScene);
  }

  private void setupListeners(
      Stage primaryStage,
      Double windowWidth,
      Double windowHeight,
      Scene welcomeScrScene) {

    primClrPicker.setOnAction(e -> {
      Color selectedColor = primClrPicker.getValue();
      BlackButtonWithImage.setButtonBackgroundColor(selectedColor);
      ColorSquareButtonWithImage.setButtonColor(selectedColor);
      TitleLabel.setTextColor(selectedColor);
      CartSquareButton.setButtonColor(selectedColor);
      ColorBtnOutlineImage.setButtonColor(selectedColor);
      ArrowButton.setButtonColor(selectedColor);
      ConfirmOrderButton.setButtonBackgroundColor(selectedColor);
      CircleButtonWithSign.setPlusColor(selectedColor);
      CircleButtonWithSign.setMinusBorder(selectedColor);
      KeyboardButtonPrim.setButtonBackgroundColor(selectedColor);
      MidLabelSec.setTextColor(selectedColor);
      LabelManager.setTextColor(selectedColor);
    });

    secClrPicker.setOnAction(e -> {
      Color selectedColor = secClrPicker.getValue();
      ColorButtonWithImage.setButtonBackgroundColor(selectedColor);
      CircleButtonWithSign.setMinusBackground(selectedColor);
      KeyboardButton.setButtonBackgroundColor(selectedColor);
    });

    sceneColorPicker.setOnAction(e -> {
      Color selectedColor = sceneColorPicker.getValue();
      BackgroundColorStore.setCurrentBackgroundColor(selectedColor);

      Scene statsScene = new CustomizationScreen().showCustomizationScreen(
          primaryStage, windowWidth, windowHeight, welcomeScrScene);
      primaryStage.setScene(statsScene);

    });
  }

  // You can add getters if needed to expose the ColorPickers externally
  public ColorPicker getPrimaryColorPicker() {
    return primClrPicker;
  }

  public ColorPicker getSecondaryColorPicker() {
    return secClrPicker;
  }

  public ColorPicker getBackgroundColorPicker() {
    return sceneColorPicker;
  }
}
