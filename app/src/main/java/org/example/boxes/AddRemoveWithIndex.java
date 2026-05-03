package org.example.boxes;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.example.buttons.CircleButtonWithSign;
import org.example.menu.Product;
import org.example.orders.Cart;

/**
 * Class for the AddRemoveWithIndex. Includes a label with quantity and two
 * buttons for adding and removing items. This version uses an index for
 * identifying the item.
 */
public class AddRemoveWithIndex extends HBox {

  private Label quantityLabel;
  private CircleButtonWithSign minusButton;
  private CircleButtonWithSign plusButton;
  private int quantity;
  private int itemIndex; // To track the item index in the array
  private Product[] items; // Reference to the items array

  // Listener to notify parent when quantity changes
  private QuantityChangeListener quantityChangeListener;

  /**
   * Constructor for AddRemoveWithIndex.
   */
  public AddRemoveWithIndex(Product[] items, int initialQuantity, int itemIndex) {
    this.items = items; // Assign items to the instance variable
    this.quantity = Math.max(0, Math.min(initialQuantity, 9));
    this.itemIndex = itemIndex;

    quantityLabel = new Label(String.valueOf(quantity));
    minusButton = new CircleButtonWithSign("-");
    plusButton = new CircleButtonWithSign("+");

    setupButtonActions();

    this.setSpacing(10);
    this.setAlignment(Pos.CENTER);
    this.getChildren().addAll(minusButton, quantityLabel, plusButton);

    updateButtonStates();
  }

  private void setupButtonActions() {
    minusButton.setOnAction(e -> updateQuantity(-1));
    plusButton.setOnAction(e -> updateQuantity(1));
  }

  /**
   * Updates the quantity and handles adding/removing the product to/from the
   * cart.
   */
  private void updateQuantity(int delta) {
    int newQuantity = quantity + delta;
    if (newQuantity >= 0 && newQuantity <= 9) {
      quantity = newQuantity;
      quantityLabel.setText(String.valueOf(quantity));
      updateButtonStates();
      notifyQuantityChanged();

      // Handle cart updates for adding/removing products
      Product item = items[itemIndex];
      if (quantity == 0) {
        Cart.getInstance().removeProduct(item);
      } else if (delta == 1) {
        Cart.getInstance().addProduct(item);
      } else {
        Cart.getInstance().removeProduct(item);
      }
    }
  }

  // Enable/Disable minus button if quantity is out of range 0-9
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
