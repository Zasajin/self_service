package org.example.boxes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.buttons.ArrowButton;
import org.example.kiosk.LabelManager;
import org.example.kiosk.LanguageSetting;
import org.example.menu.Product;

/**
 * The grid with the arrow buttons class.
 */
public class CheckoutGridWithButtons extends HBox {

  private GridPane itemGrid;
  private Label pageCounterLabel;
  private Button leftArrowButton;
  private Button rightArrowButton;
  private int itemsPerPage;
  private SimpleIntegerProperty currentPage;
  private Product[] items;
  private int[] quantitys;

  /**
   * Constructor for the CheckoutGridWithButtons instance.
   */
  public CheckoutGridWithButtons(Product[] items, int[] quantitys, int itemsPerPage) {
    this.items = items;
    this.quantitys = quantitys;
    this.itemsPerPage = itemsPerPage;
    this.leftArrowButton = new ArrowButton(true, false);
    this.rightArrowButton = new ArrowButton(false, false);
    this.currentPage = new SimpleIntegerProperty(0);
    this.itemGrid = new GridPane();
    this.pageCounterLabel = new Label();
    initialize();
  }

  // Initialize layout and functionality
  private void initialize() {
    // Setup HBox properties
    this.setAlignment(Pos.CENTER);
    this.setSpacing(20);

    // Setup GridPane for items display
    itemGrid.setHgap(20);
    itemGrid.setVgap(20);
    itemGrid.setAlignment(Pos.CENTER);

    // Setup page counter label
    pageCounterLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");
    LabelManager.register(pageCounterLabel);

    // Setup actions for the left navigation button
    leftArrowButton.setOnAction(e -> {
      if (currentPage.get() > 0) {
        currentPage.set(currentPage.get() - 1);
        updateGrid();
      }
    });

    // And the right navigation button
    rightArrowButton.setOnAction(e -> {
      if ((currentPage.get() + 1) * itemsPerPage < items.length) {
        currentPage.set(currentPage.get() + 1);
        updateGrid();
      }
    });

    // Update the grid when created
    updateGrid();

    // Setup the layout
    VBox gradWithCounter = new VBox(20);
    gradWithCounter.setAlignment(Pos.CENTER);
    gradWithCounter.getChildren().addAll(pageCounterLabel, itemGrid);

    // Create spacers for arrows positioning
    Region leftSpacer = new Region();
    Region rightSpacer = new Region();

    HBox.setHgrow(leftSpacer, Priority.ALWAYS);
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);

    // Add everything to HBox
    this.getChildren().addAll(
        leftArrowButton, leftSpacer,
        gradWithCounter,
        rightSpacer, rightArrowButton);
  }

  // Update grid content based on the current page
  private void updateGrid() {
    // Clear previous items
    itemGrid.getChildren().clear();

    int pageStartIndex = currentPage.get() * itemsPerPage;
    int pageEndIndex = Math.min(pageStartIndex + itemsPerPage, items.length);

    // Populate the grid with the items
    for (int i = pageStartIndex; i < pageEndIndex; i++) {
      Product item = items[i];
      Image itemImage = new Image(item.getImagePath());
      ImageView image = new ImageView(itemImage);
      image.setFitHeight(150);
      // image.setFitWidth(150);
      image.setPreserveRatio(true);

      // Slot for Label and Price
      HBox labelAndPrice = new HBox();
      labelAndPrice.setAlignment(Pos.CENTER);
      Label nameLabel = new Label(item.getName());
      Label priceLabel = new Label(String.format(" %.0f :-", item.getPrice()));

      // To change color with picker
      LabelManager.register(nameLabel);
      LabelManager.register(priceLabel);

      labelAndPrice.getChildren().addAll(nameLabel, priceLabel);

      // Slot for Plus/Minus Buttons and Quantity value
      int quantity = quantitys[i];
      HBox quantityBox = new HBox();
      quantityBox.setAlignment(Pos.CENTER);

      // Create AddRemoveBlock and set the listener
      final int itemIndex = i;
      AddRemoveWithIndex addRemoveBlock = new AddRemoveWithIndex(items, quantity, itemIndex);
      addRemoveBlock.setQuantityChangeListener(new AddRemoveWithIndex.QuantityChangeListener() {
        @Override
        public void onQuantityChanged(int newQuantity) {
          // Use itemIndex to update the quantity
          quantitys[itemIndex] = newQuantity;
          // Rerender grid after updating the quantity
          updateGrid();
        }
      });

      quantityBox.getChildren().addAll(addRemoveBlock);

      // Adding it all together in one item slot
      VBox itemSlot = new VBox();
      itemSlot.setAlignment(Pos.CENTER);
      itemSlot.getChildren().addAll(
          image,
          labelAndPrice,
          quantityBox);

      // Compute column and row from index
      int column = (i - pageStartIndex) % 3;
      int row = (i - pageStartIndex) / 3;
      itemGrid.add(itemSlot, column, row);
    }

    LanguageSetting.getInstance().translateLabels(itemGrid);

    // Add empty slots for grid shape
    int totalItems = pageEndIndex - pageStartIndex;
    for (int i = totalItems; i < itemsPerPage; i++) {
      StackPane emptySlot = new StackPane();
      emptySlot.setPrefSize(200, 200);
      emptySlot.setMaxSize(200, 200);
      emptySlot.setMinSize(200, 200);
      itemGrid.add(emptySlot, i % 3, i / 3);
    }

    // Update page count
    pageCounterLabel.setText(
        String.format("Page %d  of  %d",
            currentPage.get() + 1,
            (int) Math.ceil((double) items.length / itemsPerPage)));

    // Disable left button if on the first page
    if (currentPage.get() == 0) {
      leftArrowButton.setDisable(true);
      leftArrowButton.setOpacity(0.2);
    } else {
      leftArrowButton.setDisable(false);
      leftArrowButton.setOpacity(1);
    }

    // Disable right button if there are no more items beyond the current page
    if ((currentPage.get() + 1) * itemsPerPage >= items.length) {
      rightArrowButton.setDisable(true);
      rightArrowButton.setOpacity(0.2);
    } else {
      rightArrowButton.setDisable(false);
      rightArrowButton.setOpacity(1);
    }

    // If there are no items show 0 pages
    if (items.length == 0) {
      pageCounterLabel.setText("Page 0 of 0");
      leftArrowButton.setDisable(true);
      rightArrowButton.setDisable(true);
    }
  }

  public Label getPageCounterLabel() {
    return pageCounterLabel;
  }
}
