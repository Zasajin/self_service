package org.example.screens;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.boxes.AddRemoveBlock;
import org.example.buttons.ArrowButton;
import org.example.buttons.BlackButtonWithImage;
import org.example.buttons.ColorBtnOutlineImage;
import org.example.buttons.ColorButtonWithImage;
import org.example.buttons.ColorSquareButtonWithImage;
import org.example.buttons.LangBtn;
import org.example.buttons.TitleLabel;
import org.example.kiosk.LabelManager;
import org.example.kiosk.LanguageSetting;
import org.example.menu.Ingredient;
import org.example.menu.Meal;
import org.example.menu.Single;
import org.example.orders.Cart;
import org.example.sql.SqlQueries;

/**
 * Screen for the details of an Item.
 * Customer should be able to adjust an Item here
 * (add, remove ingredients and such).
 */
public class ItemDetails {

  SqlQueries queries;

  /**
   * This is the constructor of Item details scene.
   * (Only used for assigning variables.)
   *
   * @throws SQLException sql errors
   */
  public ItemDetails() throws SQLException {
    queries = new SqlQueries();
  }

  /**
   * Creating a scene for a specific item, displaying all item details.
   * Only for single items
   *
   * @param primaryStage what is the primary stage
   * @param prevScene    what was the previous stage
   * @param item         the item object itself
   * @param cart         the cart where all items are
   * @return scene containing all item details
   */
  public CustomScene create(Stage primaryStage, Scene prevScene, Single item, Cart cart)
      throws SQLException {
    try {
      item.setIngredients();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    List<Ingredient> ingredients = item.ingredients;

    List<Integer> quantities = new ArrayList<>();
    /*
     * Making a deep copy of item.quantity.
     */
    for (int i = 0; i < item.quantity.size(); i++) {
      quantities.add(item.quantity.get(i));
    }

    VBox ingredientBox = new VBox(10);
    ingredientBox.setAlignment(Pos.TOP_RIGHT);

    int visibleCount = 7;
    // Wraps the index in an array
    final int[] currentStartIndex = { 0 };

    // Storing our AddRemoveBlocks to store quantities
    List<AddRemoveBlock> blocks = new ArrayList<>();

    // Pre-creating the Blocks corresponding to ingredients.size()
    // To populate blocks
    for (int i = 0; i < ingredients.size(); i++) {

      blocks.add(new AddRemoveBlock(quantities.get(i)));

    }

    // Adds the first 7 ingredients
    for (int i = 0; i < Math.min(visibleCount, ingredients.size()); i++) {
      Label ingrLabel = new Label(ingredients.get(i).getName());
      ingrLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: normal;");
      LabelManager.register(ingrLabel);

      // Calling on corresponding block from List
      AddRemoveBlock addRemoveBlock = blocks.get(i);

      HBox row = new HBox(100, ingrLabel, addRemoveBlock);
      row.setAlignment(Pos.CENTER_RIGHT);
      ingredientBox.getChildren().add(row);
    }

    // Scroll button (initially facing down)
    ArrowButton scrollButton = new ArrowButton(true, false);
    scrollButton.setRotate(-90);
    scrollButton.setMaxHeight(160);
    scrollButton.setMinHeight(160);

    if (ingredients.size() <= visibleCount) {
      // If there are not enough ingredients, diactivate the scroll button
      scrollButton.setDisable(true);
      scrollButton.setOpacity(0.3);
    } else {
      // Otherwise, show the scroll button
      scrollButton.setDisable(false);
      scrollButton.setOpacity(1);
    }

    // Set the action for the scroll button
    scrollButton.setOnAction(e -> {
      // Logic to handle scrolling
      if (currentStartIndex[0] + visibleCount >= ingredients.size()) {
        // If we are at the bottom, this resets the index to 0 and scrolls back to the
        // top
        currentStartIndex[0] = 0;
        // And sets arrow to face down
        scrollButton.setRotate(-90);
      } else {
        // Otherwise, scroll down by incrementing the index
        currentStartIndex[0]++;
        // If it reaches the bottom, it turns the arrow up
        if (currentStartIndex[0] + visibleCount >= ingredients.size()) {
          scrollButton.setRotate(90);
        }
      }

      // Clear and refill VBox with updated ingredients
      ingredientBox.getChildren().clear();
      for (int i = currentStartIndex[0]; i < Math.min(currentStartIndex[0] + visibleCount,
          ingredients.size()); i++) {

        Label ingrLabel = new Label(ingredients.get(i).getName());
        ingrLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: normal;");
        LabelManager.register(ingrLabel);

        // Calling on corresponding block from List
        AddRemoveBlock addRemoveBlock = blocks.get(i);

        HBox row = new HBox(100, ingrLabel, addRemoveBlock);
        row.setAlignment(Pos.CENTER_RIGHT);
        ingredientBox.getChildren().add(row);
      }
      LanguageSetting.getInstance().translateLabels(ingredientBox);
    });

    // Putting the ingredient box and the scroll button together in a vboc
    VBox ingredientListBox = new VBox(20, ingredientBox, scrollButton);
    ingredientListBox.setAlignment(Pos.CENTER);

    // Set the item description label
    String descriptionText = item.getDescription();

    if (descriptionText == null || descriptionText.trim().isEmpty()) {
      try {
        String fetchedDescription = queries.getDescriptionByName(item.getName());

        if (fetchedDescription != null && !fetchedDescription.trim().isEmpty()) {
          descriptionText = fetchedDescription;
          item.setDescription(fetchedDescription);
        } else {
          descriptionText = "This is a yummy " + item.getName().toLowerCase();
        }
      } catch (SQLException e) {
        e.printStackTrace();
        descriptionText = "This is a yummy " + item.getName().toLowerCase();
      }
    }

    var transaltedDescription = LanguageSetting.getInstance().translate(descriptionText);

    // Setting the description text to a maximum of 80 characters per line
    transaltedDescription = setLinesOfTheLabel(transaltedDescription, 70);

    var descriptionLabel = new Label(transaltedDescription);
    descriptionLabel.setStyle(
        "-fx-font-size: 20px;"
            + "-fx-font-weight: normal;");
    LabelManager.register(descriptionLabel);

    // Item label
    var nameLabel = new TitleLabel(item.getName());

    // Left side of the top part of the screen
    VBox nameAndDescriptionBox = new VBox(10);
    nameAndDescriptionBox.getChildren().addAll(nameLabel, descriptionLabel);
    VBox leftSide = new VBox(20);
    leftSide.setAlignment(Pos.TOP_CENTER);
    leftSide.setPadding(new Insets(0, 0, 0, 100));
    leftSide.getChildren().addAll(nameAndDescriptionBox, ingredientListBox);

    InputStream inputStream = getClass().getResourceAsStream(item.getImagePath());

    // Initiating the image view
    ImageView imageView;

    // Errorhandling when no image found
    if (inputStream == null) {
      System.err.println("ERROR: Image not found - " + item.getName());

      // using Base64-encoded string to generate 1x1 transparent PNG
      // This Base64 string is decoded into a transparent image when
      // the Image constructor is called.
      // This prevents fetching some empty image from the database.
      Image emptyImage = new Image(
          "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABC"
              + "AQAAAC1HAwCAAAAC0lEQVR42mP8/wcAAwAB/hd5JnkAAAAASUVORK5CYII=");

      // Add empty generated image to View
      imageView = new ImageView(emptyImage);

      // Image found (input stream not empty)
    } else {
      imageView = new ImageView(new Image(inputStream));
    }

    imageView.setFitHeight(400);
    imageView.setPreserveRatio(true);

    // Price underneath the picture
    Label priceLabel = new Label(String.format("%.0f :-", item.getPrice()));
    priceLabel.setStyle(
        "-fx-font-size: 35px;"
            + "-fx-font-weight: bold;");
    LabelManager.register(priceLabel);

    // Wrapper to align the Label in its VBox
    HBox priceWrapper = new HBox(priceLabel);
    priceWrapper.setAlignment(Pos.BOTTOM_RIGHT);

    // VBox for the Picture
    VBox rightSide = new VBox(20);
    rightSide.setPadding(new Insets(0, 200, 0, 0));
    rightSide.setAlignment(Pos.CENTER);
    rightSide.getChildren().addAll(imageView, priceWrapper);

    var backButton = new ColorSquareButtonWithImage("Back",
        "back.png");

    backButton.setOnAction(e -> {
      primaryStage.setScene(prevScene);
    });

    // HBox for the upper part of the screen
    HBox topContainer = new HBox();
    topContainer.setPadding(new Insets(20));
    topContainer.setAlignment(Pos.CENTER);
    topContainer.getChildren().addAll(leftSide, rightSide);

    var addToCartButton = new ColorButtonWithImage("Add To Cart",
        "cart_wh.png");

    addToCartButton.setOnAction(e -> {
      try {
        if (item.isInMeal()) {
          primaryStage.setScene(createMealUpsell(primaryStage, prevScene, item,
              blocks, quantities));
        } else {
          // Making a new product with the modified ingredients.
          Single newProduct = new Single(item.getId(), item.getName(), item.getPrice(),
              item.getType(), item.getImagePath(), item.ingredients);
          newProduct.setModefied(true);
          save(blocks, quantities, newProduct, item.quantity);
          cart.addProduct(newProduct);
          primaryStage.setScene(prevScene);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });

    // Box for add to cart and back
    HBox bottomRightBox = new HBox(30);
    bottomRightBox.setAlignment(Pos.CENTER_RIGHT);
    bottomRightBox.getChildren().addAll(addToCartButton, backButton);

    // Language Button
    // cycles images on click
    // Language button
    var langButton = new LangBtn();
    HBox bottomLeftBox = new HBox(langButton);
    bottomLeftBox.setAlignment(Pos.BOTTOM_LEFT);

    // Spacer for bottom part of the Screen
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Bottom part of the screen
    HBox bottomContainer = new HBox();
    bottomContainer.setPadding(new Insets(0, 0, 0, 0));
    bottomContainer.getChildren().addAll(bottomLeftBox, spacerBottom, bottomRightBox);
    bottomContainer.setAlignment(Pos.CENTER);

    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setTop(topContainer);
    layout.setLeft(leftSide);
    layout.setRight(rightSide);
    layout.setBottom(bottomContainer);

    // Translate button action
    langButton.addAction(event -> {
      LanguageSetting lang = LanguageSetting.getInstance();
      String newLang;
      if (lang.getSelectedLanguage().equals("en")) {
        newLang = "sv";
      } else {
        newLang = "en";
      }
      lang.changeLanguage(newLang);
      lang.translateLabels(layout);
    });

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(layout);
    lang.translateLabels(layout);

    CustomScene scene = new CustomScene(layout, 1920, 1080);

    // Reads and applies the customized background color
    Color bgColor = BackgroundColorStore.getCurrentBackgroundColor();

    if (bgColor != null) {

      scene.setBackgroundColor(bgColor);

    }

    return scene;

  }

  /**
   * Method to save the quantites.
   *
   * @param blocks    the list of add and remove blocks
   * @param quantitys the list of quantites
   * @param item      the item
   */
  private void save(List<AddRemoveBlock> blocks, List<Integer> quantitys,
      Single item, List<Integer> basequant) {
    for (int i = 0; i < quantitys.size(); i++) {
      quantitys.set(i, blocks.get(i).getQuantity());
      // setting the variable display to the base again.
      blocks.get(i).setQuantity(basequant.get(i));
    }
    item.quantity = quantitys;
  }

  /**
   * The Meal upsell screen.
   *
   * @param primaryStage the primarystage
   * @param mainMenu     the main menu stage
   * @param item         the product
   * @param blocks       the list of quantity change blocks
   * @param quantities   the base quantities
   * @return the scene
   */
  public CustomScene createMealUpsell(Stage primaryStage, Scene mainMenu, Single item,
      List<AddRemoveBlock> blocks, List<Integer> quantities) {
    var mainText = new TitleLabel("Do you want to make it a meal?");

    LabelManager.register(mainText);

    var yesButton = new BlackButtonWithImage("Yes", "/green_tick.png");
    var noButton = new ColorBtnOutlineImage("No", "/cancel.png");

    HBox buttonBox = new HBox(20);
    buttonBox.setPadding(new Insets(50));
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(yesButton, noButton);

    yesButton.setOnMouseClicked(e -> {
      try {
        Meal meal = loadMealByProductId(item.getId());
        if (meal != null) {
          MealCustomizationScreen mealScreen = new MealCustomizationScreen();
          Scene sideScene = mealScreen.createSideSelectionScene(
              primaryStage,
              mainMenu,
              meal);
          primaryStage.setScene(sideScene);
        } else {
          System.err.println("couldnt load meal for prod id - " + item.getId());
        }
      } catch (SQLException ex) {
        ex.printStackTrace(); // Handle the exception (e.g., log it or show an error message)
      }
    });

    noButton.setOnMouseClicked(e -> {
      // Making a new product with the modified ingredients.
      Single newProduct = new Single(item.getId(), item.getName(), item.getPrice(),
          item.getType(), item.getImagePath(), item.ingredients);
      newProduct.setModefied(true);
      save(blocks, quantities, newProduct, item.quantity);
      Cart.getInstance().addProduct(newProduct);
      primaryStage.setScene(mainMenu);
    });

    VBox layout = new VBox();
    layout.setAlignment(Pos.CENTER);
    layout.getChildren().addAll(mainText, buttonBox);

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(layout);
    lang.translateLabels(layout);

    CustomScene scene = new CustomScene(layout, 1920, 1080);

    // Reads and applies the customized background color
    Color bgColor = BackgroundColorStore.getCurrentBackgroundColor();

    if (bgColor != null) {

      scene.setBackgroundColor(bgColor);

    }

    return scene;
  }

  /**
   * loads a meal object from the db using the given product id.
   * This method queries the meal table and finds a meal associated with the
   * specified product
   *
   * @param productId the product id of the product to find a corresponding meal
   *                  for
   * @return either the meal if its found or null if no meal is linked
   * @throws SQLException we get an exception if a db access error occurs or sql
   *                      is invalid
   */
  public Meal loadMealByProductId(int productId) throws SQLException {
    SqlQueries pool = new SqlQueries();
    return pool.loadMealByProductId(productId);
  }

  private String setLinesOfTheLabel(String text, int maxLineLength) {
    StringBuilder wrapped = new StringBuilder();
    int lineLength = 0;

    for (String word : text.split(" ")) {
      // If adding this word exceeds line limit, start a new line
      if (lineLength + word.length() > maxLineLength) {
        wrapped.append("\n");
        lineLength = 0;
      } else if (lineLength > 0) {
        wrapped.append(" ");
        lineLength += 1; // account for space
      }

      wrapped.append(word);
      lineLength += word.length();
    }

    return wrapped.toString();
  }
}
