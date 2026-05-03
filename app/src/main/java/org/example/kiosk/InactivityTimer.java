package org.example.kiosk;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.boxes.CustomKeyboard;
import org.example.buttons.MidButton;
import org.example.orders.Cart;

/**
 * This is the timer that starts counting after a
 * certain amount of inactivity on the terminal.
 * It will also reset the terminal if nothing is
 * clicked during an activity check.
 */
public class InactivityTimer {

  private static InactivityTimer instance;
  private Timer timer;
  private Stage primaryStage;
  private Scene welcomeScene;
  private boolean isActive = false;
  private int inactivityTimePopup = 10;
  private int inactivityTimeShop = 20;
  private CustomKeyboard keyboard;

  /**
   * The timer constructor.
   * No need for extra variables as this is a singleton.
   */
  private InactivityTimer() {
  }

  /**
   * Method to get the single instance of the
   * InactivityTimer.
   *
   * @return the InactivityTimer instance
   */
  public static InactivityTimer getInstance() {
    if (instance == null) {
      instance = new InactivityTimer();
    }
    return instance;
  }

  /**
   * Setter for the primary stage to compare if the current stage matches the
   * welcome screen Scene or not.
   *
   * @param primaryStage current stage
   */
  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  /**
   * Setter method for welcone scene. This scene will be used to fully
   * reset to the welcome screen if the inactivity of the user gets
   * confirmed. It is also used to compare the current stage.
   *
   * @param welcomeScene Welcome screen of the kiosk
   */
  public void setWelcomeScene(Scene welcomeScene) {
    this.welcomeScene = welcomeScene;
  }

  /**
   * Start the timer.
   * Timer has sceduled delay in miliseconds, so we
   * convert to seconds -> e.g. 30 * 1000 = 30 seconds.
   * After time has passed, inactivity popup will be
   * displayed (TimerTask).
   */
  public void startTimer() {

    // Timer only active in Scenes that aren't the WelcomeScreen
    // and timer cannot be already active
    if (!isActive && !(primaryStage.getScene().equals(welcomeScene))) {
      timer = new Timer(true);
      timer.schedule(displayInactivityPopup(), inactivityTimeShop * 1000);
      isActive = true;
    }
  }

  /**
   * The timer task is an inactivity popup, asking
   * 'Are you still there?'.
   * Uses private helper methods for popup.
   *
   * @return TimerTask containing popup
   */
  private TimerTask displayInactivityPopup() {
    return new TimerTask() {
      public void run() {
        inactivityPopup();
      }
    };
  }

  /**
   * Helper method for displayInactivityPopup().
   * This method contains the inactivity popup itself.
   */
  private void inactivityPopup() {

    // Run action after time; needed to prevent crashes
    Platform.runLater(() -> {

      // Make the popup
      Stage inactivityPopup = new Stage();

      // Set stage the popup will go over
      inactivityPopup.initOwner(primaryStage);

      // User cannot interact with anything else until popup is gone
      inactivityPopup.initModality(Modality.APPLICATION_MODAL);

      // No title section for popup
      inactivityPopup.initStyle(StageStyle.UNDECORATED);

      // Label of the popup
      Label label = new Label("Are you still there?");
      label.setStyle(
          "-fx-font-weight: bold;"
              + "-fx-font-size: 25px;");

      // Button for confirmation that user is still active
      MidButton button = new MidButton("Yes", "green", 40);

      // Make timer for full reset if user doesn't respond to popup after 5 seconds
      // User has 5 seconds to respond
      Timer popupTimer = new Timer(true);
      popupTimer.schedule(
          getFullResetTask(inactivityPopup, popupTimer),
          inactivityTimePopup * 1000);

      // User confirms he's still active
      // -> popup gets removed and all timers reset
      button.setOnAction(e -> {
        inactivityPopup.close();
        popupTimer.cancel();
        popupTimer.purge();
        resetTimer();
      });

      // Layout and style of popup elements
      VBox layout = new VBox(20, label, button);
      layout.setAlignment(Pos.CENTER);
      layout.setStyle(
          "-fx-background-color: white;"
              + "-fx-border-color: black;"
              + "-fx-border-width: 3px;"
              + "-fx-border-radius: 20;"
              + "-fx-background-radius: 20;"
              + "-fx-padding: 30;");

      // Outer part to center layout on screen and have round edges properly visible
      StackPane finalLayout = new StackPane(layout);
      finalLayout.setStyle("-fx-background-color: transparent;");
      finalLayout.setPrefSize(400, 200);

      // Translate the whole layout before rendering
      LanguageSetting lang = LanguageSetting.getInstance();
      lang.registerRoot(finalLayout);
      lang.translateLabels(finalLayout);

      // Set finished scene in popup
      Scene scene = new Scene(finalLayout);
      scene.setFill(Color.TRANSPARENT);

      inactivityPopup.initStyle(StageStyle.TRANSPARENT);
      inactivityPopup.setScene(scene);
      inactivityPopup.show();
    });
  }

