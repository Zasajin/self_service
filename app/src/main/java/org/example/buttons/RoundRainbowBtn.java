package org.example.buttons;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Round button with rainbow border and rainbow "Design" label in the center.
 */
public class RoundRainbowBtn extends AnimatedButton {

  private Label rainbowLabel;

  /** Constructs a round rainbow button with default label text. */
  public RoundRainbowBtn() {
    // Set fixed round size (140x140)
    this.setPrefSize(140, 140);
    this.setMinSize(140, 140);
    this.setMaxSize(140, 140);

    // Create label with rainbow text
    rainbowLabel = new Label("Design");
    rainbowLabel.setStyle(
        "-fx-font-size: 24px;"
            + "-fx-font-weight: bold;"
            + "-fx-background-color: transparent;"
            + "-fx-text-fill: linear-gradient(to right, "
            + "red, orange, green, blue, indigo, violet);");

    // Button style with rainbow border
    this.setStyle(
        "-fx-background-color: white;"
            + "-fx-border-color: linear-gradient(to right, "
            + "red, orange, green, blue, indigo, violet);"
            + "-fx-border-width: 4px;"
            + "-fx-border-radius: 70;"
            + "-fx-background-radius: 70;");

    // StackPane to center label
    StackPane content = new StackPane(rainbowLabel);
    content.setAlignment(Pos.CENTER);
    content.setPadding(new Insets(10));

    // Attach content to button
    this.setGraphic(content);
  }

  /**
   * Sets the button's center label text.
   *
   * @param text The text to display in the center of the button.
   */
  public void setButtonText(String text) {
    rainbowLabel.setText(text);
  }

  /**
   * Returns the rainbow label component.
   *
   * @return The label inside the button.
   */
  public Label getRainbowLabel() {
    return rainbowLabel;
  }
}
