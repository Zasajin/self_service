package org.example.buttons;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.menu.Ingredient;
import org.example.menu.Menu;
import org.example.menu.Product;
import org.example.menu.Single;

/**
 * The SearchBar class provides a user interface component for searching items
 * by name, price, ingredients, and category. It displays the search results
 * in a list view and interacts with a database connection to fetch data.
 */
public class SearchBar extends VBox {
  private final TextField nameField;
  private final TextField priceField;
  private final CheckBox ingredientCheckbox;
  private final Button searchButton;
  private final ListView<String> resultList;
  private final ComboBox<String> categoryCombo;
  private java.util.function.Consumer<Product> resultSelectHandler;
  private java.util.function.Consumer<List<Product>> resultsListHandler;
  private List<Single> filteredSingles = new ArrayList<>();
  private final List<Single> allSingles = new ArrayList<>();

  /**
   * Constructs a SearchBar instance with the specified database connection.
   */
  public SearchBar() {

    this.setPadding(new Insets(10));
    this.setSpacing(10);

    nameField = new TextField();
    nameField.setPromptText("Search by name...");

    String textFieldStyle = ("-fx-text-fill: #333333;"
        + "-fx-prompt-text-fill: #666666;"
        + "-fx-background-color: #f0f0f0;"
        + "-fx-border-color: #999999;"
        + "-fx-border-width: 1px;"
        + "-fx-border-radius: 4px;"
        + "-fx-padding: 6px 8px;"
        + "-fx-font-size: 14px;"
        + "-fx-pref-width: 180px;"
        + "-fx-pref-height: 32px;");

    priceField = new TextField();
    priceField.setPromptText("Search by max price...");
    nameField.setStyle(textFieldStyle);
    priceField.setStyle(textFieldStyle);

    // Set field sizes
    nameField.setPrefWidth(200);
    priceField.setPrefWidth(150);

    ingredientCheckbox = new CheckBox("Search Ingredients");

    categoryCombo = new ComboBox<>();
    categoryCombo.getItems().addAll("-- Any --", "MAIN", "DRINK", "DESSERT", "EXTRA");
    categoryCombo.setPromptText("Search by category...");
    categoryCombo.getSelectionModel().selectFirst();

    searchButton = new Button("Search");
    searchButton.setStyle(
        "-fx-background-color: #4285f4;"
            + "-fx-text-fill: white;"
            + "-fx-font-weight: bold;"
            + "-fx-padding: 4px 12px;"
            + "-fx-background-radius: 4px;");

    resultList = new ListView<>();
    resultList.setPrefHeight(100);

    try {
      Menu menu = new Menu();
      allSingles.clear();
      allSingles.addAll(menu.getMains());
      allSingles.addAll(menu.getSides());
      allSingles.addAll(menu.getDrinks());
      allSingles.addAll(menu.getDesserts());
      allSingles.addAll(menu.getExtras());

    } catch (SQLException e) {
      resultList.getItems().add("Error loading items for suggestions.");
      e.printStackTrace();
    }
    resultList.setStyle(
        "-fx-font-size: 12px;"
            + "-fx-padding: 2px;"
            + "-fx-cell-size: 25px;");

    HBox inputFields = new HBox(
        10, nameField, priceField, ingredientCheckbox, categoryCombo, searchButton);
    this.getChildren().addAll(inputFields, new Label("Search Results:"), resultList);
    nameField.textProperty().addListener((obs, oldText, newText) -> {
      resultList.getItems().clear();
      if (newText == null || newText.isBlank()) {
        return;
      }
      String query = newText.toLowerCase();
      String priceText = priceField.getText().trim();
      Float priceLimit = Float.MAX_VALUE;
      boolean hasPrice = !priceText.isEmpty();

      if (hasPrice) {
        try {
          priceLimit = Float.parseFloat(priceText);
        } catch (NumberFormatException ex) {
          resultList.getItems().add("Invalid price input.");
          return;
        }
      }

      List<Single> filtered = new ArrayList<>();
      List<String> suggestions = new ArrayList<>();

      for (Single s : allSingles) {
        String itemName = s.getName().toLowerCase();
        double itemPrice = s.getPrice();

        boolean nameMatches = itemName.contains(query);
        boolean priceMatches = itemPrice <= priceLimit;

        if (nameMatches && priceMatches) {
          filtered.add(s);
        }

        if (itemName.toLowerCase().startsWith(query) && !suggestions.contains(itemName)) {
          suggestions.add(itemName);
        }
        if (suggestions.size() == 10) {
          break;
        }

      }

      Collections.sort(suggestions);

      if (!suggestions.isEmpty()) {
        resultList.getItems().add("-- Suggestions --");

        for (String suggestion : suggestions) {
          Label suggestionLabel = new Label(suggestion + " (Suggested Search term)");
          suggestionLabel.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
          resultList.getItems().add(suggestionLabel.getText());

        }

      }

      if (!filtered.isEmpty()) {
        resultList.getItems().add("-- Filtered Results --");
        for (Single s : filtered) {
          resultList.getItems().add("Single: " + s.getName() + " - " + s.getPrice() + " SEK");
        }
      }

      if (resultsListHandler != null) {
        List<Product> products = new ArrayList<>(filtered);
        resultsListHandler.accept(products);
      }

    });
    searchButton.setOnAction(e -> {
      resultList.getItems().clear();
      for (int i = 0; i < resultList.getItems().size(); i++) {
        String item = resultList.getItems().get(i);
        if (item.startsWith("-- Suggestions") || item.contains("Suggested Search term")) {
          resultList.getItems().remove(i);
          i--;
        }
      }
      String name = nameField.getText().trim();
      String priceText = priceField.getText().trim();
      boolean isIngredientSearch = ingredientCheckbox.isSelected();
      String selectedCategory = categoryCombo.getValue();

      try {
        if (isIngredientSearch) {
          Ingredient ingredientDummy = new Ingredient(0, "");
          boolean hasName = !name.isEmpty();
          boolean hasPrice = !priceText.isEmpty();

          List<Ingredient> ingredients;
          if (!hasName && !hasPrice) {
            // resultList.getItems().add("Please enter name or price.");
            ingredients = ingredientDummy.getAllIngredients();
          } else if (hasName && hasPrice) {
            float price = Float.parseFloat(priceText);
            ingredients = ingredientDummy.searchIngredientByNameAndPrice(name, price);
          } else if (!name.isEmpty()) {
            ingredients = ingredientDummy.searchIngredientsByName(name);

          } else {
            float price = Float.parseFloat(priceText);
            ingredients = ingredientDummy.searchIngredientsByPrice(price);
          }
          if (ingredients.isEmpty()) {
            resultList.getItems().add("No ingredients found.");
          } else {
            for (Ingredient ing : ingredients) {
              resultList.getItems().add("Ingredient: " + ing.getName());
            }
          }
          return;
        }

        // boolean hasName = !name.isEmpty();
        boolean hasPrice = !priceText.isEmpty();
        // boolean hasCategory = selectedCategory != null &&
        // !selectedCategory.equals("-- Any --");
        Float priceLimit = Float.MAX_VALUE;
        if (hasPrice) {
          try {
            priceLimit = Float.parseFloat(priceText);
          } catch (NumberFormatException ex) {
            resultList.getItems().add("Invalid price input.");
            return;
          }
        }

        Menu menu = new Menu();
        allSingles.clear();
        if (selectedCategory.equals("MAIN") || selectedCategory.equals("BURGERS")) {
          allSingles.addAll(menu.getMains());
        } else if (selectedCategory.equals("SIDES")) {
          allSingles.addAll(menu.getSides());
        } else if (selectedCategory.equals("DRINK") || selectedCategory.equals("DRINKS")) {
          allSingles.addAll(menu.getDrinks());
        } else if (selectedCategory.equals("DESSERT") || selectedCategory.equals("DESSERTS")) {
          allSingles.addAll(menu.getDesserts());
        } else if (selectedCategory.equals("EXTRA") || selectedCategory.equals("EXTRAS")) {
          allSingles.addAll(menu.getExtras());
        } else {
          // "-- Any --" or unrecognized value
          allSingles.addAll(menu.getMains());
          allSingles.addAll(menu.getSides());
          allSingles.addAll(menu.getDrinks());
          allSingles.addAll(menu.getDesserts());
          allSingles.addAll(menu.getExtras());

        }

        List<Single> filtered = new ArrayList<>();

        for (Single single : allSingles) {
          String singleName = single.getName().toLowerCase();
          double singlePrice = single.getPrice();

          boolean nameMatches = true;
          boolean priceMatches = true;
          if (!name.isEmpty()) {
            String searchTerm = name.toLowerCase();
            nameMatches = singleName.contains(searchTerm);
          }

          if (singlePrice > priceLimit) {
            priceMatches = false;
          } else {
            priceMatches = true;
          }

          if (nameMatches && priceMatches) {
            filtered.add(single);
          }
        }
        boolean nameFilterActive = !name.isEmpty();
        boolean priceFilterActive = !priceText.isEmpty();

        String searchTerm = name.toLowerCase();

        Comparator<Single> nameComparator = new Comparator<Single>() {
          public int compare(Single p1, Single p2) {
            String n1 = p1.getName().toLowerCase();

            String n2 = p2.getName().toLowerCase();

            boolean s1Starts = n1.startsWith(searchTerm);

            boolean s2Starts = n2.startsWith(searchTerm);

            if (s1Starts && !s2Starts) {
              return -1;
            }

            if (!s1Starts && s2Starts) {
              return 1;
            }

            return n1.compareTo(n2);

          }
        };

        if (!searchTerm.isEmpty()) {
          filtered.sort(nameComparator);
        }
        Comparator<Single> priceComparator = Comparator.comparingDouble(Single::getPrice);

        if (nameFilterActive && !priceFilterActive) {
          filtered.sort(nameComparator);

        } else if (!nameFilterActive && priceFilterActive) {
          filtered.sort(priceComparator.reversed());

        } else if (nameFilterActive && priceFilterActive) {
          filtered.sort(nameComparator.thenComparing(priceComparator));

        }

        if (filtered.isEmpty()) {
          resultList.getItems().add("No items found.");
        } else {

          for (Single s : filtered) {
            resultList.getItems().add("Single: " + s.getName() + " - " + s.getPrice() + " SEK");
          }
        }
        filteredSingles = filtered;

      } catch (SQLException ex) {
        resultList.getItems().add("SQL Error: " + ex.getMessage());
      } catch (NumberFormatException ex) {
        resultList.getItems().add("Invalid price input.");
      }

      if (resultsListHandler != null) {
        List<Product> productList = new ArrayList<>(filteredSingles);
        resultsListHandler.accept(productList);
      }

    });

    resultList.setOnMouseClicked(e -> {
      String selectedText = resultList.getSelectionModel().getSelectedItem();
      if (selectedText == null || resultSelectHandler == null) {
        return;
      }
      if (selectedText.startsWith("--") || selectedText.contains("Suggested Search term")) {
        String actualText = selectedText.replace(" (Suggested Search term)", "").trim();

        nameField.setText(actualText);
        searchButton.fire();
        return;
      }
      try {
        Menu menu = new Menu();
        List<Single> allSingles = new ArrayList<>();
        allSingles.addAll(menu.getMains());
        allSingles.addAll(menu.getSides());
        allSingles.addAll(menu.getDrinks());
        allSingles.addAll(menu.getDesserts());
        allSingles.addAll(menu.getExtras());

        String clickedItemName = selectedText.split(" - ")[0].replace("Single: ", "").trim();

        for (Single s : allSingles) {
          if (s.getName().equalsIgnoreCase(clickedItemName)) {
            resultSelectHandler.accept(s);
            break;
          }
        }

      } catch (SQLException ex) {
        ex.printStackTrace();

      }
    });

  }

  /**
   * Getter for the text of the name.
   *
   * @return String text name of the text field
   */
  public String getText() {
    String name = nameField.getText();
    return name.trim();
  }

  /**
   * Returns the currently selected category from the category combo box.
   */
  public String getSelectedCategory() {
    String selected = categoryCombo.getValue();
    if (selected == null || selected.equals("-- Any --")) {
      return "";
    } else {
      return selected;
    }
  }

  /**
   * Returns the price filter entered by the user, or -1 if the field is empty.
   */
  public double getPriceFilter() throws NumberFormatException {
    String input = priceField.getText().trim();
    if (input.isEmpty()) {
      return -1;
    } else {
      return Double.parseDouble(input);
    }
  }

  public void setOnResultSelectHandler(java.util.function.Consumer<Product> handler) {
    this.resultSelectHandler = handler;
  }

  public void setOnResultsListHandler(java.util.function.Consumer<List<Product>> handler) {
    this.resultsListHandler = handler;
  }

  // Getter for nameField
  public TextField getNameField() {
    return nameField;
  }

  // Getter for priceField
  public TextField getPriceField() {
    return priceField;
  }
}