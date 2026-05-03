package org.example.boxes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.buttons.KeyboardButton;
import org.example.buttons.KeyboardButtonPrim;
import org.example.buttons.MidLabelSec;
import org.example.kiosk.InactivityTimer;

/**
 * A custom on-screen keyboard for our app.
 */
public class CustomKeyboard {

  private Stage keyboardStage = new Stage();
  private boolean[] isShiftPressed = { false };
  private TextInputControl targetInput;
  private List<Button> allKeyButtons = new ArrayList<>();

  private static final Map<String, String> shiftMap = Map.of(
      "1", "!", "2", "@", "3", "#", "4", "%", "5", ".",
      "6", ",", "7", "-", "8", "_", "9", "(", "0", ")");

  // Make the keyboard movable
  private double dragOffsetX;
  private double dragOffsetY;

  /**
   * Constructs a CustomKeyboard instance.
   */
  public CustomKeyboard(Stage primaryStage, TextInputControl inputField) {
    this.targetInput = inputField;
    Platform.runLater(this::initializeKeyboardUi);
    primaryStage.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
      if (!isNowShowing) {
        System.out.println("Main window hidden. Closing keyboard.");
        close();
      }
    });
  }

  private void initializeKeyboardUi() {
    keyboardStage.initModality(Modality.NONE);
    keyboardStage.initStyle(StageStyle.TRANSPARENT);
    keyboardStage.setAlwaysOnTop(true);

    VBox keyboardLayout = new VBox(10);
    keyboardLayout.setPadding(new Insets(20));
    keyboardLayout.setAlignment(Pos.CENTER);
    keyboardLayout.setStyle(
        "-fx-background-color: rgba(255, 255, 255, 0.7);"
            + "-fx-border-color: rgba(255, 255, 255, 0.5);"
            + "-fx-border-width: 3px;"
            + "-fx-border-radius: 20;"
            + "-fx-background-radius: 20;");

    // Add a label to indicate drag functionality
    var dragLabel = new MidLabelSec("Drag me to move");
    keyboardLayout.getChildren().add(dragLabel);

    List<String[]> keyRows = List.of(
        new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "⌫" },
        new String[] { "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "å" },
        new String[] { "a", "s", "d", "f", "g", "h", "j", "k", "l", "ö", "ä" },
        new String[] { "Shift", "z", "x", "c", "v", "b", "n", "m", "Close" },
        new String[] { "Space" });

    for (String[] row : keyRows) {
      HBox rowBox = new HBox(5);
      rowBox.setAlignment(Pos.CENTER);
      for (String key : row) {
        Button btn = createKeyButton(key);
        rowBox.getChildren().add(btn);
      }
      keyboardLayout.getChildren().add(rowBox);
    }

    StackPane root = new StackPane(keyboardLayout);
    root.setStyle("-fx-background-color: transparent;");
    Scene scene = new Scene(root);
    scene.setFill(Color.TRANSPARENT);

    keyboardStage.setScene(scene);

    // Make it movable
    root.setOnMousePressed(event -> {
      dragOffsetX = event.getSceneX();
      dragOffsetY = event.getSceneY();
    });

    root.setOnMouseDragged(event -> {
      keyboardStage.setX(event.getScreenX() - dragOffsetX);
      keyboardStage.setY(event.getScreenY() - dragOffsetY);
    });

    // Set size based on contents
    keyboardStage.sizeToScene();

    // Add event filters for inactivity timer reset
    root.addEventFilter(MouseEvent.ANY, ev -> InactivityTimer.getInstance().resetTimer());
    root.addEventFilter(KeyEvent.ANY, ev -> InactivityTimer.getInstance().resetTimer());
  }

  private Button createKeyButton(String key) {
    Button button;

    switch (key) {
      case "⌫":
      case "Close":
      case "Shift":
        button = new KeyboardButtonPrim(key);
        break;
      default:
        button = new KeyboardButton(key);
    }
    button.setFont(Font.font("Arial", 18));
    button.setMinSize(60, 40);

    switch (key) {
      case "Space":
        // Make space button larger
        button.setMinWidth(400);
        break;
      case "⌫":
        // Make backspace button slightly larger
        button.setMinWidth(80);
        break;
      case "Shift":
      case "Close":
        button.setMinWidth(80);
        break;
      default:
        // no special sizing for other keys
        break;
    }

    button.setOnAction(e -> {
      handleKeyPress(key);
      // reset inactivity timer on key press
      InactivityTimer.getInstance().resetTimer();
    });

    // Add event filters for inactivity timer reset
    button.addEventFilter(MouseEvent.ANY, ev -> InactivityTimer.getInstance().resetTimer());
    button.addEventFilter(KeyEvent.ANY, ev -> InactivityTimer.getInstance().resetTimer());

    allKeyButtons.add(button);
    return button;
  }

  public void setTargetInput(TextInputControl inputField) {
    this.targetInput = inputField;
  }

  private void handleKeyPress(String key) {
    // String currentText = targetInput.getText();
    int caretPosition = targetInput.getCaretPosition();

    switch (key) {
      case "Space":
        targetInput.insertText(caretPosition, " ");
        break;

      case "⌫":
        if (caretPosition > 0) {
          targetInput.deleteText(caretPosition - 1, caretPosition);
        }
        break;

      case "Shift":
        isShiftPressed[0] = !isShiftPressed[0];
        updateKeyLabels();
        break;

      case "Close":
        keyboardStage.close();

        // Return focus to a safe UI element to re-enable clickability
        if (targetInput.getScene() != null && targetInput.getScene().getRoot() != null) {
          targetInput.getScene().getRoot().requestFocus();
        }

        break;

      default:
        String toInsert;
        if (isShiftPressed[0]) {
          toInsert = shiftMap.getOrDefault(key, key.toUpperCase());
        } else {
          toInsert = key.toLowerCase();
        }
        targetInput.insertText(caretPosition, toInsert);
        break;

    }
  }

  private void updateKeyLabels() {
    for (Button button : allKeyButtons) {
      String text = button.getText();
      if (text.length() == 1) {
        if (Character.isLetter(text.charAt(0))) {
          button.setText(isShiftPressed[0] ? text.toUpperCase() : text.toLowerCase());
        } else if (shiftMap.containsKey(text) || shiftMap.containsValue(text)) {
          // Reverse mapping for visual toggle
          if (isShiftPressed[0]) {
            for (Map.Entry<String, String> entry : shiftMap.entrySet()) {
              if (entry.getKey().equals(text)) {
                button.setText(entry.getValue());
                break;
              }
            }
          } else {
            for (Map.Entry<String, String> entry : shiftMap.entrySet()) {
              if (entry.getValue().equals(text)) {
                button.setText(entry.getKey());
                break;
              }
            }
          }
        }
      }
    }
  }

  /**
   * Displays the custom keyboard if it is not already visible.
   */
  public void show() {

    if (!keyboardStage.isShowing()) {
      keyboardStage.show();

      Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
      double x = (screenBounds.getWidth() - keyboardStage.getWidth()) / 2;
      double y = screenBounds.getHeight() - keyboardStage.getHeight() - 140;
      keyboardStage.setX(x);
      keyboardStage.setY(y);
    }
  }

  /**
   * Closes the custom keyboard if it is currently visible.
   */
  public void close() {
    if (keyboardStage.isShowing()) {
      keyboardStage.close();
    }
  }

}
