package org.example.buttons;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * A class for a drop box fow options with a label on top.
 */
public class DropBoxWithLabel extends VBox {
  private final ComboBox<String> comboBox;

  /**
   * A constructor for the drop box.
   *
   * @param labelText text that acts as a Label for the combo/drop box.
   */
  public DropBoxWithLabel(String labelText) {
    label = new Label(labelText);
    label.setStyle("-fx-font-size: 38; -fx-text-fill: black;");

    comboBox = new ComboBox<>();
    comboBox.setPrefWidth(300);
    comboBox.setStyle("-fx-font-size: 24px; -fx-background-color: white;");

    setSpacing(5);
    setAlignment(Pos.TOP_LEFT);
    getChildren().addAll(label, comboBox);
  }

  public ComboBox<String> getComboBox() {
    return comboBox;
  }

  public String getSelectedItem() {
    return comboBox.getSelectionModel().getSelectedItem();
  }

  private Label label;

  public Label getLabel() {
    return label;
  }

  /**
   * Sets the text of the label.
   */
  public void setButtonText(String text) {
    if (label != null) {
      label.setText(text);
    }
  }
}
