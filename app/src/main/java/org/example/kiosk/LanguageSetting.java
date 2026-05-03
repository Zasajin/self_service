package org.example.kiosk;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.Chart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.example.buttons.LangBtn;

/**
 * Singleton class that manages language settings for the entire kiosk system.
 */
public class LanguageSetting {

  private static LanguageSetting instance;

  private final Dictionary dictionary;
  private String selectedLanguage;
  public List<String> availableLanguages;
  private final List<Parent> registeredRoots = new ArrayList<>();

  /**
   * Register a scene root to be updated when language changes.
   */
  public void registerRoot(Parent root) {
    if (!registeredRoots.contains(root)) {
      registeredRoots.add(root);
    }
  }

  /**
   * Unregister a scene root if it's no longer needed.
   */
  public void unregisterRoot(Parent root) {
    registeredRoots.remove(root);
  }

  // Private constructor to enforce singleton
  private LanguageSetting() {
    this.dictionary = new Dictionary();
    this.selectedLanguage = "en"; // default language
  }

  /**
   * Returns the singleton instance of LanguageSetting.
   */
  public static LanguageSetting getInstance() {
    if (instance == null) {
      instance = new LanguageSetting();
    }
    return instance;
  }

  /**
   * Changes the application's language if valid.
   */
  public void changeLanguage(String newLanguage) {
    if (newLanguage.equals("en") || newLanguage.equals("sv")) {
      this.selectedLanguage = newLanguage;
      dictionary.toggleLanguage();

      // Update all buttons to show the current language
      LangBtn.updateAllLanguageButtons();

      // Update all registered UI roots
      for (Parent root : registeredRoots) {
        // smartTranslate(root);
        translateLabels(root);
      }
    }
  }

  /**
   * Updates the text of all translatable nodes within the given root container.
   */
  public void smartLabelTranslate(Parent root) {
    for (Node node : root.getChildrenUnmodifiable()) {

      if (node instanceof Label) {
        Label label = (Label) node;
        label.setText(dictionary.smartTranslate(label.getText()));
      }

      if (node instanceof TextField) {
        TextField textField = (TextField) node;
        textField.setPromptText(dictionary.smartTranslate(textField.getPromptText()));
      }

      if (node instanceof PasswordField) {
        PasswordField passwordField = (PasswordField) node;
        passwordField.setPromptText(dictionary.smartTranslate(passwordField.getPromptText()));
      }

      if (node instanceof ListView<?>) {
        ListView<?> listView = (ListView<?>) node;

        if (listView.getItems().isEmpty() || listView.getItems().get(0) instanceof String) {
          @SuppressWarnings("unchecked")
          ListView<String> stringListView = (ListView<String>) listView;

          stringListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
              return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                  super.updateItem(item, empty);
                  if (!empty && item != null) {
                    setText(dictionary.smartTranslate(item));
                  }
                }
              };
            }
          });
        }
      }

      if (node instanceof Chart) {
        Chart chart = (Chart) node;

        if (chart instanceof XYChart<?, ?>) {
          XYChart<?, ?> xyChart = (XYChart<?, ?>) chart;

          if (xyChart.getXAxis() != null) {
            xyChart.getXAxis().setLabel(dictionary.smartTranslate(xyChart.getXAxis().getLabel()));
          }
          if (xyChart.getYAxis() != null) {
            xyChart.getYAxis().setLabel(dictionary.smartTranslate(xyChart.getYAxis().getLabel()));
          }
        }
      }

      if (node instanceof Button) {
        Node graphic = ((Button) node).getGraphic();
        if (graphic instanceof Parent) {
          smartLabelTranslate((Parent) graphic);
        }
        Button button = (Button) node;
        button.setText(dictionary.smartTranslate(button.getText()));
      }

      if (node instanceof Parent) {
        smartLabelTranslate((Parent) node);
      }
    }
  }

  /**
   * Updates the text of all translatable nodes within the given root container
   * using the basic translate method instead of smartTranslate.
   */
  public void translateLabels(Parent root) {
    for (Node node : root.getChildrenUnmodifiable()) {

      if (node instanceof Label) {
        Label label = (Label) node;
        label.setText(dictionary.translate(label.getText()));
      }

      if (node instanceof TextField) {
        TextField textField = (TextField) node;
        textField.setPromptText(dictionary.translate(textField.getPromptText()));
      }

      if (node instanceof PasswordField) {
        PasswordField passwordField = (PasswordField) node;
        passwordField.setPromptText(dictionary.translate(passwordField.getPromptText()));
      }

      if (node instanceof ListView<?>) {
        ListView<?> listView = (ListView<?>) node;

        if (listView.getItems().isEmpty() || listView.getItems().get(0) instanceof String) {
          @SuppressWarnings("unchecked")
          ListView<String> stringListView = (ListView<String>) listView;

          stringListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
              return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                  super.updateItem(item, empty);
                  if (!empty && item != null) {
                    setText(dictionary.translate(item));
                  }
                }
              };
            }
          });
        }
      }

      if (node instanceof Chart) {
        Chart chart = (Chart) node;

        if (chart instanceof XYChart<?, ?>) {
          XYChart<?, ?> xyChart = (XYChart<?, ?>) chart;

          if (xyChart.getXAxis() != null) {
            xyChart.getXAxis().setLabel(dictionary.translate(xyChart.getXAxis().getLabel()));
          }
          if (xyChart.getYAxis() != null) {
            xyChart.getYAxis().setLabel(dictionary.translate(xyChart.getYAxis().getLabel()));
          }
        }
      }

      if (node instanceof Button) {
        Node graphic = ((Button) node).getGraphic();
        if (graphic instanceof Parent) {
          translateLabels((Parent) graphic);
        }
        Button button = (Button) node;
        button.setText(dictionary.translate(button.getText()));
      }

      if (node instanceof Parent) {
        translateLabels((Parent) node);
      }
    }
  }

  public String getSelectedLanguage() {
    return selectedLanguage;
  }

  /**
   * Translates a single string using the current language setting.
   *
   * @param text the original string
   * @return the translated string
   */
  public String translate(String text) {
    return dictionary.translate(text);
  }

  public String smartTranslate(String text) {
    return dictionary.smartTranslate(text);
  }
}
