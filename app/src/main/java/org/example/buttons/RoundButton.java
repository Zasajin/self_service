package org.example.buttons;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Round, reusable button.
 */
public class RoundButton extends AnimatedButton {

  // List to hold images to cycle through
  protected List<String> imagePaths = new ArrayList<>();
  protected int currentImageIndex = 0;

  /**
   * Constructor for the button.
   * Using:
   *
   * @param directoryPath to scan for pictures to cycle through.
   * @param size          set the buttons size
   */
  public RoundButton(String directoryPath, int size) {

    // First scanning for images in provided directory
    loadImagesFromDirectory(directoryPath, size);

    // Sets image from list only if list not empty
    if (!imagePaths.isEmpty()) {

      setImage(imagePaths.get(currentImageIndex), size);

    }

    setStyle(
        "-fx-background-color: transparent;"
            + "-fx-background-radius: 50%;"
            + "-fx-padding: 0;"
            + "-fx-shape: \"M 50,0 A 50,50 0 1, 0 50.0001,0 Z\";");

    setPrefSize(size, size);
    setMaxSize(size, size);

    // calls on method to cycle the images on click
    setOnAction(event -> cycleImages(size));

  }

  /**
   * Adds a new action handler to the button, chaining it with the existing one.
   */
  public void addAction(EventHandler<ActionEvent> newHandler) {
    EventHandler<ActionEvent> existing = getOnAction();

    // Chain the existing and new handlers
    setOnAction(event -> {
      if (existing != null) {
        existing.handle(event);
      }
      newHandler.handle(event);
    });
  }

  // Loads and scans directory for .png's
  private void loadImagesFromDirectory(String directoryPath, int size) {

    try {

      Files.walk(Paths.get(getClass().getClassLoader().getResource(directoryPath).toURI()))
          .filter(path -> path.toString().endsWith(".png"))
          .sorted()
          .forEach(path -> imagePaths.add(path.toUri().toString()));

      // If no images are found, transparent PNG gets set as image
      if (imagePaths.isEmpty()) {

        noImage(size);

      }

      // If error occurs during scanning for images
      // Also transparent image
    } catch (Exception e) {

      e.printStackTrace();

      noImage(size);

    }

  }

  // Image setter
  /**
   * Sets the button's graphic to the specified image path with the given size.
   */
  public void setImage(String imagePath, int size) {

    Image image = new Image(imagePath);
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(size);
    imageView.setFitHeight(size);
    imageView.setPreserveRatio(true);
    setGraphic(imageView);

  }

  /**
   * Cycles to the next image in the list and updates the button's graphic.
   */
  public void cycleImages(int size) {

    if (!imagePaths.isEmpty()) {

      currentImageIndex = (currentImageIndex + 1) % imagePaths.size();
      setImage(imagePaths.get(currentImageIndex), size);

    }

  }

  // Method in case no images are found
  private void noImage(int size) {

    // using Base64-encoded string to generate 1x1 transparent PNG
    // This Base64 string is decoded into a transparent image when
    // the Image constructor is called.
    // This prevents fetching some empty image from the database.
    Image emptyImage = new Image(
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABC"
            + "AQAAAC1HAwCAAAAC0lEQVR42mP8/wcAAwAB/hd5JnkAAAAASUVORK5CYII=");

    ImageView imageView = new ImageView(emptyImage);
    imageView.setFitHeight(size);
    imageView.setFitWidth(size);
    imageView.setPreserveRatio(true);
    setGraphic(imageView);

  }
}
