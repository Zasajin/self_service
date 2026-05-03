package org.example.boxes;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.example.buttons.CircleButtonWithSign;
import org.example.kiosk.LabelManager;

/**
 * Class for the AddRemoveBlock. Includes a label with quantity and two buttons
 * for adding and removing items.
 */
public class AddRemoveBlock extends HBox {
  private Label quantityLabel;
  private CircleButtonWithSign minusButton;
  private CircleButtonWithSign plusButton;
  private int quantity;

  // Listener to notify parent when quantity changes
  private QuantityChangeListener quantityChangeListener;

  /**
   * Class for the AddRemoveBlock. Includes a label with quantity and two buttons
   * for adding and removing items.
   */
  public AddRemoveBlock(int initialQuantity) {
    this.quantity = Math.max(0, Math.min(initialQuantity, 9));

    quantityLabel = new Label(String.valueOf(quantity));
    LabelManager.register(quantityLabel);
    minusButton = new CircleButtonWithSign("-");
    plusButton = new CircleButtonWithSign("+");

    setupButtonActions();

    this.setSpacing(10);
    this.setAlignment(Pos.CENTER);
    this.getChildren().addAll(minusButton, quantityLabel, plusButton);

    updateButtonStates();
  }

  public void setQuantity(int newQuantity) {
    this.quantity = newQuantity;
  }

  private void setupButtonActions() {
    minusButton.setOnAction(e -> {
      if (quantity > 0) {
        quantity--;
        quantityLabel.setText(String.valueOf(quantity));
        updateButtonStates();
        notifyQuantityChanged();
      }
    });

    plusButton.setOnAction(e -> {
      if (quantity < 9) {
        quantity++;
        quantityLabel.setText(String.valueOf(quantity));
        updateButtonStates();
        notifyQuantityChanged();
      }
    });
  }

  private void updateButtonStates() {
    minusButton.setInvalid(quantity <= 0);
    plusButton.setInvalid(quantity >= 9);
  }

  // Notify the listener about quantity changes
  private void notifyQuantityChanged() {
    if (quantityChangeListener != null) {
      quantityChangeListener.onQuantityChanged(quantity);
    }
  }

  // Set listener to handle quantity change events
  public void setQuantityChangeListener(QuantityChangeListener listener) {
    this.quantityChangeListener = listener;
  }

  /**
   * Listener for the quantity.
   */
  public interface QuantityChangeListener {
    /**
     * Used for quantity change in +/- buttons.
     * Other classes use this as callback when the quantities change.
     *
     * @param newQuantity updated quantity value (<=0 and >=9)
     */
    void onQuantityChanged(int newQuantity);
  }

  // Getter for quantity
  public int getQuantity() {
    return quantity;
  }
}
