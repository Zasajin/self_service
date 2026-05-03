package org.example.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.MidButton;
import org.example.buttons.RectangleButtonWithText;
import org.example.buttons.SqrBtnWithOutline;
import org.example.buttons.TitleLabel;
import org.example.discounts.GlobalDiscount;

/**
 * Class for making the scene for global discounts.
 */
public class GlobalDiscountScreen {
  private Stage primaryStage;
  private Scene prevScene;
  private double discountPercentage;
  private Label currentDiscountLabel;

  /**
   * Constructor for the scene.
   *
   * @param primaryStage The screen itself.
   * @param prevScene    Previous screen.
   */
  public GlobalDiscountScreen(Stage primaryStage, Scene prevScene) {
    this.primaryStage = primaryStage;
    this.prevScene = prevScene;

  }

  /**
   * Global discount screen.
   *
   * @return Discount screen scene.
   */

  public Scene getGlobalDiscountScreen() {
    // Create title
    TitleLabel title = new TitleLabel("Select A Discount Type");
    HBox topHbox = new HBox(20);
    topHbox.setAlignment(Pos.TOP_CENTER);
    topHbox.getChildren().add(title);
    topHbox.setPadding(new Insets(0, 0, 75, 0));

    // Create main content area
    HBox content = new HBox(100); // Space between the two sections
    currentDiscountLabel = new Label("Current discount " + discountPercentage + "%");
    currentDiscountLabel.setStyle(
        "-fx-background-color: transparent;"
            + "-fx-border-color: red;"
            + "-fx-border-width: 3;"
            + "-fx-border-radius: 12;"
            + "-fx-padding: 10;"
            + "-fx-background-radius: 12;"
            + "-fx-text-fill: red;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 20px");

    // Create discount sections
    VBox itemDiscounts = createDiscountSection("Item Discounts");
    VBox orderDiscounts = createDiscountSection("Order Discounts");
    content.setAlignment(Pos.CENTER);
    content.getChildren().addAll(itemDiscounts, currentDiscountLabel, orderDiscounts);

    // Create back button and apply discount button
    BackBtnWithTxt backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> primaryStage.setScene(prevScene));
    SqrBtnWithOutline applyDiscount = new SqrBtnWithOutline("Apply",
        "/green_tick.png", "rgb(81, 173, 86)");

    // Applying the global discount on clicking "apply"
    applyDiscount.setOnAction(e -> {
      GlobalDiscount discount = new GlobalDiscount();
      discount.applyDiscount(discountPercentage);
      primaryStage.setScene(prevScene);
    });

    HBox bottomBox = new HBox(applyDiscount, backButton);
    bottomBox.setAlignment(Pos.BOTTOM_CENTER);
    bottomBox.setPadding(new Insets(0, 0, 50, 0));

    // Setup main layout
    BorderPane layout = new BorderPane();
    layout.setTop(topHbox);
    layout.setCenter(content);
    layout.setBottom(bottomBox);

    return new Scene(layout, 1920, 1080);
  }

  /**
   * Creates a discount section with title and percentage buttons.
   *
   * @param title The title for the section
   * @return VBox containing the complete discount section
   */
  private VBox createDiscountSection(String title) {
    // Create and style the title button
    MidButton titleButton = new MidButton(title, "rgb(255, 255, 255)", 45);
    titleButton.setPadding(new Insets(0, 0, 0, 10));

    // Textfield for custom percentage input
    TextField customInput = new TextField();
    customInput.setPromptText("Enter percentage (e.g. 15 for 15%)");
    customInput.setMaxWidth(150);
    customInput.setVisible(false);
    customInput.setStyle("-fx-font-size: 16px; -fx-padding: 8px;");

    // Stackpane to put the textfield into
    StackPane inputContainer = new StackPane(customInput);
    inputContainer.setPadding(new Insets(5, 0, 0, 0));
    VBox percentageButtons = new VBox(20);

    // Create buttons
    RectangleButtonWithText fivePercentButton = new RectangleButtonWithText("5%");
    RectangleButtonWithText tenPercentButton = new RectangleButtonWithText("10%");
    RectangleButtonWithText twentyPercentButton = new RectangleButtonWithText("20%");
    RectangleButtonWithText customPercentButton = new RectangleButtonWithText("Custom");

    percentageButtons.getChildren().addAll(
        fivePercentButton,
        tenPercentButton,
        twentyPercentButton,
        customPercentButton,
        inputContainer);
    percentageButtons.setAlignment(Pos.CENTER);
    fivePercentButton.setOnAction(e -> {
      this.discountPercentage = 5;
      updateCurrentDiscountLabel();
    });

    tenPercentButton.setOnAction(e -> {
      this.discountPercentage = 10;
      updateCurrentDiscountLabel();
    });

    twentyPercentButton.setOnAction(e -> {
      this.discountPercentage = 20;
      updateCurrentDiscountLabel();
    });

    customPercentButton.setOnAction(e -> {
      customInput.setVisible(!customInput.isVisible());
      if (customInput.isVisible()) {
        customInput.requestFocus();
      }
    });

    customInput.setOnAction(e -> {
      try {
        double percentage = Double.parseDouble(customInput.getText());
        if (percentage >= 0 && percentage <= 100) {
          customInput.setVisible(false);
          this.discountPercentage = percentage;
          updateCurrentDiscountLabel();

        } else {
          // TODO: Error handling
        }
      } catch (NumberFormatException ex) {
        // TODO: Error handling
      }
    });

    // Create section container
    VBox section = new VBox(20);
    section.setAlignment(Pos.TOP_CENTER);
    section.getChildren().addAll(titleButton, percentageButtons);

    return section;
  }

  private void updateCurrentDiscountLabel() {
    if (currentDiscountLabel != null) {
      currentDiscountLabel.setText("Current discount: " + discountPercentage + "%");
    }
  }
}
