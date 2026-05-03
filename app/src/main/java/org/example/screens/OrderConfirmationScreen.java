package org.example.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.buttons.TitleLabel;
import org.example.kiosk.LabelManager;
import org.example.kiosk.LanguageSetting;
import org.example.orders.Cart;

/**
 * This screen displays the confirmation of the order
 * after the user has clicked on confirm order.
 * It not only shows a confirmation message but also
 * displays the order number.
 * This is the last scene of a normal order process.
 * After a few seconds, the screen should fade out and
 * the user gets put back into the welcome screen,
 * resetting all data again.
 */
public class OrderConfirmationScreen {

  /**
   * This is the constructor of the order confirmation
   * screen. It will screate a scene for it.
   *
   * @param primaryStage    the primary stage of this scene
   * @param windowWidth     width of window
   * @param windowHeight    height of window
   * @param welcomeScrScene the welcome screen
   * @param orderId         the id of the order (database)
   * @return scene containing the order confirmation
   */
  public CustomScene createOrderConfirmationScreen(
      Stage primaryStage,
      double windowWidth,
      double windowHeight,
      Scene welcomeScrScene,
      int orderId) {

    // Title of the screen -> Order confirmation message
    Label orderConfirmationLabel = new TitleLabel("Ordered Successfully!");
    orderConfirmationLabel.setAlignment(Pos.CENTER);

    // Order id label
    Label orderIdLabel = new Label("Order number: " + orderId);
    orderIdLabel.setStyle(
        "-fx-font-size: 30px;"
            + "-fx-font-weight: normal;");
    LabelManager.register(orderIdLabel);

    // Label for Estimate time of Order
    Label estimateTimeLabel = new Label(
        "Estimate preparation time: " + Cart.getInstance().getEstimateTime());
    estimateTimeLabel.setStyle(
        "-fx-font-size: 30px;"
            + "-fx-font-weight: normal;");
    LabelManager.register(estimateTimeLabel);

    // Combining both labels
    VBox screenLabelBox = new VBox();
    screenLabelBox.setAlignment(Pos.CENTER);
    screenLabelBox.getChildren().addAll(
        orderConfirmationLabel,
        estimateTimeLabel,
        orderIdLabel);

    // Making layout into a stackpane to create fade overlay later on
    StackPane oconfirmStack = new StackPane();
    oconfirmStack.getChildren().addAll(screenLabelBox);

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(oconfirmStack);
    lang.translateLabels(oconfirmStack);

    // Create final scene result
    CustomScene scene = new CustomScene(oconfirmStack, windowWidth, windowHeight);

    // Reads and applies the customized background color
    Color bgColor = BackgroundColorStore.getCurrentBackgroundColor();

    if (bgColor != null) {

      scene.setBackgroundColor(bgColor);

    }

    return scene;
  }
}
