package org.example.buttons;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.example.orders.Order;

/**
 * This button will confirm the order in the checkout screen. The button will be
 * used only in the
 * checkout screen. It will display the total price of the order and its
 * functionality
 * ("Confirm Order") as a label.
 */
public class ConfirmOrderButton extends AnimatedButton {

  private static final List<ConfirmOrderButton> instances = new ArrayList<>();
  private static String buttonBackgroundColor = "rgb(81, 173, 86)";

  private final Label priceLabel;
  private final Label confirmLabel;
  private final Order order;
  
  /**
   * Constructor for the confirm order button.
   */
  public ConfirmOrderButton(Order order) {
    this.setPrefSize(590, 140);
    instances.add(this);
    this.order = order;
    // Price label
    priceLabel = new Label();
    updatePriceLabel();
    priceLabel.setStyle(
        "-fx-text-fill: white;"
            + "-fx-font-weight: normal;"
            + "-fx-font-size: 40;");

    // Confirm text label
    confirmLabel = new Label("Confirm Order");
    confirmLabel.setStyle(
        "-fx-text-fill: white;"
            + "-fx-font-weight: normal;"
            + "-fx-font-size: 40;");

    // Spacer to push labels apart
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    // Layout for labels
    HBox buttonLabel = new HBox();
    buttonLabel.setAlignment(Pos.CENTER);
    buttonLabel.getChildren().addAll(priceLabel, spacer, confirmLabel);
    buttonLabel.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-padding: 20 20;");

    // Set labels as graphic
    this.setGraphic(buttonLabel);
    applyStyle();

    order.addListener(this::updatePriceLabel);
  }

  /**
   * Updates the full price label when initially called and also in case of events
   * such as quantity updates.
   */
  public void updatePriceLabel() {
    priceLabel.setText(
        "Total: " + String.format("%.2f", order.calculatePrice()) + "kr");
  }

  public Label getConfirmLabel() {
    return confirmLabel;
  }

  /**
   * Sets the text of the confirmation label.
   *
   * @param text the new label text
   */
  public void setButtonText(String text) {
    if (confirmLabel != null) {
      confirmLabel.setText(text);
    }
  }

  /**
   * Applies the current style to the button using the shared background color.
   */
  private void applyStyle() {
    this.setStyle(
        "-fx-background-color: " + buttonBackgroundColor + ";"
            + "-fx-border-color: " + buttonBackgroundColor + ";"
            + "-fx-border-radius: 20;"
            + "-fx-background-radius: 20;"
            + "-fx-padding: 10;");
  }

  /**
   * Updates the background color for all ConfirmOrderButton instances.
   *
   * @param color the new Color from the ColorPicker
   */
  public static void setButtonBackgroundColor(Color color) {
    buttonBackgroundColor = toRgbString(color);
    updateAllStyles();
  }

  private static void updateAllStyles() {
    for (ConfirmOrderButton button : instances) {
      button.applyStyle();
    }
  }

  private static String toRgbString(Color color) {
    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);
    return "rgb(" + r + ", " + g + ", " + b + ")";
  }

  public static List<ConfirmOrderButton> getInstances() {
    return instances;
  }
}