  /**
   * Method to reset the inactivity timer and re-start
   * the timer.
   */
  public void resetTimer() {

    // Skip if timer wasn't even active to prevent unwanted timer start
    if (!isActive) {
      return;
    }

    // Timer only active in Scenes that aren't the WelcomeScreen
    if (!(primaryStage.getScene().equals(welcomeScene))) {
      // Terminate the timer
      timer.cancel();
      isActive = false;

      // Make a new timer and start it
      startTimer();
    }
  }

  /**
   * Method to stop the inactivity timer completely.
   */
  public void stopTimer() {
    // Execute only if timer already exists
    if (timer != null) {
      // Cancel timer completely and remove instance
      timer.cancel();
      timer = null;
    }

    // Always reset activity status
    isActive = false;
  }

  /**
   * Helper method to the part of the reset to the main menu
   * screen if the user is unresponsive in the popup.
   * The method contains the TimerTask for the inactivity
   * popup.
   * If the timer for the popup runs out, the cart gets cleared, popup
   * gets closed, all timers get reset and the scene will get set back to the
   * welcome scene.
   *
   * @param inactivityPopup Popup confirming the user is still active
   * @param popupTimer      Timer for the popup and the full reset to the main
   *                        menu screen
   * @return TimerTask for the reset if user remains inactive during the popup and
   *         the timer ran out
   */
  private TimerTask getFullResetTask(Stage inactivityPopup, Timer popupTimer) {

    TimerTask resetTask = new TimerTask() {

      // What the task does upon executing
      public void run() {
        Platform.runLater(() -> {
          // Execute only after inactivity popup is visible
          if (inactivityPopup.isShowing()) {
            inactivityPopup.close();
            Cart.getInstance().clearCart();
            primaryStage.setScene(welcomeScene);
            keyboard.close();
            stopTimer();
            popupTimer.cancel();
            popupTimer.purge();
          }
        });
      }
    };

    return resetTask;
  }

  /**
   * Get the keyboards instance to reset it later when
   * the timer runs out.
   *
   * @param keyboard The custom keyboard
   */
  public void getKeyboardInstance(CustomKeyboard keyboard) {
    this.keyboard = keyboard;
  }

  /**
   * Setter for editing the general inacitvity timer in the
   * kiosk.
   *
   * @param newShopInactivityTimer new int time in seconds
   *                               allowed inactivity until popup checker
   */
  public void setNewInactivityTimer(int newShopInactivityTimer) {
    this.inactivityTimeShop = newShopInactivityTimer;
  }

  /**
   * Getter for editing the general inacitvity timer in the
   * kiosk.
   */
  public int getInactivityTimer() {
    return inactivityTimeShop;
  }

  /**
   * Setter for editing the inactivity timer of the popup that
   * checks if the user is still active.
   *
   * @param newPopupInactivityTimer new int time in seconds
   *                                allowed inactivity in popup until full reset
   */
  public void setNewInactivityTimerPopup(int newPopupInactivityTimer) {
    this.inactivityTimePopup = newPopupInactivityTimer;
  }

  /**
   * Getter for editing the inacitvity timer in the
   * popup of the kiosk.
   */
  public int getInactivityTimerPopup() {
    return inactivityTimePopup;
  }
}
